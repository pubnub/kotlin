package com.pubnub.api.models.consumer.history;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class PNMessageCountResult {

    private Map<String, Long> channels;

}
