package com.pubnub.api.models.consumer.channel_group;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class PNChannelGroupsAllChannelsResult {
    private List<String> channels;
}
