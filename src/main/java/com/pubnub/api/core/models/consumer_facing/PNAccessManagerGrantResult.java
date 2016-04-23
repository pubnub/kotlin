package com.pubnub.api.core.models.consumer_facing;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class PNAccessManagerGrantResult {

    String level;
    int ttl;
    String subscribeKey;

    Map<String, Map<String,PNAccessManagerKeyData>> channels;

    Map<String, Map<String,PNAccessManagerKeyData>> channelGroups;

}
