package com.pubnub.api.models.consumer.presence;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class PNWhereNowResult {
    private List<String> channels;
}
