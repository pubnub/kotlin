package com.pubnub.api.builder.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PresenceOperation {

    private List<String> channels;
    private List<String> channelGroups;
    private boolean connected;

}
