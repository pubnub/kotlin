package com.pubnub.kmp

import platform.Foundation.NSInputStream

actual abstract class Downloadable

class DownloadableImpl(
    private val inputStream: NSInputStream
) : Downloadable()
