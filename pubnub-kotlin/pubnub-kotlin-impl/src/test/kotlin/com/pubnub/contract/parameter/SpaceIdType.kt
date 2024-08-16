package com.pubnub.contract.parameter

import com.pubnub.api.SpaceId
import io.cucumber.java.ParameterType

@ParameterType("'(.*)'")
fun spaceId(stringId: String): SpaceId {
    return SpaceId(stringId)
}
