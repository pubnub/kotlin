package com.pubnub.api

expect interface Endpoint<OUTPUT> {
    fun async(action: (Result<OUTPUT>) -> Unit)
}