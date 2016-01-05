package com.pubnub.api;

/**
 * @author PubnubCore
 *
 */
class SubscriptionItem {
    String name;
    boolean connected;
    boolean subscribed;
    boolean error;
    Callback callback;

    SubscriptionItem() {
    }

    SubscriptionItem(String name, Callback callback) {
        this.name = name;
        this.callback = callback;
        this.connected = false;
    }

    public String toString() {
        return this.name;
    }
}
