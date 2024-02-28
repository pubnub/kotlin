package com.pubnub.api.endpoints.objects_api.utils;

public class Include {
    static final String INCLUDE_CHANNEL_PARAM_VALUE = "channel";
    static final String INCLUDE_CHANNEL_CUSTOM_PARAM_VALUE = "channel.custom";
    static final String INCLUDE_UUID_PARAM_VALUE = "uuid";
    static final String INCLUDE_UUID_CUSTOM_PARAM_VALUE = "uuid.custom";

    private Include() {
    }

    public enum PNChannelDetailsLevel {
        CHANNEL(INCLUDE_CHANNEL_PARAM_VALUE),
        CHANNEL_WITH_CUSTOM(INCLUDE_CHANNEL_CUSTOM_PARAM_VALUE);

        private final String paramValue;

        PNChannelDetailsLevel(final String paramValue) {
            this.paramValue = paramValue;
        }
    }

    public enum PNUUIDDetailsLevel {
        UUID(INCLUDE_UUID_PARAM_VALUE),
        UUID_WITH_CUSTOM(INCLUDE_UUID_CUSTOM_PARAM_VALUE);

        private final String paramValue;

        PNUUIDDetailsLevel(final String paramValue) {
            this.paramValue = paramValue;
        }
    }
}
