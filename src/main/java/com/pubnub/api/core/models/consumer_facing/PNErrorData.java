package com.pubnub.api.core.models.consumer_facing;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PNErrorData {

    String information;
    Exception throwable;

}
