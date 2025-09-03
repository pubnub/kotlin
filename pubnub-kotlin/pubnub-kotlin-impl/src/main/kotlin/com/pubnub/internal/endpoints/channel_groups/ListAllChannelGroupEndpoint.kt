package com.pubnub.internal.endpoints.channel_groups

import com.pubnub.api.endpoints.channel_groups.ListAllChannelGroup
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.logging.LogMessageType
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsListAllResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.Envelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubImpl.listAllChannelGroups]
 */
class ListAllChannelGroupEndpoint internal constructor(pubnub: PubNubImpl) :
    EndpointCore<Envelope<Map<String, Any>>, PNChannelGroupsListAllResult>(pubnub),
    ListAllChannelGroup {
        private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

        override fun doWork(queryParams: HashMap<String, String>): Call<Envelope<Map<String, Any>>> {
            log.trace(
                LogMessage(
                    location = this::class.java.toString(),
                    type = LogMessageType.OBJECT,
                    message = LogMessageContent.Object(
                        message = mapOf(
                            "queryParams" to queryParams
                        )
                    ),
                    details = "ListAllChannelGroup API call"
                )
            )

            return retrofitManager.channelGroupService
                .listAllChannelGroup(
                    configuration.subscribeKey,
                    queryParams,
                )
        }

        @Suppress("UNCHECKED_CAST")
        override fun createResponse(input: Response<Envelope<Map<String, Any>>>): PNChannelGroupsListAllResult =
            PNChannelGroupsListAllResult(
                groups = input.body()!!.payload!!["groups"] as List<String>,
            )

        override fun operationType() = PNOperationType.PNChannelGroupsOperation

        override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.CHANNEL_GROUP
    }
