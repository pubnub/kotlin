package com.pubnub.core

interface RemoteAction<Output, Status : com.pubnub.core.Status> {
    fun async(callback: (result: Output?, status: Status) -> Unit)
    fun silentCancel()
}