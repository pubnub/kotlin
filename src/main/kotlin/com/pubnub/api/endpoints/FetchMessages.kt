package com.pubnub.api.endpoints

import com.google.gson.JsonElement
import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.history.PNFetchMessageItem
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import com.pubnub.api.models.server.FetchMessagesEnvelope
import com.pubnub.api.toCsv
import com.pubnub.api.vendor.Crypto
import org.slf4j.LoggerFactory
import retrofit2.Call
import retrofit2.Response
import java.util.HashMap
import java.util.Locale

class FetchMessages(pubnub: PubNub) : Endpoint<FetchMessagesEnvelope, PNFetchMessagesResult>(pubnub) {

    private val log = LoggerFactory.getLogger("FetchMessages")

    private companion object {
        private const val DEFAULT_MESSAGES = 1
        private const val MAX_MESSAGES = 25
    }

    lateinit var channels: List<String>
    var maximumPerChannel = 0
    var start: Long? = null
    var end: Long? = null
    var includeMeta = false
    var includeMessageActions = false

    override fun validateParams() {
        super.validateParams()
        if (!::channels.isInitialized || channels.isEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }

        if (!includeMessageActions) {
            if (maximumPerChannel !in DEFAULT_MESSAGES..MAX_MESSAGES) {
                when {
                    maximumPerChannel < DEFAULT_MESSAGES -> maximumPerChannel = DEFAULT_MESSAGES
                    maximumPerChannel > MAX_MESSAGES -> maximumPerChannel = MAX_MESSAGES
                }
                log.info("maximumPerChannel param defaulting to $maximumPerChannel")
            }
        } else {
            if (maximumPerChannel !in DEFAULT_MESSAGES..MAX_MESSAGES) {
                maximumPerChannel = MAX_MESSAGES
                log.info("maximumPerChannel param defaulting to $maximumPerChannel")
            }
        }
    }

    override fun getAffectedChannels() = channels

    override fun doWork(queryParams: HashMap<String, String>): Call<FetchMessagesEnvelope> {
        queryParams["max"] = maximumPerChannel.toString()

        start?.let {
            queryParams["start"] = it.toString().toLowerCase(Locale.US)
        }
        end?.let {
            queryParams["end"] = it.toString().toLowerCase(Locale.US)
        }

        if (includeMeta) {
            queryParams["include_meta"] = includeMeta.toString()
        }

        return if (!includeMessageActions) {
            pubnub.retrofitManager.historyService.fetchMessages(
                subKey = pubnub.configuration.subscribeKey,
                channels = channels.toCsv(),
                options = queryParams
            )
        } else {
            if (channels.size > 1) {
                throw PubNubException(PubNubError.HISTORY_MESSAGE_ACTIONS_MULTIPLE_CHANNELS)
            }
            pubnub.retrofitManager.historyService.fetchMessagesWithActions(
                subKey = pubnub.configuration.subscribeKey,
                channel = channels.first(),
                options = queryParams
            )
        }
    }

    override fun createResponse(input: Response<FetchMessagesEnvelope>): PNFetchMessagesResult? {
        val channelsMap = hashMapOf<String, List<PNFetchMessageItem>>()

        for (entry in input.body()!!.channels) {
            val items = mutableListOf<PNFetchMessageItem>()
            for (item in entry.value) {
                items.add(
                    PNFetchMessageItem(
                        message = processMessage(item.message),
                        timetoken = item.timetoken,
                        meta = item.meta
                    ).apply {
                        if (includeMessageActions) {
                            actions = (item.actions) ?: mapOf()
                        }
                    }
                )
            }
            channelsMap[entry.key] = items
        }

        return PNFetchMessagesResult(channelsMap)
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

        val outputText = crypto.decrypt(inputText!!)

        var outputObject = pubnub.mapper.fromJson(outputText, JsonElement::class.java)

        pubnub.mapper.getField(message, "pn_other")?.let {
            val objectNode = pubnub.mapper.getAsObject(message)
            pubnub.mapper.putOnObject(objectNode, "pn_other", outputObject)
            outputObject = objectNode
        }

        return outputObject
    }

    override fun operationType() = PNOperationType.PNFetchMessagesOperation
}
