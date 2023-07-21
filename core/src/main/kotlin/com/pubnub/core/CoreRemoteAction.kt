@file:JvmMultifileClass
package com.pubnub.core

interface CoreRemoteAction<O, S : Status> {

    fun async(callback: (result: O?, status: S) -> Unit)

    fun silentCancel()
}