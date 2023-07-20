package com.pubnub.core

interface CoreRemoteAction<Output, Status : com.pubnub.core.Status> {
    fun async(callback: (result: Output?, status: Status) -> Unit)
    fun silentCancel()
}