package com.pubnub.contract.parameter

import com.google.gson.Gson
import io.cucumber.java.ParameterType

val gson = Gson()

@ParameterType("'(.*)'")
fun customMetadata(customMetadataString: String): Map<String, Any> {
    return gson.fromJson<MutableMap<String, Any>>(customMetadataString, mutableMapOf<String, Any>()::class.java)
}
