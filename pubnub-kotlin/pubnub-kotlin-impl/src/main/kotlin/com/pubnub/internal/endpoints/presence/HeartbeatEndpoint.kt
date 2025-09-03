package com.pubnub.internal.endpoints.presence

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.logging.LogMessageType
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.PubNubUtil
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import retrofit2.Call
import retrofit2.Response

class HeartbeatEndpoint internal constructor(
    pubnub: PubNubImpl,
    val channels: List<String> = listOf(),
    val channelGroups: List<String> = listOf(),
    val state: Any? = null,
) : EndpointCore<Void, Boolean>(pubnub) {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun getAffectedChannels() = channels

    override fun getAffectedChannelGroups() = channelGroups

    override fun validateParams() {
        super.validateParams()
        when {
            channels.any { it.isEmpty() } || channelGroups.any { it.isEmpty() } -> {
                throw PubNubException(PubNubError.CHANNEL_AND_GROUP_CONTAINS_EMPTY_STRING)
            }
            channels.isEmpty() && channelGroups.isEmpty() -> {
                throw PubNubException(PubNubError.CHANNEL_AND_GROUP_MISSING)
            }
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Void> {
        log.trace(
            LogMessage(
                location = this::class.java.toString(),
                type = LogMessageType.OBJECT,
                message = LogMessageContent.Object(
                    message = mapOf(
                        "channels" to channels,
                        "channelGroups" to channelGroups,
                        "state" to (state ?: ""),
                        "queryParams" to queryParams
                    )
                ),
                details = "Heartbeat API call"
            )
        )

        addQueryParams(queryParams)

        val channelsCsv =
            if (channels.isNotEmpty()) {
                channels.joinToString(",")
            } else {
                ","
            }

        return retrofitManager.presenceService.heartbeat(
            configuration.subscribeKey,
            channelsCsv,
            queryParams,
        )
    }

    private fun addQueryParams(queryParams: HashMap<String, String>) {
        queryParams["heartbeat"] = pubnub.configuration.presenceTimeout.toString()

        if (channelGroups.isNotEmpty()) {
            queryParams["channel-group"] = channelGroups.joinToString(",")
        }

        state?.let {
            queryParams["state"] = pubnub.mapper.toJson(it)
        }

        PubNubUtil.maybeAddEeQueryParam(queryParams)
    }

    override fun createResponse(input: Response<Void>): Boolean {
        return true
    }

    override fun operationType() = PNOperationType.PNHeartbeatOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.PRESENCE
}
