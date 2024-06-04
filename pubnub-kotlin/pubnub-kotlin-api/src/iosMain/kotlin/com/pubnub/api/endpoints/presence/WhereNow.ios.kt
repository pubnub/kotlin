package com.pubnub.api.endpoints.presence

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.whereNowWithUuid
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.presence.PNWhereNowResult
import com.pubnub.api.onFailureHandler
import com.pubnub.api.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.whereNow]
 */
actual interface WhereNow : Endpoint<PNWhereNowResult> {
}

@OptIn(ExperimentalForeignApi::class)
class WhereNowImpl(
    public val pubnub: PubNubObjC,
    public val uuid: String
): WhereNow {
    override fun async(callback: Consumer<Result<PNWhereNowResult>>) {
        pubnub.whereNowWithUuid(
            uuid = uuid,
            onSuccess = callback.onSuccessHandler { PNWhereNowResult(it as? List<String> ?: emptyList()) },
            onFailure = callback.onFailureHandler()
        )
    }
}