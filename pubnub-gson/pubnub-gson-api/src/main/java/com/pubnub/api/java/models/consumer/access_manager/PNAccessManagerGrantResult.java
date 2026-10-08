package com.pubnub.api.java.models.consumer.access_manager;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
@ToString
@Data
public class PNAccessManagerGrantResult {

    private String level;
    private int ttl;
    private String subscribeKey;

    private Map<String, Map<String, PNAccessManagerKeyData>> channels;

    private Map<String, Map<String, PNAccessManagerKeyData>> channelGroups;

    private Map<String, Map<String, PNAccessManagerKeyData>> uuids;

    /**
     * Category-level access rights, keyed {@code channels} / {@code uuids}. Empty unless {@code getAllChannels} or
     * {@code getAllUUIDs} was requested.
     */
    private Map<String, Map<String, PNAccessManagerKeyData>> categories;

    public static PNAccessManagerGrantResult from(com.pubnub.api.models.consumer.access_manager.PNAccessManagerGrantResult data) {

        return PNAccessManagerGrantResult.builder()
                .level(data.getLevel())
                .ttl(data.getTtl())
                .subscribeKey(data.getSubscribeKey())
                .channels(from(data.getChannels()))
                .channelGroups(from(data.getChannelGroups()))
                .uuids(from(data.getUuids()))
                .categories(from(data.getCategories()))
                .build();
    }

    private static Map<String, Map<String, PNAccessManagerKeyData>> from(Map<String, Map<String, com.pubnub.api.models.consumer.access_manager.PNAccessManagerKeyData>> data) {
        Map<String, Map<String, PNAccessManagerKeyData>> newMap = new HashMap<>(data.size());
        for (Map.Entry<String, Map<String, com.pubnub.api.models.consumer.access_manager.PNAccessManagerKeyData>> stringMapEntry : data.entrySet()) {
            if (stringMapEntry.getValue() == null) {
                continue;
            }
            Map<String, PNAccessManagerKeyData> innerMap = new HashMap<>(stringMapEntry.getValue().size());
            for (Map.Entry<String, com.pubnub.api.models.consumer.access_manager.PNAccessManagerKeyData> innerEntry : stringMapEntry.getValue().entrySet()) {
                if (innerEntry.getValue() == null) {
                    continue;
                }
                innerMap.put(innerEntry.getKey(), PNAccessManagerKeyData.from(innerEntry.getValue()));
            }
            newMap.put(stringMapEntry.getKey(), innerMap);
        }
        return newMap;
    }
}
