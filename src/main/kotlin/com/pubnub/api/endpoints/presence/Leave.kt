package com.pubnub.api.endpoints.presence

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.server.Envelope
import com.pubnub.api.toCsv
import retrofit2.Call
import retrofit2.Response
import java.util.HashMap

internal class Leave(pubnub: PubNub) : Endpoint<Envelope<*>, Boolean>(pubnub) {

    var channels = emptyList<String>()
    var channelGroups = emptyList<String>()

    override fun validateParams() {
        super.validateParams()
        if (channels.isEmpty() && channelGroups.isEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_AND_GROUP_MISSING)
        }
    }

    override fun getAffectedChannels() = channels

    override fun getAffectedChannelGroups() = channelGroups

    override fun doWork(queryParams: HashMap<String, String>): Call<Envelope<*>> {
        queryParams["channel-group"] = channelGroups.toCsv()

        return pubnub.retrofitManager.presenceService.leave(
            pubnub.configuration.subscribeKey,
            channels.toCsv(),
            queryParams
        )
    }

    override fun createResponse(input: Response<Envelope<*>>) = true

    override fun operationType() = PNOperationType.PNUnsubscribeOperation

    override fun isSubKeyRequired() = true
    override fun isPubKeyRequired() = false
    override fun isAuthRequired() = true
}


