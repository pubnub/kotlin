package com.pubnub.api;

class NonSubscribeManager extends AbstractNonSubscribeManager {

    public NonSubscribeManager(String name, int connectionTimeout,
                               int requestTimeout, boolean daemonThreads) {
        super(name, connectionTimeout, requestTimeout, daemonThreads);
    }

    public void clearRequestQueue() {
        _waiting.removeAllElements();
    }
}
