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

/**
 * @see [PubNub.fetchMessages]
 */
class FetchMessages internal constructor(
    pubnub: PubNub,
    val channels: List<String>,
    val maximumPerChannel: Int,
    val start: Long? = null,
    val end: Long? = null,
    val includeMeta: Boolean,
    val includeMessageActions: Boolean
) :
    Endpoint<FetchMessagesEnvelope, PNFetchMessagesResult>(pubnub) {

    private companion object {
        private const val DEFAULT_MESSAGES = 1
        private const val MAX_MESSAGES = 25
    }

    private val log = LoggerFactory.getLogger("FetchMessages")

    private var maximumPerChannelParam: Int = maximumPerChannel

    override fun validateParams() {
        super.validateParams()
        if (channels.isEmpty()) throw PubNubException(PubNubError.CHANNEL_MISSING)

        if (!includeMessageActions) {
            if (maximumPerChannel !in DEFAULT_MESSAGES..MAX_MESSAGES) {
                when {
                    maximumPerChannel < DEFAULT_MESSAGES -> maximumPerChannelParam =
                        DEFAULT_MESSAGES
                    maximumPerChannel > MAX_MESSAGES -> maximumPerChannelParam = MAX_MESSAGES
                }
                log.info("maximumPerChannel param defaulting to $maximumPerChannelParam")
            }
        } else {
            if (maximumPerChannel !in DEFAULT_MESSAGES..MAX_MESSAGES) {
                maximumPerChannelParam = MAX_MESSAGES
                log.info("maximumPerChannel param defaulting to $maximumPerChannelParam")
            }
        }
    }

    override fun getAffectedChannels() = channels

    override fun doWork(queryParams: HashMap<String, String>): Call<FetchMessagesEnvelope> {
        addQueryParams(queryParams)

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

    override fun createResponse(input: Response<FetchMessagesEnvelope>): PNFetchMessagesResult {
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

        return PNFetchMessagesResult(channels = channelsMap)
    }

    override fun operationType() = PNOperationType.PNFetchMessagesOperation

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
        queryParams["max"] = maximumPerChannelParam.toString()

        start?.run { queryParams["start"] = this.toString().toLowerCase(Locale.US) }
        end?.run { queryParams["end"] = this.toString().toLowerCase(Locale.US) }

        if (includeMeta) queryParams["include_meta"] = includeMeta.toString()
    }
}
