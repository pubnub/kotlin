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
import java.util.ArrayList
import java.util.HashMap
import java.util.Locale

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
        if (!::channel.isInitialized) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
        count =
            when (count) {
                in 1..MAX_COUNT -> count
                else -> MAX_COUNT
            }
    }

    override fun getAffectedChannels() = listOf(channel)

    override fun getAffectedChannelGroups() = emptyList<String>()

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

        var result: PNHistoryResult? = null
        val messages = ArrayList<PNHistoryItemResult>()

        input.body()?.let {
            val startTimeToken = pubnub.mapper.elementToLong(pubnub.mapper.getArrayElement(it, 1))
            val endTimeToken = pubnub.mapper.elementToLong(pubnub.mapper.getArrayElement(it, 2))

            pubnub.mapper.getArrayElement(it, 0).run {
                if (this.isJsonArray) {
                    pubnub.mapper.getArrayIterator(this)?.forEach {

                        var message: JsonElement?
                        var timetoken: Long? = null
                        var meta: JsonElement? = null

                        if (includeTimetoken || includeMeta) {
                            if (includeTimetoken) {
                                timetoken = pubnub.mapper.elementToLong(it, "timetoken")
                            }
                            if (includeMeta) {
                                meta = pubnub.mapper.getField(it, "meta")
                            }
                            message = processMessage(pubnub.mapper.getField(it, "message")!!)
                        } else {
                            message = processMessage(it)
                        }

                        messages += PNHistoryItemResult(
                            message,
                            timetoken,
                            meta
                        )
                    }
                }
            }

            result = PNHistoryResult(
                messages,
                startTimeToken,
                endTimeToken
            )
        }

        return result
    }

    @Throws(PubNubException::class)
    private fun processMessage(message: JsonElement): JsonElement {
        if (!pubnub.configuration.isCipherKeyValid())
            return message

        val crypto = Crypto(pubnub.configuration.cipherKey)

        val inputText = pubnub.mapper.getField(message, "pn_other")?.let {
            pubnub.mapper.elementToString(it)
        }.run {
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

    override fun operationType() = PNOperationType.PNHistoryOperation

    override fun isSubKeyRequired() = true
    override fun isPubKeyRequired() = false
    override fun isAuthRequired() = true
}