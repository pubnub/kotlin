package com.pubnub.api.models.consumer.access_manager.v3;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class PNGrantTokenResult {

    private String token;
}
