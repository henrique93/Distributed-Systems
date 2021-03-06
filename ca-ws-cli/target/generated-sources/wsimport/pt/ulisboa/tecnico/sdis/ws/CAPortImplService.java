
package pt.ulisboa.tecnico.sdis.ws;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.10
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "CAPortImplService", targetNamespace = "http://ws.sdis.tecnico.ulisboa.pt/", wsdlLocation = "http://sec.sd.rnl.tecnico.ulisboa.pt:8081/ca?WSDL")
public class CAPortImplService
    extends Service
{

    private final static URL CAPORTIMPLSERVICE_WSDL_LOCATION;
    private final static WebServiceException CAPORTIMPLSERVICE_EXCEPTION;
    private final static QName CAPORTIMPLSERVICE_QNAME = new QName("http://ws.sdis.tecnico.ulisboa.pt/", "CAPortImplService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://sec.sd.rnl.tecnico.ulisboa.pt:8081/ca?WSDL");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        CAPORTIMPLSERVICE_WSDL_LOCATION = url;
        CAPORTIMPLSERVICE_EXCEPTION = e;
    }

    public CAPortImplService() {
        super(__getWsdlLocation(), CAPORTIMPLSERVICE_QNAME);
    }

    public CAPortImplService(WebServiceFeature... features) {
        super(__getWsdlLocation(), CAPORTIMPLSERVICE_QNAME, features);
    }

    public CAPortImplService(URL wsdlLocation) {
        super(wsdlLocation, CAPORTIMPLSERVICE_QNAME);
    }

    public CAPortImplService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, CAPORTIMPLSERVICE_QNAME, features);
    }

    public CAPortImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public CAPortImplService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns CA
     */
    @WebEndpoint(name = "CAPortImplPort")
    public CA getCAPortImplPort() {
        return super.getPort(new QName("http://ws.sdis.tecnico.ulisboa.pt/", "CAPortImplPort"), CA.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CA
     */
    @WebEndpoint(name = "CAPortImplPort")
    public CA getCAPortImplPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://ws.sdis.tecnico.ulisboa.pt/", "CAPortImplPort"), CA.class, features);
    }

    private static URL __getWsdlLocation() {
        if (CAPORTIMPLSERVICE_EXCEPTION!= null) {
            throw CAPORTIMPLSERVICE_EXCEPTION;
        }
        return CAPORTIMPLSERVICE_WSDL_LOCATION;
    }

}
