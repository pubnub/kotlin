package com.pubnub.api.endpoints.remoteaction

import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus

class MappingRemoteAction<T, U> private constructor(
    private val result: T,
    private val operationType: PNOperationType,
    private val function: (T) -> U
) : ExtendedRemoteAction<U> {
    private lateinit var cachedCallback: (result: U?, status: PNStatus) -> Unit

    @Throws(PubNubException::class)
    override fun sync(): U {
        return function.invoke(result)
    }

    override fun async(callback: (result: U?, status: PNStatus) -> Unit) {
        cachedCallback = callback
        callback(
            function.invoke(result),
            PNStatus(
                category = PNStatusCategory.PNAcknowledgmentCategory,
                error = false,
                operation = operationType
            )
        )
    }

    override fun retry() {
        async(cachedCallback)
    }

    override fun silentCancel() {}

    override fun operationType(): PNOperationType {
        return operationType
    }

    companion object {
        fun <T, U> map(result: T, operationType: PNOperationType, function: (T) -> U): ExtendedRemoteAction<U> {
            return MappingRemoteAction(result, operationType, function)
        }
    }
}
