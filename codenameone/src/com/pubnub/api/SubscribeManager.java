package com.pubnub.api;

class SubscribeManager extends AbstractSubscribeManager {
    public SubscribeManager(String name, int connectionTimeout,
                            int requestTimeout, boolean daemonThreads) {
        super(name, connectionTimeout, requestTimeout, daemonThreads);
    }
    public void clearRequestQueue() {
        _waiting.clear();
    }
}
