package com.pubnub.api.models.consumer.access_manager.v3;

public class ChannelGrant extends PNResource<ChannelGrant> {

    private ChannelGrant() {

    }

    public static ChannelGrant name(String channelName) {
        ChannelGrant channelGrant = new ChannelGrant();
        channelGrant.resourceName = channelName;
        return channelGrant;
    }

    public static ChannelGrant pattern(String channelPattern) {
        ChannelGrant channelGrant = new ChannelGrant();
        channelGrant.resourcePattern = channelPattern;
        return channelGrant;
    }

    @Override
    public ChannelGrant read() {
        return super.read();
    }

    @Override
    public ChannelGrant delete() {
        return super.delete();
    }

    @Override
    public ChannelGrant write() {
        return super.write();
    }

    @Override
    public ChannelGrant get() {
        return super.get();
    }

    @Override
    public ChannelGrant manage() {
        return super.manage();
    }

    @Override
    public ChannelGrant update() {
        return super.update();
    }

    @Override
    public ChannelGrant join() {
        return super.join();
    }


}
