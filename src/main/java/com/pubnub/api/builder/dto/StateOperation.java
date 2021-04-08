package com.pubnub.api.builder.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Builder
@Data
public class StateOperation implements PubSubOperation {
    @Builder.Default private final List<String> channels = Collections.emptyList();
    @Builder.Default private final List<String> channelGroups = Collections.emptyList();
    private final Object state;
}
