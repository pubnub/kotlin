package com.pubnub.api.models.consumer.access_manager;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Builder
@Getter
@ToString
public class PNAccessManagerGrantResult {

    private String level;
    private int ttl;
    private String subscribeKey;

    private Map<String, Map<String, PNAccessManagerKeyData>> channels;

    private Map<String, Map<String, PNAccessManagerKeyData>> channelGroups;

}
