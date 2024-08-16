package com.pubnub.internal.endpoints.presence

import com.pubnub.api.endpoints.presence.WhereNow
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.presence.PNWhereNowResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.models.server.Envelope
import com.pubnub.internal.models.server.presence.WhereNowPayload
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubImpl.whereNow]
 */
class WhereNowEndpoint internal constructor(
    pubnub: PubNubImpl,
    override val uuid: String = pubnub.configuration.userId.value,
) : EndpointCore<Envelope<WhereNowPayload>, PNWhereNowResult>(pubnub), WhereNow {
    override fun doWork(queryParams: HashMap<String, String>): Call<Envelope<WhereNowPayload>> {
        return retrofitManager.presenceService.whereNow(
            configuration.subscribeKey,
            uuid,
            queryParams,
        )
    }

    override fun createResponse(input: Response<Envelope<WhereNowPayload>>): PNWhereNowResult =
        PNWhereNowResult(channels = input.body()!!.payload!!.channels)

    override fun operationType() = PNOperationType.PNWhereNowOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.PRESENCE
}
