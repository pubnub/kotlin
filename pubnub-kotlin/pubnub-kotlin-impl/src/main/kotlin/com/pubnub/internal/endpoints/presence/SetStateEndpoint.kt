package com.pubnub.internal.endpoints.presence

import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.presence.SetState
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.logging.LogMessageType
import com.pubnub.api.models.consumer.presence.PNSetStateResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.models.server.Envelope
import com.pubnub.internal.presence.eventengine.data.PresenceData
import com.pubnub.internal.toCsv
import org.slf4j.event.Level
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubImpl.setPresenceState]
 */
class SetStateEndpoint internal constructor(
    pubnub: PubNubImpl,
    override val channels: List<String>,
    override val channelGroups: List<String>,
    override val state: Any,
    override val uuid: String = pubnub.configuration.userId.value,
    private val presenceData: PresenceData,
) : EndpointCore<Envelope<JsonElement>, PNSetStateResult>(pubnub), SetState {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun getAffectedChannels() = channels

    override fun getAffectedChannelGroups() = channelGroups

    override fun validateParams() {
        super.validateParams()
        if (channels.isEmpty() && channelGroups.isEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_AND_GROUP_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Envelope<JsonElement>> {
        log.trace(
            LogMessage(
                pubNubId = pubnub.instanceId,
                logLevel = Level.TRACE,
                location = this::class.java.toString(),
                type = LogMessageType.OBJECT,
                message = LogMessageContent.Object(
                    message = mapOf(
                        "channels" to channels,
                        "channelGroups" to channelGroups,
                        "state" to state,
                        "uuid" to uuid,
                        "queryParams" to queryParams
                    )
                ),
                details = "SetState API call"
            )
        )

        if (uuid == pubnub.configuration.userId.value) {
            val stateCopy = pubnub.mapper.fromJson(pubnub.mapper.toJson(state), JsonElement::class.java)
            presenceData.channelStates.putAll(channels.associateWith { stateCopy })
        }

        addQueryParams(queryParams)

        return retrofitManager.presenceService.setState(
            configuration.subscribeKey,
            channels.toCsv(),
            uuid,
            queryParams,
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
        if (channelGroups.isNotEmpty()) {
            queryParams["channel-group"] = channelGroups.toCsv()
        }
        queryParams["state"] = pubnub.mapper.toJson(state)
    }
}
