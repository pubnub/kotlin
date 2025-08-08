package com.pubnub.internal.endpoints.channel_groups

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.channel_groups.RemoveChannelChannelGroup
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.logging.LogMessageType
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsRemoveChannelResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.ExtendedLogger
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.toCsv
import org.slf4j.event.Level
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubImpl.removeChannelsFromChannelGroup]
 */
class RemoveChannelChannelGroupEndpoint internal constructor(
    pubnub: PubNubImpl,
    override val channelGroup: String,
    override val channels: List<String>,
) : EndpointCore<Void, PNChannelGroupsRemoveChannelResult>(pubnub), RemoveChannelChannelGroup {
    private val log: ExtendedLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun getAffectedChannels() = channels

    override fun getAffectedChannelGroups() = listOf(channelGroup)

    override fun validateParams() {
        super.validateParams()
        if (channelGroup.isBlank()) {
            throw PubNubException(PubNubError.GROUP_MISSING)
        }
        if (channels.isEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Void> {
        log.trace(
            LogMessage(
                pubNubId = pubnub.instanceId,
                logLevel = Level.TRACE,
                location = this::class.java.toString(),
                type = LogMessageType.OBJECT,
                message = LogMessageContent.Object(
                    message = mapOf(
                        "channelGroup" to channelGroup,
                        "channels" to channels,
                        "queryParams" to queryParams
                    )
                ),
                details = "RemoveChannelChannelGroup API call"
            )
        )

        addQueryParams(queryParams)

        return retrofitManager.channelGroupService
            .removeChannel(
                configuration.subscribeKey,
                channelGroup,
                queryParams,
            )
    }

    override fun createResponse(input: Response<Void>): PNChannelGroupsRemoveChannelResult = PNChannelGroupsRemoveChannelResult()

    override fun operationType() = PNOperationType.PNRemoveChannelsFromGroupOperation

    private fun addQueryParams(queryParams: MutableMap<String, String>) {
        if (channels.isNotEmpty()) {
            queryParams["remove"] = channels.toCsv()
        }
    }

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.CHANNEL_GROUP
}
