package com.pubnub.api.models.consumer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class PNErrorData {

    private String information;
    private Exception throwable;

}
