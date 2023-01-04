package com.pubnub.contract.parameter

import io.cucumber.java.ParameterType

@ParameterType("'(.*)'")
fun boolean(booleanString: String): Boolean {
    return when {
        booleanString.equals("true", ignoreCase = true) -> true
        booleanString.equals("false", ignoreCase = true) -> false
        else -> throw RuntimeException("Expected true or false, but found: $booleanString")
    }
}
