@file:JvmMultifileClass
package com.pubnub.core

interface PNCallback<O, S : Status> {
    fun onResponse(result: O?, status: S)
}

interface CoreRemoteAction<O, S : Status, C : PNCallback<O, S>> {

    fun async(callback: C)

    // https://youtrack.jetbrains.com/issue/KT-31420
    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("__do_not_use_in_java_async")
    fun async(callback: (result: O?, status: S) -> Unit) {
        val a: PNCallback<O, S> = object : PNCallback<O, S> {
            override fun onResponse(result: O?, status: S) {
                callback(result, status)
            }
        }
        async(a)
    }

    fun silentCancel()
}