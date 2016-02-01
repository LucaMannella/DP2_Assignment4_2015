package it.polito.dp2.WF.sol4.server;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import java.io.File;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class ValidationHandler implements SOAPHandler<SOAPMessageContext> {
	protected String schemaLocation = "build/META-INF/Workflow.xsd";
	protected String jaxbPackage = "it.polito.dp2.WF.sol4.gen";

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		// Is this an inbound message, i.e., a request?
		Boolean isOutbound = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		// Validate the SOAP only if it's inbound
	    if (!isOutbound) {
			SOAPMessage msg = context.getMessage();
			if (msg==null) {
				return false; // stop message processing
			}
		    try {
				SOAPBody body = msg.getSOAPBody();
	
				// Ensure that the SOAP message has a body.
				if (body == null) {
				    generateSOAPFault(msg, "No message body.");
				    return false;
				}
				//InputStream schemaStream = ValidationHandler.class.getResourceAsStream(schemaLocation);
				JAXBContext jc = JAXBContext.newInstance(jaxbPackage);
	            Unmarshaller u = jc.createUnmarshaller();
				
	            SchemaFactory sf = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
	            try {
	            	File file = new File(schemaLocation);
	            	Schema schema = sf.newSchema(file);
	                //Schema schema = sf.newSchema(new StreamSource(schemaStream));
	                u.setSchema(schema);
	            } catch (org.xml.sax.SAXException se) {
	            	String absolute = getClass().getProtectionDomain().getCodeSource().getLocation().toExternalForm();
	            	absolute = absolute.substring(0, absolute.length() - 1);
	            	absolute = absolute.substring(0, absolute.lastIndexOf("/") + 1);
	            	System.err.println(absolute);
	            	System.err.println("Unable to validate due to internal schema error.");
	            	generateSOAPFault(msg, "Unable to validate due to internal schema error.");
	            	return false;
	            }

	            if( body.getChildNodes().getLength() > 1 ) {
	            	generateSOAPFault(msg, "Invalid input message body: too many child nodes.");
			    	return false;
	            }
	            u.unmarshal(body.getFirstChild());
	            //TODO: we can improve the handler adding controls on the received messages
	            
	            System.out.println("Message ok!");
		    }
		    catch(SOAPException e) { return false; }
		    catch( UnmarshalException ue ) { 
		    	generateSOAPFault(msg, "Invalid input message body.");
		    	return false;
		    }
		    catch (JAXBException e) { 
		    	generateSOAPFault(msg, "Unable to validate input message body."); 
		    	return false;
		    }
		}
	    
	    return true; // continue down the chain	
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	@Override
	public void close(MessageContext context) {
		
	}

	@Override
	public Set<QName> getHeaders() {
		return null;
	}
	
    private void generateSOAPFault(SOAPMessage msg, String reason) {
    	try {
    	    SOAPBody body = msg.getSOAPBody();
    	    body.removeContents();
    	    SOAPFault fault = body.addFault();
    	    QName fault_name = new QName(SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE, "Client");
    	    fault.setFaultCode(fault_name);
    	    fault.setFaultString(reason);
    	}
    	catch(SOAPException e) { }
    }

}
