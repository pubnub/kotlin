package com.pubnub.core

interface CoreError {
    // https://youtrack.jetbrains.com/issue/KT-31420
    @Suppress("INAPPLICABLE_JVM_NAME")
    @get:JvmName("getErrorCode")
    val code: Int
    val message: String
}