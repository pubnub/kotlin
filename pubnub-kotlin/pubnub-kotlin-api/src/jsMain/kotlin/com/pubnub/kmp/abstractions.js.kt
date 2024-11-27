package com.pubnub.kmp

actual abstract class Uploadable(val fileInput: Any)

class UploadableImpl(fileInput: Any) : Uploadable(fileInput)
