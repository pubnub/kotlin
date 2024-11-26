package com.pubnub.kmp

actual fun createCustomObject(map: Map<String, Any?>): CustomObject {
    return CustomObject(map)
}
