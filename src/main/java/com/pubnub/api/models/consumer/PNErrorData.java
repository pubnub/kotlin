package com.pubnub.api.models.consumer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PNErrorData {

    private String information;
    private Exception throwable;

}
