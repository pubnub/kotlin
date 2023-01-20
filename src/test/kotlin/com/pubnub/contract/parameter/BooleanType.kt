package com.pubnub.contract.parameter

import io.cucumber.java.ParameterType

@ParameterType("'(.*)'")
fun boolean(booleanString: String): Boolean {
    return booleanString.toBooleanStrict()
}
