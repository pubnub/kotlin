package com.pubnub.api.managers.subscription.utils;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

public abstract class RequestDetails {
    @Data
    @EqualsAndHashCode(callSuper=false)
    public static class SubscribeRequestDetails extends RequestDetails {
        private final String subscribeKey;
        private final String channelCSV;
        private final Map<String, String> options;
    }
}
