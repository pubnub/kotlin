package com.pubnub.api.endpoints

import com.google.gson.JsonElement
import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.history.PNHistoryItemResult
import com.pubnub.api.models.consumer.history.PNHistoryResult
import com.pubnub.api.vendor.Crypto
import retrofit2.Call
import retrofit2.Response
import java.util.HashMap
import java.util.Locale

/**
 * @see [PubNub.history]
 */
class History internal constructor(
    pubnub: PubNub,
    val channel: String,
    val start: Long? = null,
    val end: Long? = null,
    val count: Int,
    val reverse: Boolean,
    val includeTimetoken: Boolean,
    val includeMeta: Boolean
) : Endpoint<JsonElement, PNHistoryResult>(pubnub) {

    internal companion object {
        internal const val MAX_COUNT = 100
    }

    private val countParam: Int = if (count in 1..MAX_COUNT) count else MAX_COUNT

    override fun validateParams() {
        super.validateParams()
        if (channel.isBlank()) throw PubNubException(PubNubError.CHANNEL_MISSING)
    }

    override fun getAffectedChannels() = listOf(channel)

    override fun doWork(queryParams: HashMap<String, String>): Call<JsonElement> {
        addQueryParams(queryParams)

        return pubnub.retrofitManager.historyService.fetchHistory(
            pubnub.configuration.subscribeKey,
            channel,
            queryParams
        )
    }

    override fun createResponse(input: Response<JsonElement>): PNHistoryResult {
        val startTimeToken =
            pubnub.mapper.elementToLong(pubnub.mapper.getArrayElement(input.body()!!, 1))
        val endTimeToken =
            pubnub.mapper.elementToLong(pubnub.mapper.getArrayElement(input.body()!!, 2))

        val messages = mutableListOf<PNHistoryItemResult>()

        val historyData = PNHistoryResult(
            messages = messages,
            startTimetoken = startTimeToken,
            endTimetoken = endTimeToken
        )

        if (pubnub.mapper.getArrayElement(input.body()!!, 0).isJsonArray) {
            val iterator = pubnub.mapper.getArrayIterator(pubnub.mapper.getArrayElement(input.body()!!, 0))!!
            while (iterator.hasNext()) {

                val historyEntry = iterator.next()

                var message: JsonElement
                var timetoken: Long? = null
                var meta: JsonElement? = null

                if (includeTimetoken || includeMeta) {
                    message = processMessage(pubnub.mapper.getField(historyEntry, "message")!!)
                    if (includeTimetoken) {
                        timetoken = pubnub.mapper.elementToLong(historyEntry, "timetoken")
                    }
                    if (includeMeta) {
                        meta = pubnub.mapper.getField(historyEntry, "meta")
                    }
                } else {
                    message = processMessage(historyEntry)
                }

                val historyItem = PNHistoryItemResult(
                    entry = message,
                    timetoken = timetoken,
                    meta = meta
                )

                messages.add(historyItem)
            }
        } else {
            throw PubNubException(PubNubError.HTTP_ERROR).apply {
                errorMessage = "History is disabled"
            }
        }

        return historyData
    }

    override fun operationType() = PNOperationType.PNHistoryOperation

    private fun processMessage(message: JsonElement): JsonElement {
        if (!pubnub.configuration.isCipherKeyValid())
            return message

        val crypto = Crypto(pubnub.configuration.cipherKey)

        val inputText =
            if (pubnub.mapper.isJsonObject(message) && pubnub.mapper.hasField(
                    message,
                    "pn_other"
                )
            ) {
                pubnub.mapper.elementToString(message, "pn_other")
            } else {
                pubnub.mapper.elementToString(message)
            }

        val outputText = crypto.decrypt(inputText!!)

        var outputObject = pubnub.mapper.fromJson(outputText, JsonElement::class.java)

        pubnub.mapper.getField(message, "pn_other")?.let {
            val objectNode = pubnub.mapper.getAsObject(message)
            pubnub.mapper.putOnObject(objectNode, "pn_other", outputObject)
            outputObject = objectNode
        }

        return outputObject
    }

    private fun addQueryParams(queryParams: MutableMap<String, String>) {
        queryParams["reverse"] = reverse.toString()
        queryParams["include_token"] = includeTimetoken.toString()
        queryParams["include_meta"] = includeMeta.toString()
        queryParams["count"] = countParam.toString()

        start?.run { queryParams["start"] = this.toString().toLowerCase(Locale.US) }
        end?.run { queryParams["end"] = this.toString().toLowerCase(Locale.US) }
    }
}
