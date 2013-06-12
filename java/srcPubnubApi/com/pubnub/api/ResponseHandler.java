package com.pubnub.api;

/**
 * @author PubnubCore
 */

abstract class ResponseHandler {
    public abstract void handleResponse(HttpRequest hreq, String response);

    public abstract void handleError(HttpRequest hreq, PubnubError error);

    public void handleTimeout(HttpRequest hreq) {
    }
    public void handleBackFromDar(HttpRequest hreq) {
    }
}
