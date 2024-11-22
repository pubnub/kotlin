package com.pubnub.api.java.models.consumer.objects_api.membership;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
public class PNChannelMembership {
    @Getter
    private final ChannelId channel;
    @Getter
    private Object custom;
    @Getter
    private String status;
    @Getter
    private String type;

    public static Builder builder(String channelId){
        return new Builder(channelId);
    }

    private PNChannelMembership(Builder builder){
        this.channel = new ChannelId(builder.channelId);
        this.custom = builder.custom;
        this.status = builder.status;
        this.type = builder.type;
    }


    public static class Builder{
        private final String channelId;
        private Object custom;
        private String status;
        private String type;

        public Builder(String channelId) {
            if (channelId == null || channelId.isEmpty()) {
                throw new IllegalArgumentException("channelId cannot be null or empty");
            }
            this.channelId = channelId;
        }

        public Builder custom(Object custom) {
            this.custom = custom;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public PNChannelMembership build() {
            return new PNChannelMembership(this);
        }
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    @Getter
    public static class ChannelId {
        private final String id;
    }

    // Factory method for creating a membership with only a channel ID.
    public static PNChannelMembership channel(final String channelId) {
        return new JustChannel(new ChannelId(channelId));
    }

    // Factory method for creating a membership with a channel ID and custom data.
    public static PNChannelMembership channelWithCustom(final String channelId, final Object custom) {
        return new ChannelWithCustom(new ChannelId(channelId), custom);
    }

    /**
     * Represents a membership with only a channel ID.
     */
    @Getter
    @EqualsAndHashCode(callSuper = true)
    public static class JustChannel extends PNChannelMembership {
        public JustChannel(@NonNull final ChannelId channelId) {
            super(channelId);
        }
    }

    /**
     * Represents a membership with a channel ID and custom data.
     */
    @Getter
    @EqualsAndHashCode(callSuper = true)
    public static class ChannelWithCustom extends PNChannelMembership {
        private final Object custom;

        public ChannelWithCustom(@NonNull final ChannelId channelId, @NonNull Object custom) {
            super(channelId);
            this.custom = custom;
        }
    }
}
