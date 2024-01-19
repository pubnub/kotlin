package com.pubnub.contract.access.parameter

import io.cucumber.java.ParameterType

@ParameterType(".*")
fun ttl(ttl: String): Long {
    return ttl.toLong()
}
