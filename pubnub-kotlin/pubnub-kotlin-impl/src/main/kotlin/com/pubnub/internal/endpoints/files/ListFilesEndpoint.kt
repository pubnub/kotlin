package com.pubnub.internal.endpoints.files

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.files.ListFiles
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.files.PNListFilesResult
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.files.ListFilesResult
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubImpl.listFiles]
 */
class ListFilesEndpoint(
    private val channel: String,
    private val limit: Int? = null,
    private val next: PNPage.PNNext? = null,
    pubNub: PubNubImpl,
) : EndpointCore<ListFilesResult, PNListFilesResult>(pubNub), ListFiles {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    @Throws(PubNubException::class)
    override fun validateParams() {
        if (channel.isEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
        if (limit != null && limit !in MIN_LIMIT..MAX_LIMIT) {
            throw PubNubException(PubNubError.INVALID_ARGUMENTS).copy(
                errorMessage = "Limit should be in range from 1 to 100 (both inclusive)",
            )
        }
        if (next != null && (next.pageHash.isBlank())) {
            throw PubNubException(PubNubError.INVALID_ARGUMENTS).copy(
                errorMessage = "Next should not be an empty string",
            )
        }
    }

    @Throws(PubNubException::class)
    override fun doWork(queryParams: HashMap<String, String>): Call<ListFilesResult> {
        log.trace(
            LogMessage(
                message = LogMessageContent.Object(
                    message = mapOf(
                        "channel" to channel,
                        "limit" to (limit ?: DEFAULT_LIMIT),
                        "next" to (next?.pageHash ?: ""),
                        "queryParams" to queryParams
                    )
                ),
                details = "ListFiles API call",
            )
        )

        queryParams[LIMIT_QUERY_PARAM] = (limit ?: DEFAULT_LIMIT).toString()
        if (next != null) {
            queryParams[NEXT_PAGE_QUERY_PARAM] = next.pageHash
        }
        return retrofitManager.filesService.listFiles(
            configuration.subscribeKey,
            channel,
            queryParams,
        )
    }

    @Throws(PubNubException::class)
    override fun createResponse(input: Response<ListFilesResult>): PNListFilesResult {
        return input.body()?.let { body ->
            PNListFilesResult(
                body.count,
                body.next,
                body.status,
                body.data,
            )
        } ?: throw PubNubException(PubNubError.INTERNAL_ERROR)
    }

    override fun getAffectedChannels() = listOf(channel)

    override fun getAffectedChannelGroups(): List<String> = listOf()

    override fun operationType(): PNOperationType = PNOperationType.FileOperation

    override fun isAuthRequired(): Boolean = true

    override fun isSubKeyRequired(): Boolean = true

    override fun isPubKeyRequired(): Boolean = false

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.FILE_PERSISTENCE

    companion object {
        private const val LIMIT_QUERY_PARAM = "limit"
        private const val NEXT_PAGE_QUERY_PARAM = "next"
        private const val DEFAULT_LIMIT = 100
        private const val MIN_LIMIT = 1
        private const val MAX_LIMIT = 100
    }
}
