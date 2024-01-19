package com.pubnub.internal.endpoints.presence

import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.presence.PNSetStateResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.PubNub
import com.pubnub.internal.builder.StateOperation
import com.pubnub.internal.models.server.Envelope
import com.pubnub.internal.presence.eventengine.data.PresenceData
import com.pubnub.internal.toCsv
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNub.setPresenceState]
 */
class SetState internal constructor(
    pubnub: PubNub,
    override val channels: List<String>,
    override val channelGroups: List<String>,
    override val state: Any,
    override val uuid: String = pubnub.configuration.userId.value,
    private val presenceData: PresenceData
) : com.pubnub.internal.Endpoint<Envelope<JsonElement>, PNSetStateResult>(pubnub), ISetState {

    override fun getAffectedChannels() = channels
    override fun getAffectedChannelGroups() = channelGroups

    override fun validateParams() {
        super.validateParams()
        if (channels.isEmpty() && channelGroups.isEmpty())
            throw PubNubException(PubNubError.CHANNEL_AND_GROUP_MISSING)
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Envelope<JsonElement>> {
        if (uuid == pubnub.configuration.userId.value) {
            val stateCopy = pubnub.mapper.fromJson(pubnub.mapper.toJson(state), JsonElement::class.java)
            if (pubnub.configuration.enableEventEngine) {
                presenceData.channelStates.putAll(channels.associateWith { stateCopy })
            } else {
                pubnub.subscriptionManager.adaptStateBuilder(
                    StateOperation(
                        state = stateCopy,
                        channels = channels,
                        channelGroups = channelGroups
                    )
                )
            }
        }

        addQueryParams(queryParams)

        return pubnub.retrofitManager.presenceService.setState(
            pubnub.configuration.subscribeKey,
            channels.toCsv(),
            uuid,
            queryParams
        )
    }

    override fun createResponse(input: Response<Envelope<JsonElement>>): PNSetStateResult {
        if (input.body()!!.payload!! is JsonNull) {
            throw PubNubException(PubNubError.PARSING_ERROR)
        }
        return PNSetStateResult(state = input.body()!!.payload!!)
    }

    override fun operationType() = PNOperationType.PNSetStateOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.PRESENCE

    private fun addQueryParams(queryParams: MutableMap<String, String>) {
        if (channelGroups.isNotEmpty()) queryParams["channel-group"] = channelGroups.toCsv()
        queryParams["state"] = pubnub.mapper.toJson(state)
    }
}
