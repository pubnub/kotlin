package com.pubnub.internal.endpoints

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.FetchMessages
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.history.HistoryMessageType
import com.pubnub.api.models.consumer.history.PNFetchMessageItem
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.extension.limit
import com.pubnub.internal.extension.nonPositiveToNull
import com.pubnub.internal.extension.tryDecryptMessage
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.FetchMessagesEnvelope
import com.pubnub.internal.models.server.history.ServerFetchMessageItem
import com.pubnub.internal.toCsv
import retrofit2.Call
import retrofit2.Response
import java.util.Locale

/**
 * @see [PubNubImpl.fetchMessages]
 */
class FetchMessagesEndpoint internal constructor(
    pubnub: PubNubImpl,
    override val channels: List<String>,
    override val page: PNBoundedPage,
    override val includeUUID: Boolean = false,
    override val includeMeta: Boolean = false,
    override val includeMessageActions: Boolean = false,
    override val includeMessageType: Boolean = false,
    override val includeCustomMessageType: Boolean = false
) : EndpointCore<FetchMessagesEnvelope, PNFetchMessagesResult>(pubnub), FetchMessages {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    internal companion object {
        private const val SINGLE_CHANNEL_DEFAULT_MESSAGES = 100
        private const val SINGLE_CHANNEL_MAX_MESSAGES = 100
        private const val MULTIPLE_CHANNEL_DEFAULT_MESSAGES = 25
        private const val MULTIPLE_CHANNEL_MAX_MESSAGES = 25
        private const val DEFAULT_MESSAGES_WITH_ACTIONS = 25
        private const val MAX_MESSAGES_WITH_ACTIONS = 25
        internal const val INCLUDE_MESSAGE_TYPE_QUERY_PARAM = "include_message_type"
        private const val INCLUDE_CUSTOM_MESSAGE_TYPE = "include_custom_message_type"

        internal fun effectiveMax(
            maximumPerChannel: Int?,
            includeMessageActions: Boolean,
            numberOfChannels: Int,
        ): Int =
            when {
                includeMessageActions ->
                    maximumPerChannel?.limit(MAX_MESSAGES_WITH_ACTIONS)?.nonPositiveToNull()
                        ?: DEFAULT_MESSAGES_WITH_ACTIONS

                numberOfChannels == 1 ->
                    maximumPerChannel?.limit(SINGLE_CHANNEL_MAX_MESSAGES)?.nonPositiveToNull()
                        ?: SINGLE_CHANNEL_DEFAULT_MESSAGES

                else ->
                    maximumPerChannel?.limit(MULTIPLE_CHANNEL_MAX_MESSAGES)?.nonPositiveToNull()
                        ?: MULTIPLE_CHANNEL_DEFAULT_MESSAGES
            }
    }

    override fun validateParams() {
        super.validateParams()
        if (channels.isEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
        if (includeMessageActions && channels.size > 1) {
            throw PubNubException(PubNubError.HISTORY_MESSAGE_ACTIONS_MULTIPLE_CHANNELS)
        }
    }

    override fun getAffectedChannels() = channels

    override fun doWork(queryParams: HashMap<String, String>): Call<FetchMessagesEnvelope> {
        log.trace(
            LogMessage(
                location = this::class.java.toString(),
                message = LogMessageContent.Object(
                    message = mapOf(
                        "channels" to channels,
                        "page" to page,
                        "includeUUID" to includeUUID,
                        "includeMeta" to includeMeta,
                        "includeMessageActions" to includeMessageActions,
                        "includeMessageType" to includeMessageType,
                        "includeCustomMessageType" to includeCustomMessageType,
                        "queryParams" to queryParams
                    )
                ),
                details = "FetchMessages API call"
            )
        )

        addQueryParams(queryParams)

        return if (!includeMessageActions) {
            retrofitManager.historyService.fetchMessages(
                subKey = configuration.subscribeKey,
                channels = channels.toCsv(),
                options = queryParams,
            )
        } else {
            retrofitManager.historyService.fetchMessagesWithActions(
                subKey = configuration.subscribeKey,
                channel = channels.first(),
                options = queryParams,
            )
        }
    }

    override fun createResponse(input: Response<FetchMessagesEnvelope>): PNFetchMessagesResult {
        val body = input.body()!!
        val channelsMap =
            body.channels.mapValues { (_, value) ->
                value.map { serverMessageItem: ServerFetchMessageItem ->
                    val (newMessage, error) =
                        serverMessageItem.message.tryDecryptMessage(
                            pubnub.cryptoModuleWithLogConfig,
                            pubnub.mapper,
                            log,
                        )
                    val newActions =
                        if (includeMessageActions) {
                            serverMessageItem.actions ?: mapOf()
                        } else {
                            serverMessageItem.actions
                        }
                    PNFetchMessageItem(
                        uuid = serverMessageItem.uuid,
                        message = newMessage,
                        meta = serverMessageItem.meta,
                        timetoken = serverMessageItem.timetoken,
                        actions = newActions,
                        messageType =
                            if (includeMessageType) {
                                HistoryMessageType.of(serverMessageItem.messageType)
                            } else {
                                null
                            },
                        customMessageType = serverMessageItem.customMessageType,
                        error = error,
                    )
                }
            }.toMap()

        val page =
            body.more?.let {
                PNBoundedPage(start = it.start, end = it.end, limit = it.max)
            }
        return PNFetchMessagesResult(channels = channelsMap, page = page)
    }

    override fun operationType() = PNOperationType.PNFetchMessagesOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.MESSAGE_PERSISTENCE

    private fun addQueryParams(queryParams: MutableMap<String, String>) {
        queryParams["max"] = effectiveMax(page.limit, includeMessageActions, channels.size).toString()
        queryParams["include_uuid"] = includeUUID.toString()

        page.start?.run { queryParams["start"] = this.toString().lowercase(Locale.US) }
        page.end?.run { queryParams["end"] = this.toString().lowercase(Locale.US) }

        queryParams[INCLUDE_MESSAGE_TYPE_QUERY_PARAM] = includeMessageType.toString()

        if (includeMeta) {
            queryParams["include_meta"] = "true"
        }
        queryParams[INCLUDE_CUSTOM_MESSAGE_TYPE] = includeCustomMessageType.toString()
    }
}
