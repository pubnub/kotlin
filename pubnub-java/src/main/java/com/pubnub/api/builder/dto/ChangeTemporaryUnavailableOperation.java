package com.pubnub.api.builder.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Builder
@Data
public class ChangeTemporaryUnavailableOperation implements PubSubOperation {
    @Singular
    private final List<String> unavailableChannels;
    @Singular
    private final List<String> unavailableChannelGroups;
    @Singular
    private final List<String> availableChannels;
    @Singular
    private final List<String> availableChannelGroups;
}
