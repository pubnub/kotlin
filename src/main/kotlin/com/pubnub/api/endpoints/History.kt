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
import java.util.*

class History(pubnub: PubNub) : Endpoint<JsonElement, PNHistoryResult>(pubnub) {

    private companion object {
        private const val MAX_COUNT = 100
    }

    lateinit var channel: String
    var start: Long? = null
    var end: Long? = null
    var count = MAX_COUNT
    var reverse = false
    var includeTimetoken = false
    var includeMeta = false

    override fun validateParams() {
        super.validateParams()
        if (!::channel.isInitialized || channel.isBlank()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
        count =
            when (count) {
                in 1..MAX_COUNT -> count
                else -> MAX_COUNT
            }
    }

    override fun getAffectedChannels() = listOf(channel)

    override fun doWork(queryParams: HashMap<String, String>): Call<JsonElement> {
        queryParams["reverse"] = reverse.toString()
        queryParams["include_token"] = includeTimetoken.toString()
        queryParams["include_meta"] = includeMeta.toString()
        queryParams["count"] = count.toString()

        start?.let {
            queryParams["start"] = it.toString().toLowerCase(Locale.US)
        }
        end?.let {
            queryParams["end"] = it.toString().toLowerCase(Locale.US)
        }

        return pubnub.retrofitManager.historyService.fetchHistory(
            pubnub.configuration.subscribeKey,
            channel,
            queryParams
        )
    }

    override fun createResponse(input: Response<JsonElement>): PNHistoryResult? {
        val startTimeToken = pubnub.mapper.elementToLong(pubnub.mapper.getArrayElement(input.body()!!, 1))
        val endTimeToken = pubnub.mapper.elementToLong(pubnub.mapper.getArrayElement(input.body()!!, 2))

        val messages = mutableListOf<PNHistoryItemResult>()

        var historyData = PNHistoryResult(
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

    private fun processMessage(message: JsonElement): JsonElement {
        if (!pubnub.configuration.isCipherKeyValid())
            return message

        val crypto = Crypto(pubnub.configuration.cipherKey)

        val inputText =
            if (pubnub.mapper.isJsonObject(message) && pubnub.mapper.hasField(message, "pn_other")) {
                pubnub.mapper.elementToString(message, "pn_other")
            } else {
                pubnub.mapper.elementToString(message)
            }

        println("InputtTextt $inputText")

        val outputText = crypto.decrypt(inputText!!)

        var outputObject = pubnub.mapper.fromJson(outputText, JsonElement::class.java)

        pubnub.mapper.getField(message, "pn_other")?.let {
            val objectNode = pubnub.mapper.getAsObject(message)
            pubnub.mapper.putOnObject(objectNode, "pn_other", outputObject)
            outputObject = objectNode
        }

        return outputObject
    }

    override fun operationType() = PNOperationType.PNHistoryOperation
}