package com.pubnub.api.models.consumer;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class PNPublishResult {
    private Long timetoken;
}
