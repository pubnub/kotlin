@file:JvmMultifileClass
package com.pubnub.core

interface CoreRemoteAction<O, S : Status> {

    // https://youtrack.jetbrains.com/issue/KT-31420
    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("__do_not_use_in_java")
    fun async(callback: (result: O?, status: S) -> Unit)

    fun silentCancel()
}