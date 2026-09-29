package com.pubnub.api.java.models.consumer.access_manager.v3;

/**
 * A PAM v3 grant on a <b>channel</b>: pub/sub ({@code read}/{@code write}), presence, channel management
 * ({@code manage}) and App Context v2 channel metadata / members / memberships ({@code get}/{@code update}/
 * {@code delete}/{@code join}).
 *
 * <p>For DataSync channels use {@link DataSyncGrant#channel(String)} (REST CRUD, optional projection) and
 * {@link DataSyncGrant#subscribe(String)} (realtime subscribe on the resolved ref-channel) instead.
 */
public class ChannelGrant extends PNResource<ChannelGrant> implements TokenGrant {

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
