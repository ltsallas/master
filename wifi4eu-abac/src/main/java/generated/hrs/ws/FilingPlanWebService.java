
package generated.hrs.ws;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * The FilingPlan web service provides search operations for the filing plan : files and headings.
 * 
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.8
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "FilingPlanWebService", targetNamespace = "http://ec.europa.eu/sg/hrs", wsdlLocation = "file:/C:/Projects/OP6/SRC/wifi4ever/app/wifi4eu-abac/src/main/resources/integration/HRS_1.28/FilingPlanWebServicePS_1.wsdl")
public class FilingPlanWebService
    extends Service
{

    private final static URL FILINGPLANWEBSERVICE_WSDL_LOCATION;
    private final static WebServiceException FILINGPLANWEBSERVICE_EXCEPTION;
    private final static QName FILINGPLANWEBSERVICE_QNAME = new QName("http://ec.europa.eu/sg/hrs", "FilingPlanWebService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("file:/C:/Projects/OP6/SRC/wifi4ever/app/wifi4eu-abac/src/main/resources/integration/HRS_1.28/FilingPlanWebServicePS_1.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        FILINGPLANWEBSERVICE_WSDL_LOCATION = url;
        FILINGPLANWEBSERVICE_EXCEPTION = e;
    }

    public FilingPlanWebService() {
        super(__getWsdlLocation(), FILINGPLANWEBSERVICE_QNAME);
    }

    public FilingPlanWebService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    /**
     * 
     * @return
     *     returns FilingPlanService
     */
    @WebEndpoint(name = "FilingPlanService")
    public FilingPlanService getFilingPlanService() {
        return super.getPort(new QName("http://ec.europa.eu/sg/hrs", "FilingPlanService"), FilingPlanService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns FilingPlanService
     */
    @WebEndpoint(name = "FilingPlanService")
    public FilingPlanService getFilingPlanService(WebServiceFeature... features) {
        return super.getPort(new QName("http://ec.europa.eu/sg/hrs", "FilingPlanService"), FilingPlanService.class, features);
    }

    private static URL __getWsdlLocation() {
        if (FILINGPLANWEBSERVICE_EXCEPTION!= null) {
            throw FILINGPLANWEBSERVICE_EXCEPTION;
        }
        return FILINGPLANWEBSERVICE_WSDL_LOCATION;
    }

}
