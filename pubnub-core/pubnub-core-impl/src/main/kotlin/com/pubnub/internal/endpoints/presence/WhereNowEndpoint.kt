package com.pubnub.internal.endpoints.presence

import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubCore
import com.pubnub.internal.models.consumer.presence.PNWhereNowResult
import com.pubnub.internal.models.server.Envelope
import com.pubnub.internal.models.server.presence.WhereNowPayload
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubCore.whereNow]
 */
class WhereNowEndpoint internal constructor(
    pubnub: PubNubCore,
    override val uuid: String = pubnub.configuration.userId.value,
) : EndpointCore<Envelope<WhereNowPayload>, PNWhereNowResult>(pubnub), WhereNowInterface {
    override fun doWork(queryParams: HashMap<String, String>): Call<Envelope<WhereNowPayload>> {
        return pubnub.retrofitManager.presenceService.whereNow(
            pubnub.configuration.subscribeKey,
            uuid,
            queryParams,
        )
    }

    override fun createResponse(input: Response<Envelope<WhereNowPayload>>): PNWhereNowResult =
        PNWhereNowResult(channels = input.body()!!.payload!!.channels)

    override fun operationType() = PNOperationType.PNWhereNowOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.PRESENCE
}
