package com.pubnub.api.core.builder.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UnsubscribeOperation {

    private List<String> channels;
    private List<String> channelGroups;
}
