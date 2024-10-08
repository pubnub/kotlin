package com.pubnub.internal.endpoints

import com.google.gson.JsonElement
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.History
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.history.PNHistoryItemResult
import com.pubnub.api.models.consumer.history.PNHistoryResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.extension.tryDecryptMessage
import retrofit2.Call
import retrofit2.Response
import java.util.Locale

/**
 * @see [PubNubImpl.history]
 */
class HistoryEndpoint internal constructor(
    pubnub: PubNubImpl,
    override val channel: String,
    override val start: Long? = null,
    override val end: Long? = null,
    override val count: Int,
    override val reverse: Boolean,
    override val includeTimetoken: Boolean,
    override val includeMeta: Boolean,
) : EndpointCore<JsonElement, PNHistoryResult>(pubnub), History {
    private val countParam: Int =
        if (count in 1..PNHistoryResult.MAX_COUNT) {
            count
        } else {
            PNHistoryResult.MAX_COUNT
        }

    override fun validateParams() {
        super.validateParams()
        if (channel.isBlank()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
    }

    override fun getAffectedChannels() = listOf(channel)

    override fun doWork(queryParams: HashMap<String, String>): Call<JsonElement> {
        addQueryParams(queryParams)

        return retrofitManager.historyService.fetchHistory(
            configuration.subscribeKey,
            channel,
            queryParams,
        )
    }

    override fun createResponse(input: Response<JsonElement>): PNHistoryResult {
        val startTimeToken =
            pubnub.mapper.elementToLong(pubnub.mapper.getArrayElement(input.body()!!, 1))
        val endTimeToken =
            pubnub.mapper.elementToLong(pubnub.mapper.getArrayElement(input.body()!!, 2))

        val messages = mutableListOf<PNHistoryItemResult>()

        val historyData =
            PNHistoryResult(
                messages = messages,
                startTimetoken = startTimeToken,
                endTimetoken = endTimeToken,
            )

        if (pubnub.mapper.getArrayElement(input.body()!!, 0).isJsonArray) {
            val iterator = pubnub.mapper.getArrayIterator(pubnub.mapper.getArrayElement(input.body()!!, 0))!!
            while (iterator.hasNext()) {
                val historyEntry = iterator.next()

                var timetoken: Long? = null
                var meta: JsonElement? = null
                val historyMessageWithError: Pair<JsonElement, PubNubError?>

                if (includeTimetoken || includeMeta) {
                    historyMessageWithError =
                        pubnub.mapper.getField(historyEntry, "message")!!
                            .tryDecryptMessage(configuration.cryptoModule, pubnub.mapper)
                    if (includeTimetoken) {
                        timetoken = pubnub.mapper.elementToLong(historyEntry, "timetoken")
                    }
                    if (includeMeta) {
                        meta = pubnub.mapper.getField(historyEntry, "meta")
                    }
                } else {
                    historyMessageWithError = historyEntry.tryDecryptMessage(configuration.cryptoModule, pubnub.mapper)
                }

                val message: JsonElement = historyMessageWithError.first
                val error: PubNubError? = historyMessageWithError.second

                val historyItem =
                    PNHistoryItemResult(
                        entry = message,
                        timetoken = timetoken,
                        meta = meta,
                        error = error,
                    )

                messages.add(historyItem)
            }
        } else {
            throw PubNubException(
                pubnubError = PubNubError.HTTP_ERROR,
                errorMessage = "History is disabled",
            )
        }

        return historyData
    }

    override fun operationType() = PNOperationType.PNHistoryOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.MESSAGE_PERSISTENCE

    private fun addQueryParams(queryParams: MutableMap<String, String>) {
        queryParams["reverse"] = reverse.toString()
        queryParams["include_token"] = includeTimetoken.toString()
        queryParams["include_meta"] = includeMeta.toString()
        queryParams["count"] = countParam.toString()

        start?.run { queryParams["start"] = this.toString().lowercase(Locale.US) }
        end?.run { queryParams["end"] = this.toString().lowercase(Locale.US) }
    }
}
