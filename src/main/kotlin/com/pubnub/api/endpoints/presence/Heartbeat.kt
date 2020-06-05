package com.pubnub.api.endpoints.presence

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import retrofit2.Call
import retrofit2.Response
import java.util.*

class Heartbeat internal constructor(
    pubnub: PubNub,
    val channels: List<String> = listOf(),
    val channelGroups: List<String> = listOf()
) : Endpoint<Void, Boolean>(pubnub) {

    override fun getAffectedChannels() = channels
    override fun getAffectedChannelGroups() = channelGroups

    override fun validateParams() {
        super.validateParams()
        if (channels.isEmpty() && channelGroups.isEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_AND_GROUP_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Void> {
        queryParams["heartbeat"] = pubnub.configuration.presenceTimeout.toString()

        if (channelGroups.isNotEmpty()) {
            queryParams["channel-group"] = channelGroups.joinToString(",")
        }

        val channelsCsv =
            if (channels.isNotEmpty())
                channels.joinToString(",")
            else
                ","

        return pubnub.retrofitManager.presenceService.heartbeat(
            pubnub.configuration.subscribeKey,
            channelsCsv,
            queryParams
        )
    }

    override fun createResponse(input: Response<Void>): Boolean? {
        return true
    }

    override fun operationType() = PNOperationType.PNHeartbeatOperation

}