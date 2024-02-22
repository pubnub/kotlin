package com.pubnub.internal.endpoints.presence

import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.Endpoint
import com.pubnub.internal.InternalPubNubClient
import com.pubnub.internal.models.consumer.presence.PNWhereNowResult
import com.pubnub.internal.models.server.Envelope
import com.pubnub.internal.models.server.presence.WhereNowPayload
import retrofit2.Call
import retrofit2.Response

/**
 * @see [InternalPubNubClient.whereNow]
 */
class WhereNow internal constructor(
    pubnub: InternalPubNubClient,
    override val uuid: String = pubnub.configuration.userId.value
) : Endpoint<Envelope<WhereNowPayload>, PNWhereNowResult>(pubnub), IWhereNow {

    override fun doWork(queryParams: HashMap<String, String>): Call<Envelope<WhereNowPayload>> {

        return pubnub.retrofitManager.presenceService.whereNow(
            pubnub.configuration.subscribeKey,
            uuid,
            queryParams
        )
    }

    override fun createResponse(input: Response<Envelope<WhereNowPayload>>): PNWhereNowResult =
        PNWhereNowResult(channels = input.body()!!.payload!!.channels)

    override fun operationType() = PNOperationType.PNWhereNowOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.PRESENCE
}
