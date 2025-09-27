package com.pubnub.internal.endpoints

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.DeleteMessages
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.toCsv
import retrofit2.Call
import retrofit2.Response
import java.util.Locale

/**
 * @see [PubNubImpl.deleteMessages]
 */
class DeleteMessagesEndpoint internal constructor(
    pubnub: PubNubImpl,
    override val channels: List<String>,
    override val start: Long? = null,
    override val end: Long? = null,
) : EndpointCore<Void, PNDeleteMessagesResult>(pubnub), DeleteMessages {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun validateParams() {
        super.validateParams()
        if (channels.isEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Void> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    message = mapOf(
                        "channels" to channels,
                        "start" to (start ?: ""),
                        "end" to (end ?: ""),
                        "queryParams" to queryParams
                    )
                ),
                details = "DeleteMessages API call",
            )
        )

        addQueryParams(queryParams)

        return retrofitManager.historyService.deleteMessages(
            configuration.subscribeKey,
            channels.toCsv(),
            queryParams,
        )
    }

    override fun createResponse(input: Response<Void>): PNDeleteMessagesResult = PNDeleteMessagesResult()

    override fun operationType() = PNOperationType.PNDeleteMessagesOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.MESSAGE_PERSISTENCE

    private fun addQueryParams(queryParams: MutableMap<String, String>) {
        start?.run { queryParams["start"] = this.toString().lowercase(Locale.US) }
        end?.run { queryParams["end"] = this.toString().lowercase(Locale.US) }
    }
}
