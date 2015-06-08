package sk.lkcm.organizer.server.service;

import javax.servlet.http.HttpServletRequest;

/*
 * Interface which every GWT service in this application should implement
 * in order to enabled hooks for various stages of dispatching of RPC.
 */
public interface GwtService {

    void prepareForRpcCall(HttpServletRequest request);
    void rpcCallDone();
}
