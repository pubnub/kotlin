package com.pubnub.api.endpoints.presence

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.whereNowWithUuid
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.presence.PNWhereNowResult
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.whereNow]
 */
actual interface WhereNow : PNFuture<PNWhereNowResult> {
}

@OptIn(ExperimentalForeignApi::class)
class WhereNowImpl(
    private val pubnub: PubNubObjC,
    private val uuid: String
): WhereNow {
    override fun async(callback: Consumer<Result<PNWhereNowResult>>) {
        pubnub.whereNowWithUuid(
            uuid = uuid,
            onSuccess = callback.onSuccessHandler { PNWhereNowResult(it?.filterIsInstance<String>() ?: emptyList()) },
            onFailure = callback.onFailureHandler()
        )
    }
}