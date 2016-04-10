package com.pubnub.api.core.models.consumer_facing;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PNMessageResult extends PNResult {
    private PNMessageData data;
}
