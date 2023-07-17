package com.pubnub.core

interface RemoteAction<Output> {
    fun async(callback: (result: Output?, status: PNStatus) -> Unit)
    fun silentCancel()
}