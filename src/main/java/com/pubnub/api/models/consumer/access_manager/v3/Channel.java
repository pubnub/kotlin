package com.pubnub.api.models.consumer.access_manager.v3;

public class Channel extends PNResource<Channel> {

    private Channel() {

    }

    public static Channel name(String channelName) {
        Channel channel = new Channel();
        channel.resourceName = channelName;
        return channel;
    }

    public static Channel pattern(String channelPattern) {
        Channel channel = new Channel();
        channel.resourcePattern = channelPattern;
        return channel;
    }

    @Override
    public Channel read() {
        return super.read();
    }

    @Override
    public Channel delete() {
        return super.delete();
    }

    @Override
    public Channel write() {
        return super.write();
    }
}
