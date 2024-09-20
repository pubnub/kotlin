package com.pubnub.kmp

actual typealias CustomObject = CustomObjectImpl

class CustomObjectImpl(map: Map<String, Any?> = emptyMap()) : Map<String, Any?> by map

actual abstract class Uploadable(val fileInput: Any)

class UploadableImpl(fileInput: Any) : Uploadable(fileInput)
