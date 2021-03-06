package org.komparator.supplier.ws.handler;

import java.util.Iterator;
import java.util.Set;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

import java.io.ByteArrayOutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.komparator.security.CryptoUtil;
import org.komparator.supplier.ws.SupplierEndpointManager;

public class SupplierServerHandler implements SOAPHandler<SOAPMessageContext> {

	public static final String CLASS_NAME = SupplierServerHandler.class.getSimpleName();
	public static final String TOKEN = "server-handler";
	
	private static byte[] SOAPMessageToByteArray(SOAPMessage msg) throws Exception {
        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
        byte[] msgByteArray = null;

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        Source source = msg.getSOAPPart().getContent();
        Result result = new StreamResult(byteOutStream);
        transformer.transform(source, result);

        msgByteArray = byteOutStream.toByteArray();
        return msgByteArray;
    }
	
	
	public boolean handleMessage(SOAPMessageContext smc) {
		Boolean outbound = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (outbound) {
			// outbound message
			
			try {
				String wsName = SupplierEndpointManager.getWsName();
				
				// get SOAP envelope
				SOAPMessage msg = smc.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();

				// add header
				SOAPHeader sh = se.getHeader();
				if (sh == null)
					sh = se.addHeader();

				/** Date */
				// add header element (name, namespace prefix, namespace)
				Name dateName = se.createName("date","date","urn:example");
				SOAPHeaderElement dateElement = sh.addHeaderElement(dateName);
				
				// Instantiate a Date object
			    Date date = new Date();
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				String dateStr = sdf.format(date);

				dateElement.addTextNode(dateStr);

				System.out.printf("%s sent message with date and time: '%s' %n", CLASS_NAME, dateStr );  
				
				/** Server Name */
				Name serverName = se.createName("ServerName","ServerName","urn:example");
				SOAPHeaderElement serverElement = sh.addHeaderElement(serverName);
				
				serverElement.addTextNode(wsName);
				
				System.out.printf("%s sent message with server name: '%s' %n", CLASS_NAME, wsName);
				
				/** Signature */
				String signText = null;
				String alias = wsName.toLowerCase();
				wsName = wsName + ".jks";
				try {
					byte[] messageByteArray = SOAPMessageToByteArray(msg);
			    	PrivateKey privKey = CryptoUtil.getPrivateKeyFromKeyStoreResource(wsName,"fjJay9Ii".toCharArray(), alias, "fjJay9Ii".toCharArray());
			    	byte[] signArray = CryptoUtil.makeDigitalSignature(messageByteArray, privKey);
			  		signText = printBase64Binary(signArray);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				
				Name signatureName = se.createName("signature","signature","urn:example");
				SOAPHeaderElement signatureElement = sh.addHeaderElement(signatureName);
				
				
				signatureElement.addTextNode(signText);
				
				System.out.printf("%s sent message with signature: '%s' %n", CLASS_NAME, signText);
				
				
			} catch (SOAPException e) {
				System.out.printf("Failed to add SOAP header because of %s%n", e);
			}
			
			
			System.out.println();
		} else {
			// inbound message

			// get token from request SOAP header
			try {
				// get SOAP envelope header
				SOAPMessage msg = smc.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();
				SOAPHeader sh = se.getHeader();

				// check header
				if (sh == null) {
					System.out.println("Header not found.");
					return true;
				}
				
				// get first header element
				Name date = se.createName("date","date","urn:example");
				Iterator<?> it = sh.getChildElements(date);
				// check header element
				if (!it.hasNext()) {
					System.out.printf("Header element date not found.%n");
					return true;
				}
				SOAPElement dateElement = (SOAPElement) it.next();
				
				
				// Instantiate a Date object
			    Date dateNow = new Date();
				
				// *** #4 ***
				// get header element value
				String headerValue = dateElement.getValue();
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				Date datePast = sdf.parse(headerValue);
				
				long seconds = (dateNow.getTime()-datePast.getTime())/1000;
				
				if(seconds >= 3) {
					throw new RuntimeException("SOAP message is not fresh");
				}
				
				System.out.printf("%s got message with date and time: '%s' from SupplierClientHandler  %n", CLASS_NAME, headerValue);
				
				
				Name signature = se.createName("signature","signature","urn:example");
				it = sh.getChildElements(signature);
				// check header element
				if (!it.hasNext()) {
					System.out.printf("Header element signature not found.%n");
					return true;
				}
				
				SOAPElement signatureElement = (SOAPElement) it.next();
				headerValue = signatureElement.getValue();
				System.out.printf("%s got message with signature: '%s' from SupplierClientHandler  %n", CLASS_NAME, headerValue);

				byte[] signatureBytes = parseBase64Binary(headerValue);
				
				sh.removeChild(signatureElement);
				
			
				byte[] messageByteArray = null;
				try {
					messageByteArray = SOAPMessageToByteArray(msg);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				
				PublicKey pubKey = CryptoUtil.getPublicKey("A50_Mediator.cer");
				try {
					boolean sig = CryptoUtil.verifyDigitalSignature(signatureBytes, messageByteArray, pubKey);
					if (!sig) 
						throw new RuntimeException("Failed at SupplierClientHandler: Public key does not match the private key that encripted this message");
				} catch (Exception e) {
					throw new RuntimeException(e);
				} 
				
			} catch (SOAPException | ParseException e) {
				System.out.printf("Failed to get SOAP header because of %s%n", e);
			}

		}
		System.out.println();
		return true;
	}

	public boolean handleFault(SOAPMessageContext smc) {
		return true;
	}

	public Set<QName> getHeaders() {
		return null;
	}

	public void close(MessageContext messageContext) {
	}

}

