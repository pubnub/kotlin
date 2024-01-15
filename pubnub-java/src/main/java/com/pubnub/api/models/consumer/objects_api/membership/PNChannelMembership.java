package com.pubnub.api.models.consumer.objects_api.membership;

import com.google.gson.annotations.JsonAdapter;
import com.pubnub.api.models.consumer.objects_api.util.CustomPayloadJsonInterceptor;
import lombok.*;

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

    public static PNChannelMembership channel(final String channelId) {
        return new JustChannel(new ChannelId(channelId));
    }

    public static PNChannelMembership channelWithCustom(final String channelId, final Map<String, Object> custom) {
        return new ChannelWithCustom(new ChannelId(channelId), new HashMap<>(custom));
    }

    @Getter
    @EqualsAndHashCode(callSuper = true)
    public static class JustChannel extends PNChannelMembership {
        JustChannel(@NonNull final ChannelId channelId) {
            super(channelId);
        }
    }

    @Getter
    @EqualsAndHashCode(callSuper = true)
    public static class ChannelWithCustom extends PNChannelMembership {
        @JsonAdapter(CustomPayloadJsonInterceptor.class)
        private final Object custom;

        ChannelWithCustom(@NonNull final ChannelId channelId, @NonNull Object custom) {
            super(channelId);
            this.custom = custom;
        }
    }
}
