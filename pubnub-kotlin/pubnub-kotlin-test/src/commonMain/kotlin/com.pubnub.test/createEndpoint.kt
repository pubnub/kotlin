package com.pubnub.test

import com.pubnub.api.Endpoint

expect fun <T> createEndpoint(action: () -> T): Endpoint<T>