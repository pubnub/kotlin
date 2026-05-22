package com.pubnub.internal.endpoints.files

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.files.PublishFileMessage
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.files.PNBaseFile
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.crypto.encryptString
import com.pubnub.internal.endpoints.pubsub.PublishEndpoint.Companion.CUSTOM_MESSAGE_TYPE_QUERY_PARAM
import com.pubnub.internal.extension.numericString
import com.pubnub.internal.extension.quoted
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.logging.getMessageFingerprintInput
import com.pubnub.internal.logging.prepareMessageLogContent
import com.pubnub.internal.models.server.files.FileUploadNotification
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubImpl.publishFileMessage]
 */
open class PublishFileMessageEndpoint(
    private val channel: String,
    fileName: String,
    fileId: String,
    private val message: Any? = null,
    private val meta: Any? = null,
    private val ttl: Int? = null,
    private val shouldStore: Boolean? = null,
    private val customMessageType: String? = null,
    pubNub: PubNubImpl,
) : EndpointCore<List<Any>, PNPublishFileMessageResult>(pubNub), PublishFileMessage {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)
    private val pnFile = PNBaseFile(fileId, fileName)

    @Throws(PubNubException::class)
    override fun validateParams() {
        if (channel.isEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
    }

    @Throws(PubNubException::class)
    override fun doWork(queryParams: HashMap<String, String>): Call<List<Any>> {
        // The wire payload is the FileUploadNotification envelope (file metadata + user message).
        // Serialize once, encrypt-once for crypto-on, and reuse the same string for both the log
        // fingerprint and the retrofit call so pn_mfp covers exactly what is sent.
        val notification = FileUploadNotification(message, pnFile)
        val plaintextJson: String = pubnub.mapper.toJson(notification)
        val encryptedString: String? = pubnub.cryptoModuleWithLogConfig?.encryptString(plaintextJson)

        logPublishFileMessageApiCall(notification, plaintextJson, encryptedString)

        val messageAsString = encryptedString?.quoted() ?: plaintextJson
        meta?.let {
            val stringifiedMeta: String = pubnub.mapper.toJson(it)
            queryParams["meta"] = stringifiedMeta
        }
        shouldStore?.numericString?.let { queryParams["store"] = it }
        ttl?.let { queryParams["ttl"] = it.toString() }
        customMessageType?.let { queryParams[CUSTOM_MESSAGE_TYPE_QUERY_PARAM] = it }

        return retrofitManager.filesService.notifyAboutFileUpload(
            configuration.publishKey,
            configuration.subscribeKey,
            channel,
            messageAsString,
            queryParams,
        )
    }

    @Throws(PubNubException::class)
    override fun createResponse(input: Response<List<Any>>): PNPublishFileMessageResult {
        return input.body()?.let { body ->
            val timetoken = body[2].toString().toLong()
            PNPublishFileMessageResult(timetoken)
        } ?: throw PubNubException(PubNubError.INTERNAL_ERROR)
    }

    override fun getAffectedChannels() = listOf(channel)

    override fun getAffectedChannelGroups(): List<String> = listOf()

    override fun operationType(): PNOperationType = PNOperationType.FileOperation

    override fun isAuthRequired(): Boolean = true

    override fun isSubKeyRequired(): Boolean = true

    override fun isPubKeyRequired(): Boolean = true

    /**
     * Emits the "PublishFileMessage API call" debug record. LOGGING ONLY — no side effects, no
     * externally-visible state. Gated on isDebugEnabled() because fingerprint + content prep
     * each walk the full plaintext (non-trivial on the 2 MiB ceiling); skip when no sink will
     * consume the record.
     */
    private fun logPublishFileMessageApiCall(
        notification: FileUploadNotification,
        plaintextJson: String,
        encryptedString: String?,
    ) {
        if (log.isDebugEnabled()) {
            val fingerprintInput: String = encryptedString
                ?: getMessageFingerprintInput(notification, pubnub.mapper, preComputedJson = plaintextJson)

            val logContent = prepareMessageLogContent(
                plaintext = notification,
                cap = pubnub.configuration.logContentConfig.loggedMessageContentMaxBytes,
                mapper = pubnub.mapper,
                fingerprintInput = fingerprintInput,
                preComputedPlaintextJson = plaintextJson,
            )

            log.debug(
                LogMessage(
                    message = LogMessageContent.Object(
                        arguments = mapOf(
                            "channel" to channel,
                            "fileName" to pnFile.name,
                            "fileId" to pnFile.id,
                            "message" to logContent.display,
                            "pn_mfp" to logContent.pnMfp,
                            "totalBytes" to logContent.totalBytes,
                            "meta" to (meta ?: ""),
                            "ttl" to (ttl ?: 0),
                            "shouldStore" to (shouldStore ?: true),
                            "customMessageType" to (customMessageType ?: "")
                        ),
                        operation = this::class.simpleName
                    ),
                    details = "PublishFileMessage API call",
                )
            )
        }
    }

    internal class Factory(private val pubNub: PubNubImpl) {
        fun create(
            channel: String,
            fileName: String,
            fileId: String,
            message: Any? = null,
            meta: Any? = null,
            ttl: Int? = null,
            shouldStore: Boolean? = null,
            customMessageType: String? = null
        ): ExtendedRemoteAction<PNPublishFileMessageResult> {
            return PublishFileMessageEndpoint(
                channel = channel,
                fileName = fileName,
                fileId = fileId,
                message = message,
                meta = meta,
                ttl = ttl,
                shouldStore = shouldStore,
                customMessageType = customMessageType,
                pubNub = pubNub,
            )
        }
    }

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.PUBLISH
}
