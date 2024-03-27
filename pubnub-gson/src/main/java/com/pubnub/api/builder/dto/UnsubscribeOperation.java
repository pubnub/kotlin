package com.pubnub.api.builder.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UnsubscribeOperation implements PubSubOperation {
    private final List<String> channels;
    private final List<String> channelGroups;
}
