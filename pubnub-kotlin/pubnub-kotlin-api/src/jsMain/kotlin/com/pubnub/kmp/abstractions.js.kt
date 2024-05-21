package com.pubnub.kmp

actual typealias CustomObject = CustomObjectImpl

class CustomObjectImpl(map: Map<String,Any?> = emptyMap()) : Map<String, Any?> by map