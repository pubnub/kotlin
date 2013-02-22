package com.pubnub.api;

/**
 * @author PubnubCore
 */

abstract class ResponseHandler {
    public abstract void handleResponse(String response);

    public abstract void handleError(String response);

    public void handleTimeout() {
    }
}
