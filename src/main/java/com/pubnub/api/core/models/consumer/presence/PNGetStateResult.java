package com.pubnub.api.core.models.consumer.presence;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class PNGetStateResult {

    private Map<String, Object> stateByUUID;

}
