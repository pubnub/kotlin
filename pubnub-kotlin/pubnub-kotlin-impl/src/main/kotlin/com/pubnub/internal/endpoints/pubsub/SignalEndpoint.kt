package com.pubnub.internal.endpoints.pubsub

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.pubsub.Signal
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.logging.getMessageFingerprintInput
import com.pubnub.internal.logging.prepareMessageLogContent
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubImpl.signal]
 */
class SignalEndpoint internal constructor(
    pubnub: PubNubImpl,
    override val channel: String,
    override val message: Any,
    override val customMessageType: String? = null
) : EndpointCore<List<Any>, PNPublishResult>(pubnub), Signal {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    companion object {
        private const val CUSTOM_MESSAGE_TYPE_QUERY_PARAM = "custom_message_type"
    }

    override fun validateParams() {
        super.validateParams()
        if (channel.isBlank()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
    }

    override fun getAffectedChannels() = listOf(channel)

    override fun doWork(queryParams: HashMap<String, String>): Call<List<Any>> {
        // Serialize once; reused for the retrofit signal call and the log helper.
        val plaintextJson: String = pubnub.mapper.toJson(message)

        logSignalApiCall(plaintextJson)

        customMessageType?.let { customMessageTypeNotNull ->
            queryParams[CUSTOM_MESSAGE_TYPE_QUERY_PARAM] = customMessageTypeNotNull
        }

        return retrofitManager.signalService.signal(
            pubKey = configuration.publishKey,
            subKey = configuration.subscribeKey,
            channel = channel,
            message = plaintextJson,
            options = queryParams,
        )
    }

    /**
     * Emits the "Signal API call" debug record. LOGGING ONLY — no side effects, no
     * externally-visible state. Gated on isDebugEnabled() because fingerprint + content
     * prep each walk the full plaintext (non-trivial on the 2 MiB ceiling); skip when
     * no sink will consume the record.
     */
    private fun logSignalApiCall(plaintextJson: String) {
        if (log.isDebugEnabled()) {
            // Server-rule fingerprint input: string-typed values unquoted, everything else as
            // canonical JSON — same rule as subscribe so JsonPrimitive("hello") matches.
            val fingerprintInput: String = getMessageFingerprintInput(message, pubnub.mapper, preComputedJson = plaintextJson)
            val logContent = prepareMessageLogContent(
                plaintext = message,
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
                            "message" to logContent.display,
                            "pn_mfp" to logContent.pnMfp,
                            "totalBytes" to logContent.totalBytes,
                            "customMessageType" to (customMessageType ?: "")
                        ),
                        operation = this::class.simpleName
                    ),
                    details = "Signal API call",
                )
            )
        }
    }

    override fun createResponse(input: Response<List<Any>>): PNPublishResult =
        PNPublishResult(
            timetoken = input.body()!![2].toString().toLong(),
        )

    override fun operationType() = PNOperationType.PNSignalOperation

    override fun isPubKeyRequired() = true

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.PUBLISH
}
