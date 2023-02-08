package com.pubnub.api.workers

import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PNConfiguration.Companion.isValid
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubUtil
import com.pubnub.api.managers.DuplicationManager
import com.pubnub.api.models.consumer.MessageType
import com.pubnub.api.models.consumer.files.PNDownloadableFile
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.ObjectPayload
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventMessage
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.models.server.PresenceEnvelope
import com.pubnub.api.models.server.SubscribeMessage
import com.pubnub.api.models.server.files.FileUploadNotification
import com.pubnub.api.services.FilesService
import com.pubnub.api.vendor.Crypto
import org.slf4j.LoggerFactory

internal class SubscribeMessageProcessor(
    private val pubnub: PubNub,
    private val duplicationManager: DuplicationManager
) {

    companion object {
        private const val PRESENCE_SUFFIX = "-pnpres"
        private const val PN_OTHER_FIELD = "pn_other"
    }

    private val log = LoggerFactory.getLogger("SubscribeMessageProcessor")

    fun processIncomingPayload(message: SubscribeMessage): PNEvent? {
        if (message.channel == null) {
            return null
        }

        val channel = message.channel
        var subscriptionMatch = message.subscriptionMatch
        val publishMetaData = message.publishMetaData

        if (channel == subscriptionMatch) {
            subscriptionMatch = null
        }

        if (pubnub.configuration.dedupOnSubscribe) {
            if (duplicationManager.isDuplicate(message)) {
                return null
            } else {
                duplicationManager.addEntry(message)
            }
        }

        if (message.channel.endsWith(PRESENCE_SUFFIX)) {
            val presencePayload = pubnub.mapper.convertValue(message.payload, PresenceEnvelope::class.java)
            val strippedPresenceChannel = PubNubUtil.replaceLast(channel, PRESENCE_SUFFIX, "")
            val strippedPresenceSubscription = subscriptionMatch?.let {
                PubNubUtil.replaceLast(it, PRESENCE_SUFFIX, "")
            }

            val isHereNowRefresh = message.payload?.asJsonObject?.get("here_now_refresh")

            return PNPresenceEventResult(
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

            return when (message.pnMessageType) {
                is MessageType.Message -> {
                    PNMessageResult(
                        basePubSubResult = result,
                        message = extractedMessage!!,
                        spaceId = message.spaceId,
                        messageType = message.userMessageType ?: message.pnMessageType
                    )
                }

                is MessageType.Signal -> {
                    PNSignalResult(
                        basePubSubResult = result,
                        message = extractedMessage!!,
                        spaceId = message.spaceId,
                        messageType = message.userMessageType ?: message.pnMessageType
                    )
                }

                is MessageType.Object -> {
                    PNObjectEventResult(
                        result,
                        pubnub.mapper.convertValue(
                            extractedMessage, PNObjectEventMessage::class.java
                        )
                    )
                }

                is MessageType.MessageAction -> {
                    val objectPayload = pubnub.mapper.convertValue(extractedMessage, ObjectPayload::class.java)
                    val data = objectPayload.data.asJsonObject
                    if (!data.has("uuid")) {
                        data.addProperty("uuid", result.publisher)
                    }
                    PNMessageActionResult(
                        result = result,
                        event = objectPayload.event,
                        data = pubnub.mapper.convertValue(data, PNMessageAction::class.java)
                    )
                }

                is MessageType.File -> {
                    val fileUploadNotification = pubnub.mapper.convertValue(
                        extractedMessage, FileUploadNotification::class.java
                    )
                    PNFileEventResult(
                        channel = message.channel,
                        message = fileUploadNotification.message,
                        file = PNDownloadableFile(
                            id = fileUploadNotification.file.id,
                            name = fileUploadNotification.file.name,
                            url = buildFileUrl(
                                message.channel, fileUploadNotification.file.id, fileUploadNotification.file.name
                            )
                        ),
                        publisher = message.issuingClientId,
                        timetoken = result.timetoken,
                        jsonMessage = fileUploadNotification.message?.let { pubnub.mapper.toJsonTree(it) }
                            ?: JsonNull.INSTANCE,
                        spaceId = message.spaceId,
                        messageType = message.userMessageType ?: message.pnMessageType,
                        userMetadata = message.userMetadata
                    )
                }
            }
        }
    }

    private val formatFriendlyGetFileUrl = "%s" + FilesService.GET_FILE_URL.replace("\\{.*?\\}".toRegex(), "%s")

    private fun buildFileUrl(channel: String, fileId: String, fileName: String): String {
        val basePath: String = java.lang.String.format(
            formatFriendlyGetFileUrl, pubnub.baseUrl(), pubnub.configuration.subscribeKey, channel, fileId, fileName
        )
        val queryParams = ArrayList<String>()
        val authKey = if (pubnub.configuration.authKey.isValid()) pubnub.configuration.authKey else null

        if (PubNubUtil.shouldSignRequest(pubnub.configuration)) {
            val timestamp: Int = pubnub.timestamp()
            val signature: String = generateSignature(pubnub.configuration, basePath, authKey, timestamp)
            queryParams.add(PubNubUtil.TIMESTAMP_QUERY_PARAM_NAME + "=" + timestamp)
            queryParams.add(PubNubUtil.SIGNATURE_QUERY_PARAM_NAME + "=" + signature)
        }

        authKey?.run { queryParams.add(PubNubUtil.AUTH_QUERY_PARAM_NAME + "=" + authKey) }

        return if (queryParams.isEmpty()) {
            basePath
        } else {
            "$basePath?${queryParams.joinToString(separator = "&")}"
        }
    }

    private fun generateSignature(
        configuration: PNConfiguration,
        url: String,
        authKey: String?,
        timestamp: Int
    ): String {
        val queryParams = mutableMapOf<String, String>()
        if (authKey != null) {
            queryParams["auth"] = authKey
        }
        return PubNubUtil.generateSignature(
            configuration, url, queryParams, "get", null, timestamp
        )
    }

    private fun processMessage(subscribeMessage: SubscribeMessage): JsonElement? {
        val input = subscribeMessage.payload

        // if we do not have a crypto key, there is no way to process the node; let's return.
        if (!pubnub.configuration.cipherKey.isValid()) {
            return input
        }

        // if the message couldn't possibly be encrypted in the first place, there is no way to process the node;
        // let's return.
        if (!subscribeMessage.supportsEncryption()) {
            return input
        }

        val crypto = Crypto(
            pubnub.configuration.cipherKey, pubnub.configuration.useRandomInitializationVector
        )

        val inputText = if (pubnub.mapper.isJsonObject(input!!) && pubnub.mapper.hasField(input, PN_OTHER_FIELD)) {
            pubnub.mapper.elementToString(input, PN_OTHER_FIELD)
        } else {
            pubnub.mapper.elementToString(input)
        }

        val outputText = crypto.decrypt(inputText!!)
        var outputObject = pubnub.mapper.fromJson(outputText, JsonElement::class.java)

        if (pubnub.mapper.isJsonObject(input) && pubnub.mapper.hasField(input, PN_OTHER_FIELD)) {
            val objectNode = pubnub.mapper.getAsObject(input)
            pubnub.mapper.putOnObject(objectNode, PN_OTHER_FIELD, outputObject)
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
