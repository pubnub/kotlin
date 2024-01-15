package com.pubnub.api.models.consumer.access_manager.v3;

public class ChannelGroupGrant extends PNResource<ChannelGroupGrant> {

    private ChannelGroupGrant() {
    }

    public static ChannelGroupGrant id(String groupName) {
        ChannelGroupGrant channelGroupGrant = new ChannelGroupGrant();
        channelGroupGrant.resourceName = groupName;
        return channelGroupGrant;
    }

    public static ChannelGroupGrant pattern(String groupPattern) {
        ChannelGroupGrant channelGroupGrant = new ChannelGroupGrant();
        channelGroupGrant.resourcePattern = groupPattern;
        return channelGroupGrant;
    }

    @Override
    public ChannelGroupGrant read() {
        return super.read();
    }

    @Override
    public ChannelGroupGrant manage() {
        return super.manage();
    }
}
