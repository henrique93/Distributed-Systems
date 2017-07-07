package org.komparator.mediator.ws.cli.handler;

import java.util.Date;
import java.util.Set;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;

import java.security.PublicKey;
import java.text.SimpleDateFormat;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.komparator.security.CryptoUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class MediatorClientHandler implements SOAPHandler<SOAPMessageContext> {
	
	private PublicKey key = null;

	private static Document SOAPMessageToDOMDocument(SOAPMessage msg) throws Exception {

        // SOAPPart implements org.w3c.dom.Document interface
        Document part = msg.getSOAPPart();

        return part;
    }

	public boolean handleMessage(SOAPMessageContext smc) {
		Boolean outbound = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		
		String plainText = null;
		
		if (outbound) {
			// outbound message
			try {
			SOAPMessage msg = smc.getMessage();
			SOAPPart sp = msg.getSOAPPart();
			SOAPEnvelope se = sp.getEnvelope();
			Document xmlDocument = SOAPMessageToDOMDocument(msg);
			
			// add header
			SOAPHeader sh = se.getHeader();
			if (sh == null)
				sh = se.addHeader();
			
			// add header element (name, namespace prefix, namespace)
			Name dateName = se.createName("messageID","messageID","urn:example");
			SOAPHeaderElement dateElement = sh.addHeaderElement(dateName);
			
			// Instantiate a Date object
		    Date date = new Date();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			String dateStr = sdf.format(date);

			dateElement.addTextNode(dateStr);
			
			
			
			
			Node bodyNode = xmlDocument.getDocumentElement().getLastChild().getFirstChild().getFirstChild();
			for (Node node = bodyNode; node != null; node = node.getNextSibling()) {
				if (node.getNodeName().equals("creditCardNr")) {
					
					if(key==null) {
						key = CryptoUtil.getPublicKey("A50_Mediator.cer");
					}
					
					plainText = node.getFirstChild().getTextContent();
					byte[] plainBytes = plainText.getBytes("UTF-8");
					
					byte[] cipherBytes = CryptoUtil.asymCipher(plainBytes, key);
					String cipherText = printBase64Binary(cipherBytes);
				
					node.getFirstChild().setTextContent(cipherText);
			  		
					break;
				}
			}

			
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		} else {
			// inbound message
			
			


		}

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

