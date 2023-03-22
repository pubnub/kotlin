package com.pubnub.api.endpoints

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.SpaceId
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.HistoryMessageType
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.history.PNFetchMessageItem
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import com.pubnub.api.models.server.FetchMessagesEnvelope
import com.pubnub.api.toCsv
import com.pubnub.extension.limit
import com.pubnub.extension.nonPositiveToNull
import com.pubnub.extension.processHistoryMessage
import retrofit2.Call
import retrofit2.Response
import java.util.Locale

/**
 * @see [PubNub.fetchMessages]
 */
class FetchMessages internal constructor(
    pubnub: PubNub,
    val channels: List<String>,
    val page: PNBoundedPage,
    val includeUUID: Boolean,
    val includeMeta: Boolean,
    val includeMessageActions: Boolean,
    val includeMessageType: Boolean,
    val includeType: Boolean,
    val includeSpaceId: Boolean
) : Endpoint<FetchMessagesEnvelope, PNFetchMessagesResult>(pubnub) {

    internal companion object {
        private const val SINGLE_CHANNEL_DEFAULT_MESSAGES = 100
        private const val SINGLE_CHANNEL_MAX_MESSAGES = 100
        private const val MULTIPLE_CHANNEL_DEFAULT_MESSAGES = 25
        private const val MULTIPLE_CHANNEL_MAX_MESSAGES = 25
        private const val DEFAULT_MESSAGES_WITH_ACTIONS = 25
        private const val MAX_MESSAGES_WITH_ACTIONS = 25
        internal const val INCLUDE_SPACE_ID_QUERY_PARAM = "include_space_id"
        internal const val INCLUDE_MESSAGE_TYPE_QUERY_PARAM = "include_message_type"
        internal const val INCLUDE_TYPE_QUERY_PARAM = "include_type"

        internal fun effectiveMax(
            maximumPerChannel: Int?,
            includeMessageActions: Boolean,
            numberOfChannels: Int
        ): Int = when {
            includeMessageActions -> maximumPerChannel?.limit(MAX_MESSAGES_WITH_ACTIONS)?.nonPositiveToNull()
                ?: DEFAULT_MESSAGES_WITH_ACTIONS

            numberOfChannels == 1 -> maximumPerChannel?.limit(SINGLE_CHANNEL_MAX_MESSAGES)?.nonPositiveToNull()
                ?: SINGLE_CHANNEL_DEFAULT_MESSAGES

            else -> maximumPerChannel?.limit(MULTIPLE_CHANNEL_MAX_MESSAGES)?.nonPositiveToNull()
                ?: MULTIPLE_CHANNEL_DEFAULT_MESSAGES
        }
    }

    override fun validateParams() {
        super.validateParams()
        if (channels.isEmpty()) throw PubNubException(PubNubError.CHANNEL_MISSING)
        if (includeMessageActions && channels.size > 1) throw PubNubException(PubNubError.HISTORY_MESSAGE_ACTIONS_MULTIPLE_CHANNELS)
    }

    override fun getAffectedChannels() = channels

    override fun doWork(queryParams: HashMap<String, String>): Call<FetchMessagesEnvelope> {
        addQueryParams(queryParams)

        return if (!includeMessageActions) {
            pubnub.retrofitManager.historyService.fetchMessages(
                subKey = pubnub.configuration.subscribeKey, channels = channels.toCsv(), options = queryParams
            )
        } else {
            pubnub.retrofitManager.historyService.fetchMessagesWithActions(
                subKey = pubnub.configuration.subscribeKey, channel = channels.first(), options = queryParams
            )
        }
    }

    override fun createResponse(input: Response<FetchMessagesEnvelope>): PNFetchMessagesResult {
        val body = input.body()!!
        val channelsMap = body.channels.mapValues { (_, value) ->
            value.map { serverMessageItem ->
                val newMessage = serverMessageItem.message.processHistoryMessage(pubnub.configuration, pubnub.mapper)
                val newActions =
                    if (includeMessageActions) serverMessageItem.actions ?: mapOf() else serverMessageItem.actions
                PNFetchMessageItem(
                    uuid = serverMessageItem.uuid,
                    message = newMessage,
                    meta = serverMessageItem.meta,
                    timetoken = serverMessageItem.timetoken,
                    actions = newActions,
                    messageType = if (includeMessageType) HistoryMessageType.of(serverMessageItem.messageType) else null,
                    type = serverMessageItem.type,
                    spaceId = serverMessageItem.spaceId?.let { SpaceId(it) },
                )
            }
        }.toMap()

        val page = body.more?.let {
            PNBoundedPage(start = it.start, end = it.end, limit = it.max)
        }
        return PNFetchMessagesResult(channels = channelsMap, page = page)
    }

    override fun operationType() = PNOperationType.PNFetchMessagesOperation

    private fun addQueryParams(queryParams: MutableMap<String, String>) {
        queryParams["max"] = effectiveMax(page.limit, includeMessageActions, channels.size).toString()
        queryParams["include_uuid"] = includeUUID.toString()

        page.start?.run { queryParams["start"] = this.toString().lowercase(Locale.US) }
        page.end?.run { queryParams["end"] = this.toString().lowercase(Locale.US) }

        queryParams[INCLUDE_MESSAGE_TYPE_QUERY_PARAM] = includeMessageType.toString()
        queryParams[INCLUDE_TYPE_QUERY_PARAM] = includeType.toString()
        queryParams[INCLUDE_SPACE_ID_QUERY_PARAM] = includeSpaceId.toString()

        if (includeMeta) queryParams["include_meta"] = "true"
    }
}
