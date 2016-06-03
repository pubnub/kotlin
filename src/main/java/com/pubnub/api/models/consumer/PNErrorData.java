package com.pubnub.api.models.consumer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PNErrorData {

    private String information;
    private Exception throwable;

}
