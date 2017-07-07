package org.komparator.mediator.ws.handler;

import java.util.Set;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

import java.security.PrivateKey;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.komparator.security.CryptoUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class MediatorServerHandler implements SOAPHandler<SOAPMessageContext> {
	
	private PrivateKey priv = null;

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

		} else {
			 //inbound message
			try {
				
				SOAPMessage msg = smc.getMessage();
				Document xmlDocument = SOAPMessageToDOMDocument(msg);
				
				
				Node bodyNode = xmlDocument.getDocumentElement().getLastChild().getFirstChild().getFirstChild();
				for (Node node = bodyNode; node != null; node = node.getNextSibling()) {
					if (node.getNodeName().equals("creditCardNr")) {
						
						if(priv==null) {
						 priv = CryptoUtil.getPrivateKeyFromKeyStoreResource("A50_Mediator.jks","fjJay9Ii".toCharArray(), "a50_Mediator", "fjJay9Ii".toCharArray());
						}
						
						plainText = node.getFirstChild().getTextContent();
						
						byte[] by = parseBase64Binary(plainText);
						
						byte[] decipheredBytes = CryptoUtil.asymDecipher(by, priv);
						String str = new String(decipheredBytes, "UTF-8");
						
						node.getFirstChild().setTextContent(str);
						
					}
				}

				} catch (Exception e) {
					throw new RuntimeException(e);
				}


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

