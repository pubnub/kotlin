package com.pubnub.api.java.models.consumer.access_manager.v3;

public class ChannelGrant extends PNResource<ChannelGrant> implements TokenGrant {

    private String projection;

    private ChannelGrant() {

    }

    /**
     * The DataSync projection the token holder looks through when reading this channel's DataSync schema, or
     * {@code null} for the implicit {@code __default__} projection. It applies only to DataSync reads through this
     * channel's schema and is ignored by the pub/sub / Presence / App Context bits carried on the same grant.
     */
    public String getProjection() {
        return projection;
    }

    /**
     * Sets the DataSync projection the token holder looks through for this channel. Fluent; returns {@code this}.
     */
    public ChannelGrant projection(String projection) {
        this.projection = projection;
        return this;
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
    public ChannelGrant create() {
        return super.create();
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
