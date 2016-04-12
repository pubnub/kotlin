package com.pubnub.api.core.models.consumer_facing;

import lombok.Data;

import java.util.Map;

@Data
public class PNSetStateResult {

    Map<String, Object> state;

}
