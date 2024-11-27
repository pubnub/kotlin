package com.pubnub.kmp

actual class CustomObject(val value: Map<String, Any?> = emptyMap()) : Map<String, Any?> by value
