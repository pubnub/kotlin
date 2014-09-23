package com.pubnub.api;

public class Channel extends SubscriptionItem{
    public ChannelGroup group;

    public Channel(String channel, Callback callback, ChannelGroup group) {
        super(channel, callback);

        this.group = group;
    }

    public Channel(String channel, Callback callback) {
        super(channel, callback);
    }
}
