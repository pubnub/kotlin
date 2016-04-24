package com.pubnub.api.core.models.consumer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PNErrorData {

    String information;
    Exception throwable;

}
