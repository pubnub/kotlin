package com.pubnub.core

interface RemoteAction<Output, Status : PNStatus> {
    fun async(callback: (result: Output?, status: Status) -> Unit)
    fun silentCancel()
}