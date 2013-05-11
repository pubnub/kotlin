package com.pubnub.api;

/**
 * @author PubnubCore
 *
 */
class Channel {
    String name;
    boolean connected;
    boolean subscribed;
    boolean error;
    Callback callback;
}
