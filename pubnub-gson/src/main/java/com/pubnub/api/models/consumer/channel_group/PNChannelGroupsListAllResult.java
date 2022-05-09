package com.pubnub.api.models.consumer.channel_group;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
public class PNChannelGroupsListAllResult {
    private List<String> groups;
}
