package com.pubnub.kmp

actual abstract class Downloadable(val pubnubFile: Any)

class DownloadableImpl(pubnubFile: Any) : Downloadable(pubnubFile)