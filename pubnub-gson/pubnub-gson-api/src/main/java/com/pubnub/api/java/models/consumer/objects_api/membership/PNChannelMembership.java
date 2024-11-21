package com.pubnub.api.java.models.consumer.objects_api.membership;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public abstract class PNChannelMembership {
    @AllArgsConstructor
    @EqualsAndHashCode
    @Getter
    public static class ChannelId {
        private String id;
    }

    @Getter
    private final ChannelId channel;
    @Getter
    private final Map<String, Object> custom;
    @Getter
    private final String status;
    @Getter
    private final String type;

    @Deprecated
    public static PNChannelMembership channel(final String channelId) {
        return new JustChannel(new ChannelId(channelId));
    }

    @Deprecated
    public static PNChannelMembership channelWithCustom(final String channelId, final Map<String, Object> custom) {
        return new ChannelWithCustom(new ChannelId(channelId), new HashMap<>(custom), null, null);
    }

    // PNChannelMembership.PNChannelMembership(membership01ChannelId).type("t").status("s").custom(custom).build()
    // add static import to get PNChannelMembership(membership01ChannelId).type("t").status("s").custom(custom).build()
    @SuppressWarnings("checkstyle:MethodName")
    public static Builder PNChannelMembership(final String channelId) {
        return new Builder(new ChannelId(channelId));
    }

    public static class Builder {
        private final ChannelId channelId;
        private Map<String, Object> custom = new HashMap<>();
        private String status;
        private String type;

        private Builder(ChannelId channelId) {
            this.channelId = channelId;
        }

        public Builder custom(Map<String, Object> custom) {
            this.custom = new HashMap<>(custom);
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
            if (custom.isEmpty() && status == null && type == null) {
                return new JustChannel(channelId);
            } else {
                return new ChannelWithCustom(channelId, custom, status, type);
            }
        }
    }

    @Getter
    @EqualsAndHashCode(callSuper = true)
    public static class JustChannel extends PNChannelMembership {
        JustChannel(@NonNull final ChannelId channelId) {
            super(channelId, null, null, null);
        }
    }

    @Getter
    @EqualsAndHashCode(callSuper = true)
    public static class ChannelWithCustom extends PNChannelMembership {
        ChannelWithCustom(@NonNull final ChannelId channelId,
                          @NonNull Map<String, Object> custom,
                          String status,
                          String type) {
            super(channelId, custom, status, type);
        }
    }
}
