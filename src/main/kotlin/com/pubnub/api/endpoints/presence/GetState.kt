package com.pubnub.api.endpoints.presence

import com.google.gson.JsonElement
import com.pubnub.api.*
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.presence.PNGetStateResult
import com.pubnub.api.models.server.Envelope
import retrofit2.Call
import retrofit2.Response
import java.util.*

class GetState(pubnub: PubNub) : Endpoint<Envelope<JsonElement>, PNGetStateResult>(pubnub) {

    var channels = listOf<String>()
    var channelGroups = listOf<String>()
    var uuid = pubnub.configuration.uuid

    override fun getAffectedChannels() = channels

    override fun getAffectedChannelGroups() = channelGroups

    override fun validateParams() {
        super.validateParams()
        if (channels.isNullOrEmpty() && channelGroups.isNullOrEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_AND_GROUP_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Envelope<JsonElement>> {
        if (channelGroups.isNotEmpty()) {
            queryParams["channel-group"] = channelGroups.toCsv()
        }

        return pubnub.retrofitManager.presenceService.getState(
            pubnub.configuration.subscribeKey,
            channels.toCsv(),
            uuid,
            queryParams
        )
    }

    override fun createResponse(input: Response<Envelope<JsonElement>>): PNGetStateResult? {
        val stateMappings = hashMapOf<String, JsonElement>()
        if (channels.size == 1 && channelGroups.isEmpty()) {
            stateMappings[channels.first()] = input.body()!!.payload!!
        } else {
            val it = pubnub.mapper.getObjectIterator(input.body()!!.payload!!)
            while (it.hasNext()) {
                val stateMapping = it.next()
                stateMappings[stateMapping.key] = stateMapping.value
            }
        }

        return PNGetStateResult(stateMappings)
    }

    override fun operationType() = PNOperationType.PNGetState
}



