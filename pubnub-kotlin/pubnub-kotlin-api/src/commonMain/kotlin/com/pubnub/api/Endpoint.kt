package com.pubnub.api

expect interface Endpoint<OUTPUT>

expect fun <T> Endpoint<T>.async(action: (Result<T>) -> Unit)