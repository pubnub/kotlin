package com.pubnub.test

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.PNConfigurationOverride
import com.pubnub.api.v2.callbacks.Result
import java.util.function.Consumer

actual fun <T> createEndpoint(action: () -> T): Endpoint<T> {
    return object : Endpoint<T> {
        override fun overrideConfiguration(action: PNConfigurationOverride.Builder.() -> Unit): Endpoint<T> {
            TODO("Not yet implemented")
        }

        override fun overrideConfiguration(configuration: PNConfiguration): Endpoint<T> {
            TODO("Not yet implemented")
        }

        override fun operationType(): PNOperationType {
            TODO("Not yet implemented")
        }

        override fun sync(): T {
            TODO("Not yet implemented")
        }

        override fun retry() {
            TODO("Not yet implemented")
        }

        override fun silentCancel() {
            TODO("Not yet implemented")
        }

        override fun async(callback: Consumer<Result<T>>) {
            try {
                callback.accept(Result.success(action()))
            } catch (e: PubNubException) {
                callback.accept(Result.failure(e))
            }
        }
    }
}