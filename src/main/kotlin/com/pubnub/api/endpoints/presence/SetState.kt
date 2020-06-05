package com.pubnub.api.endpoints.presence

import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.pubnub.api.*
import com.pubnub.api.builder.StateOperation
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.presence.PNSetStateResult
import com.pubnub.api.models.server.Envelope
import retrofit2.Call
import retrofit2.Response
import java.util.*

class SetState(pubnub: PubNub) : Endpoint<Envelope<JsonElement>, PNSetStateResult>(pubnub) {

    var channels = emptyList<String>()
    var channelGroups = emptyList<String>()
    var uuid = pubnub.configuration.uuid
    lateinit var state: Any

    override fun getAffectedChannels() = channels
    override fun getAffectedChannelGroups() = channelGroups

    override fun validateParams() {
        super.validateParams()
        if (channels.isNullOrEmpty() && channelGroups.isNullOrEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_AND_GROUP_MISSING)
        }
        if (!::state.isInitialized) {
            throw PubNubException(PubNubError.STATE_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Envelope<JsonElement>> {
        if (uuid == pubnub.configuration.uuid) {
            pubnub.subscriptionManager.adaptStateBuilder(
                StateOperation(
                    state = state
                ).apply {
                    this.channels = this@SetState.channels
                    this.channelGroups = this@SetState.channelGroups
                }
            )
        }

        if (channelGroups.isNotEmpty()) {
            queryParams["channel-group"] = channelGroups.toCsv()
        }
        queryParams["state"] = pubnub.mapper.toJson(state)

        return pubnub.retrofitManager.presenceService.setState(
            pubnub.configuration.subscribeKey,
            channels.toCsv(),
            uuid,
            queryParams
        )
    }

    override fun createResponse(input: Response<Envelope<JsonElement>>): PNSetStateResult? {
        if (input.body()!!.payload!! is JsonNull) {
            throw PubNubException(PubNubError.PARSING_ERROR)
        }
        return PNSetStateResult(input.body()!!.payload!!)
    }

    override fun operationType() = PNOperationType.PNSetStateOperation
}



