package com.pubnub.api.endpoints.presence

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.presence.PNWhereNowResult
import com.pubnub.api.models.server.Envelope
import com.pubnub.api.models.server.presence.WhereNowPayload
import com.pubnub.api.throwIfEmpty
import retrofit2.Call
import retrofit2.Response
import java.util.HashMap

class WhereNow(pubnub: PubNub) : Endpoint<Envelope<WhereNowPayload>, PNWhereNowResult>(pubnub) {

    var uuid = pubnub.configuration.uuid

    override fun getAffectedChannels() = emptyList<String>()
    override fun getAffectedChannelGroups() = emptyList<String>()

    override fun doWork(queryParams: HashMap<String, String>): Call<Envelope<WhereNowPayload>> {
        return pubnub.retrofitManager.presenceService.whereNow(
            pubnub.configuration.subscribeKey,
            uuid,
            queryParams
        )
    }

    override fun createResponse(input: Response<Envelope<WhereNowPayload>>): PNWhereNowResult? {
        input.throwIfEmpty()
        return PNWhereNowResult(input.body()!!.payload!!.channels)
    }

    override fun operationType() = PNOperationType.PNWhereNowOperation

    override fun isSubKeyRequired() = true
    override fun isPubKeyRequired() = false
    override fun isAuthRequired() = true
}