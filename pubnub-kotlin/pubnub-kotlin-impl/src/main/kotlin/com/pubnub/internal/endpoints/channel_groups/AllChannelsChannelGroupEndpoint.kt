package com.pubnub.internal.endpoints.channel_groups

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.channel_groups.AllChannelsChannelGroup
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.Envelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubImpl.listChannelsForChannelGroup]
 */
class AllChannelsChannelGroupEndpoint internal constructor(
    pubnub: PubNubImpl,
    override val channelGroup: String,
) : EndpointCore<Envelope<Map<String, Any>>, PNChannelGroupsAllChannelsResult>(pubnub),
    AllChannelsChannelGroup {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun getAffectedChannelGroups() = listOf(channelGroup)

    override fun validateParams() {
        super.validateParams()
        if (channelGroup.isBlank()) {
            throw PubNubException(PubNubError.GROUP_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Envelope<Map<String, Any>>> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    message = mapOf(
                        "channelGroup" to channelGroup,
                        "queryParams" to queryParams
                    )
                ),
                details = "AllChannelsChannelGroup API call",
            )
        )

        return retrofitManager.channelGroupService
            .allChannelsChannelGroup(
                configuration.subscribeKey,
                channelGroup,
                queryParams,
            )
    }

    @Suppress("UNCHECKED_CAST")
    override fun createResponse(input: Response<Envelope<Map<String, Any>>>): PNChannelGroupsAllChannelsResult =
        PNChannelGroupsAllChannelsResult(
            channels = input.body()!!.payload!!["channels"] as List<String>,
        )

    override fun operationType() = PNOperationType.PNChannelsForGroupOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.CHANNEL_GROUP
}
