package com.pubnub.api.endpoints.presence

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.presence.PNWhereNowResult
import com.pubnub.api.models.server.Envelope
import com.pubnub.api.models.server.presence.WhereNowPayload
import retrofit2.Call
import retrofit2.Response
import java.util.HashMap

interface WhereNow : RemoteAction<PNWhereNowResult>

/**
 * @see [PubNub.whereNow]
 */
class WhereNowImpl(
    pubnub: PubNub,
    val uuid: String = pubnub.configuration.uuid
) : Endpoint<Envelope<WhereNowPayload>, PNWhereNowResult>(pubnub), WhereNow {

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
}
