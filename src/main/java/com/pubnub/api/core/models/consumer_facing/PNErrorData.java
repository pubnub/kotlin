package com.pubnub.api.core.models.consumer_facing;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PNErrorData {

    String information;
    Exception throwable;

}
