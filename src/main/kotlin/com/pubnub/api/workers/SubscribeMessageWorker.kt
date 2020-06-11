package com.pubnub.api.workers

import com.google.gson.JsonElement
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.PubNubUtil
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.managers.DuplicationManager
import com.pubnub.api.managers.ListenerManager
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.ObjectPayload
import com.pubnub.api.models.server.PresenceEnvelope
import com.pubnub.api.models.server.SubscribeMessage
import com.pubnub.api.vendor.Crypto
import org.slf4j.LoggerFactory
import java.util.concurrent.LinkedBlockingQueue

internal class SubscribeMessageWorker(
    val pubnub: PubNub,
    val listenerManager: ListenerManager,
    val queue: LinkedBlockingQueue<SubscribeMessage>,
    val duplicationManager: DuplicationManager
) : Runnable {

    private val log = LoggerFactory.getLogger("SubscribeMessageWorker")

    private var running = false

    companion object {
        private const val TYPE_MESSAGE = 0
        private const val TYPE_SIGNAL = 1
        private const val TYPE_OBJECT = 2
        private const val TYPE_MESSAGE_ACTION = 3
    }

    override fun run() {
        takeMessage()
    }

    private fun takeMessage() {
        running = true

        while (running) {
            try {
                processIncomingPayload(queue.take())
            } catch (e: InterruptedException) {
                running = false
                log.trace("take message interrupted!")
                e.printStackTrace()
            }
        }
    }

    private fun processIncomingPayload(message: SubscribeMessage) {
        if (message.channel == null) {
            return
        }

        val channel = message.channel
        var subscriptionMatch = message.subscriptionMatch
        val publishMetaData = message.publishMetaData

        if (channel == subscriptionMatch) {
            subscriptionMatch = null
        }

        if (pubnub.configuration.dedupOnSubscribe) {
            if (duplicationManager.isDuplicate(message)) {
                return
            } else {
                duplicationManager.addEntry(message)
            }
        }

        if (message.channel.endsWith("-pnpres")) {
            val presencePayload = pubnub.mapper.convertValue(message.payload, PresenceEnvelope::class.java)
            val strippedPresenceChannel = PubNubUtil.replaceLast(channel, "-pnpres", "")
            val strippedPresenceSubscription = subscriptionMatch?.let {
                PubNubUtil.replaceLast(it, "-pnpres", "")
            }

            val isHereNowRefresh = message.payload?.asJsonObject?.get("here_now_refresh")

            listenerManager.announce(
                PNPresenceEventResult(
                    event = presencePayload.action,
                    uuid = presencePayload.uuid,
                    timestamp = presencePayload.timestamp,
                    occupancy = presencePayload.occupancy,
                    state = presencePayload.data,
                    channel = strippedPresenceChannel,
                    subscription = strippedPresenceSubscription,
                    timetoken = publishMetaData?.publishTimetoken,
                    join = getDelta(message.payload?.asJsonObject?.get("join")),
                    leave = getDelta(message.payload?.asJsonObject?.get("leave")),
                    timeout = getDelta(message.payload?.asJsonObject?.get("timeout")),
                    hereNowRefresh = isHereNowRefresh != null && isHereNowRefresh.asBoolean
                )
            )
        } else {
            val extractedMessage = processMessage(message)

            if (extractedMessage == null) {
                log.debug("unable to parse payload on #processIncomingMessages")
            }

            val result = BasePubSubResult(
                channel = channel,
                subscription = subscriptionMatch,
                timetoken = publishMetaData?.publishTimetoken,
                userMetadata = message.userMetadata,
                publisher = message.issuingClientId
            )

            when (message.type) {
                null -> {
                    listenerManager.announce(PNMessageResult(result, extractedMessage!!))
                }
                TYPE_MESSAGE -> {
                    listenerManager.announce(PNMessageResult(result, extractedMessage!!))
                }
                TYPE_SIGNAL -> {
                    listenerManager.announce(PNSignalResult(result, extractedMessage!!))
                }
                TYPE_OBJECT -> {

                }
                TYPE_MESSAGE_ACTION -> {
                    val objectPayload = pubnub.mapper.convertValue(extractedMessage, ObjectPayload::class.java)
                    val data = objectPayload.data.asJsonObject
                    if (!data.has("uuid")) {
                        data.addProperty("uuid", result.publisher)
                    }
                    listenerManager.announce(
                        PNMessageActionResult(
                            result = result,
                            event = objectPayload.event,
                            data = pubnub.mapper.convertValue(data, PNMessageAction::class.java)
                        )
                    )
                }
            }
        }

    }

    private fun processMessage(subscribeMessage: SubscribeMessage): JsonElement? {
        val input = subscribeMessage.payload

        // if we do not have a crypto key, there is no way to process the node; let's return.
        if (!pubnub.configuration.isCipherKeyValid()) {
            return input
        }

        // if the message couldn't possibly be encrypted in the first place, there is no way to process the node;
        // let's return.
        if (!subscribeMessage.supportsEncryption()) {
            return input
        }

        val crypto = Crypto(pubnub.configuration.cipherKey)

        val inputText =
            if (pubnub.mapper.isJsonObject(input!!) && pubnub.mapper.hasField(input, "pn_other")) {
                pubnub.mapper.elementToString(input, "pn_other")
            } else {
                pubnub.mapper.elementToString(input)
            }

        val outputText =
            try {
                crypto.decrypt(inputText!!)
            } catch (e: PubNubException) {
                val pnStatus = PNStatus(
                    PNStatusCategory.PNMalformedResponseCategory,
                    true,
                    PNOperationType.PNSubscribeOperation,
                    e
                )
                listenerManager.announce(pnStatus)
                return null
            }

        var outputObject =
            try {
                pubnub.mapper.fromJson(outputText, JsonElement::class.java)
            } catch (e: PubNubException) {
                val pnStatus = PNStatus(
                    PNStatusCategory.PNMalformedResponseCategory,
                    true,
                    PNOperationType.PNSubscribeOperation,
                    e
                )
                listenerManager.announce(pnStatus)
                return null
            }

        if (pubnub.mapper.isJsonObject(input) && pubnub.mapper.hasField(input, "pn_other")) {
            val objectNode = pubnub.mapper.getAsObject(input)
            pubnub.mapper.putOnObject(objectNode, "pn_other", outputObject)
            outputObject = objectNode
        }

        return outputObject
    }

    private fun getDelta(delta: JsonElement?): List<String> {
        val list = mutableListOf<String>()
        delta?.let {
            it.asJsonArray.forEach { item: JsonElement? ->
                item?.let {
                    list.add(it.asString)
                }
            }
        }
        return list
    }

}