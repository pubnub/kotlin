package com.pubnub.api.builder.dto;

import lombok.Data;

@Data
public class TimetokenAndRegionOperation implements PubSubOperation {
    private final long timetoken;
    private final String region;
}
