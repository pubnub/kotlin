package com.pubnub.api.endpoints.presence

import com.google.gson.JsonElement
import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.presence.PNGetStateResult
import com.pubnub.api.models.server.Envelope
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.api.toCsv
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNub.getPresenceState]
 */
class GetState internal constructor(
    pubnub: PubNub,
    val channels: List<String>,
    val channelGroups: List<String>,
    val uuid: String = pubnub.configuration.userId.value
) : Endpoint<Envelope<JsonElement>, PNGetStateResult>(pubnub) {

    override fun getAffectedChannels() = channels

    override fun getAffectedChannelGroups() = channelGroups

    override fun validateParams() {
        super.validateParams()
        if (channels.isEmpty() && channelGroups.isEmpty())
            throw PubNubException(PubNubError.CHANNEL_AND_GROUP_MISSING)
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Envelope<JsonElement>> {
        addQueryParams(queryParams)

        return pubnub.retrofitManager.presenceService.getState(
            pubnub.configuration.subscribeKey,
            channels.toCsv(),
            uuid,
            queryParams
        )
    }

    override fun createResponse(input: Response<Envelope<JsonElement>>): PNGetStateResult {
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

        return PNGetStateResult(stateByUUID = stateMappings)
    }

    override fun operationType() = PNOperationType.PNGetState

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.PRESENCE

    private fun addQueryParams(queryParams: MutableMap<String, String>) {
        if (channelGroups.isNotEmpty()) queryParams["channel-group"] = channelGroups.toCsv()
    }
}
