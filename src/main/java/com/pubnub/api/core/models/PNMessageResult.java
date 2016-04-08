package com.pubnub.api.core.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PNMessageResult extends PNResult {
    private PNMessageData data;
}
