package com.pubnub.api.endpoints.presence

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.server.Envelope
import retrofit2.Call
import retrofit2.Response
import java.util.HashMap

internal class Heartbeat(
    pubnub: PubNub,
    val channels: List<String> = listOf(),
    val channelGroups: List<String> = listOf()
) : Endpoint<Envelope<*>, Boolean>(pubnub) {

    override fun getAffectedChannels() = channels

    override fun getAffectedChannelGroups() = channelGroups

    override fun validateParams() {
        super.validateParams()
        if (channels.isEmpty() && channelGroups.isEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_AND_GROUP_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Envelope<*>> {
        queryParams["heartbeat"] = pubnub.configuration.presenceTimeout.toString()

        if (channelGroups.isNotEmpty()) {
            queryParams["channel-group"] = channelGroups.joinToString(",")
        }

        val channelsCsv =
            if (channels.isNotEmpty())
                channels.joinToString(",")
            else
                ","

        queryParams.putAll(encodeParams(queryParams))

        return pubnub.retrofitManager.presenceService.heartbeat(
            pubnub.configuration.subscribeKey,
            channelsCsv,
            queryParams
        )
    }

    override fun createResponse(input: Response<Envelope<*>>) = true

    override fun operationType() = PNOperationType.PNHeartbeatOperation

    override fun isSubKeyRequired() = true
    override fun isPubKeyRequired() = false
    override fun isAuthRequired() = true


}