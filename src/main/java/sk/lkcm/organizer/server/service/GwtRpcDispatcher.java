package sk.lkcm.organizer.server.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.UrlPathHelper;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Custom RPC dispatcher. Dispatches the GWT remote procedure calls to respective
 * GWT RPC services defined in a map.
 *
 */
@SuppressWarnings("serial")
public class GwtRpcDispatcher extends RemoteServiceServlet {

    private static final String SERVICE_URL_MAPPER = "gwtServiceUrlMap";
    private static final UrlPathHelper PATH_HELPER = new UrlPathHelper();

    private final static Logger logger = LoggerFactory
            .getLogger(GwtRpcDispatcher.class);
    private WebApplicationContext applicationContext;
    private Map<String, RemoteService> gwtServicesUrlMapping = new HashMap<String, RemoteService>();

    public HttpServletRequest getLocalRequest() {
        return perThreadRequest.get();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init() {
        logger.info("Initializing " + GwtRpcDispatcher.class.getName()
                + " servlet");

        applicationContext = WebApplicationContextUtils
                .getWebApplicationContext(getServletContext());
        if (applicationContext == null)
            throw new IllegalStateException(
                    "No Spring web application context found");

        // Get map of gwt url services
        String beanName = getInitParameter(SERVICE_URL_MAPPER);
        if (beanName == null || beanName.isEmpty())
            throw new IllegalArgumentException("The servlet "
                    + GwtRpcDispatcher.class.getSimpleName()
                    + " should have a '" + SERVICE_URL_MAPPER
                    + "' parameter defined");
        gwtServicesUrlMapping = (Map<String, RemoteService>) applicationContext
                .getBean(beanName);
    }

    @Override
    public String processCall(String payload) throws SerializationException {
        logger.info("Processing call " + payload + " thread id "
                + Thread.currentThread().getId());

        try {
            RemoteService service = retrieveSpringBean(getThreadLocalRequest());
            RPCRequest rpcRequest = RPC.decodeRequest(payload,
                    service.getClass(), this);
            onAfterRequestDeserialized(rpcRequest);

            // Pass request if its instance of GwtService
            if (service instanceof GwtService) {
                HttpServletRequest request = getThreadLocalRequest();
                ((GwtService) service).prepareForRpcCall(request);
            }

            String response = null;
            try {
                response = RPC.invokeAndEncodeResponse(service,
                        rpcRequest.getMethod(), rpcRequest.getParameters(),
                        rpcRequest.getSerializationPolicy());
                // Catch all and log all throwables before propagating
                // it to the client.
            } catch (Throwable t) {
                logger.error("Error during rpc call:", t);
                throw t;
            } finally {
                if (service instanceof GwtService)
                    ((GwtService) service).rpcCallDone();
            }

            return response;

        } catch (IncompatibleRemoteServiceException ex) {
            return RPC.encodeResponseForFailure(null, ex);
        }
    }

    private RemoteService retrieveSpringBean(HttpServletRequest request) {

        String serviceUrl = PATH_HELPER.getPathWithinServletMapping(request);

        RemoteService bean = gwtServicesUrlMapping.get(serviceUrl);
        if (bean == null)
            throw new IllegalArgumentException("No service mapped to url "
                    + serviceUrl);

        return bean;
    }

}