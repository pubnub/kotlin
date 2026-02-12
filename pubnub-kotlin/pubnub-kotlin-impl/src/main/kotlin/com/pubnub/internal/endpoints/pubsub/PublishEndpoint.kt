package com.pubnub.internal.endpoints.pubsub

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.pubsub.Publish
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.api.v2.PNConfiguration.Companion.isValid
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.crypto.encryptString
import com.pubnub.internal.extension.numericString
import com.pubnub.internal.extension.quoted
import com.pubnub.internal.extension.valueString
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubImpl.publish]
 */
class PublishEndpoint internal constructor(
    pubnub: PubNubImpl,
    override val message: Any,
    override val channel: String,
    override val meta: Any? = null,
    override val shouldStore: Boolean? = null,
    override val usePost: Boolean = false,
    override val replicate: Boolean = true,
    override val ttl: Int? = null,
    override val customMessageType: String? = null,
) : EndpointCore<List<Any>, PNPublishResult>(pubnub), Publish {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    companion object {
        internal const val CUSTOM_MESSAGE_TYPE_QUERY_PARAM = "custom_message_type"

        // POST body limit: nginx client_max_body_size = 32k
        internal const val PUBLISH_V1_MAX_POST_BODY_BYTES = 32_768

        // GET path limit: nginx large_client_header_buffers = 32k
        // Request line: "GET {path} HTTP/1.1\r\n" has 15 bytes overhead
        // Maximum path size: 32,768 - 16 = 32,752 bytes
        // (nginx limit is exclusive, so we subtract 16 instead of 15)
        internal const val PUBLISH_V1_MAX_GET_PATH_BYTES = 32_752

        // Signature overhead added by SignatureInterceptor when secret key is configured:
        // - "&timestamp=XXXXXXXXXX" = 21 bytes (11 + 10 digit Unix timestamp in seconds)
        // - "&signature=v2.XXX..." = 57 bytes (11 + 3 + 43 base64 HMAC-SHA256)
        // Total: 78 bytes
        internal const val SIGNATURE_QUERY_PARAMS_OVERHEAD_BYTES = 78
    }

    override fun validateParams() {
        super.validateParams()
        if (channel.isBlank()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
    }

    override fun getAffectedChannels() = listOf(channel)

    override fun doWork(queryParams: HashMap<String, String>): Call<List<Any>> {
//        log.debug(
//            LogMessage(
//                message = LogMessageContent.Object(
//                    arguments = mapOf(
//                        "message" to message,
//                        "channel" to channel,
//                        "shouldStore" to (shouldStore ?: true),
//                        "meta" to (meta ?: ""),
//                        "usePost" to usePost,
//                        "ttl" to (ttl ?: 0),
//                        "replicate" to replicate,
//                        "customMessageType" to (customMessageType ?: "")
//                    ),
//                    operation = this::class.simpleName
//                ),
//                details = "Publish API call"
//            )
//        )

        addQueryParams(queryParams)

        return if (usePost) {
            val payload = getBodyMessage(message)
            val bodySizeBytes = calculateV1PostBodySizeBytes(payload)

            if (bodySizeBytes > PUBLISH_V1_MAX_POST_BODY_BYTES) {
                // Too large for publish v1 → use publish v2 (POST)
                log.debug(
                    LogMessage(
                        message = LogMessageContent.Text(
                            "POST body size ($bodySizeBytes bytes) exceeds publish v1 limit ($PUBLISH_V1_MAX_POST_BODY_BYTES bytes). " +
                                "Using V2 POST endpoint."
                        )
                    )
                )
                retrofitManager.publishService.publishWithPostV2(
                    configuration.publishKey,
                    configuration.subscribeKey,
                    channel,
                    payload,
                    queryParams,
                )
            } else {
                // Body fits in V1 POST
                retrofitManager.publishService.publishWithPost(
                    configuration.publishKey,
                    configuration.subscribeKey,
                    channel,
                    payload,
                    queryParams,
                )
            }
        } else {
            // === GET PATH ===
            val stringifiedMessage = getParamMessage(message)
            val v1GetCall = retrofitManager.publishService.publish(
                configuration.publishKey,
                configuration.subscribeKey,
                channel,
                stringifiedMessage,
                queryParams,
            )
            val pathLengthBytes = calculateV1GetPathLengthBytes(v1GetCall)

            if (pathLengthBytes > PUBLISH_V1_MAX_GET_PATH_BYTES) {
                // Too large for publish v1 → use publish v2 (POST), ignore usePost=false
                log.debug(
                    LogMessage(
                        message = LogMessageContent.Text(
                            "GET path length ($pathLengthBytes bytes) exceeds publish v1 limit ($PUBLISH_V1_MAX_GET_PATH_BYTES bytes). " +
                                "Ignoring usePost=false and using V2 POST endpoint."
                        )
                    )
                )
                val payload = getBodyMessage(message)
                retrofitManager.publishService.publishWithPostV2(
                    configuration.publishKey,
                    configuration.subscribeKey,
                    channel,
                    payload,
                    queryParams,
                )
            } else {
                // Path fits in GET
                v1GetCall
            }
        }
    }

    override fun createResponse(input: Response<List<Any>>): PNPublishResult =
        PNPublishResult(
            timetoken = input.body()!![2].toString().toLong(),
        )

    override fun operationType() = PNOperationType.PNPublishOperation

    override fun isPubKeyRequired() = true

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.PUBLISH

    // region Parameters

    /**
     * Add query params to passed HashMap
     *
     * @param queryParams hashMap to add parameters
     */
    private fun addQueryParams(queryParams: MutableMap<String, String>) {
        meta?.let { queryParams["meta"] = pubnub.mapper.toJson(it) }
        shouldStore?.let { queryParams["store"] = it.numericString }
        ttl?.let { queryParams["ttl"] = it.toString() }
        if (!replicate) {
            queryParams["norep"] = true.valueString
        }
        customMessageType?.let { queryParams[CUSTOM_MESSAGE_TYPE_QUERY_PARAM] = it }
        queryParams["seqn"] = pubnub.publishSequenceManager.nextSequence().toString()
    }
    // endregion

    // region Message parsers
    private fun getBodyMessage(message: Any): Any = pubnub.cryptoModuleWithLogConfig?.encryptString(toJson(message)) ?: message

    private fun getParamMessage(message: Any): String =
        pubnub.cryptoModuleWithLogConfig?.encryptString(toJson(message))?.quoted() ?: toJson(message)

    private fun toJson(message: Any): String = pubnub.mapper.toJson(message)

    /**
     * Calculate publish v1 POST request body size by serializing with the same mapper
     * Retrofit uses. This matches JSON escaping/quoting behavior (including Strings).
     */
    private fun calculateV1PostBodySizeBytes(payload: Any): Int {
        return pubnub.mapper.toJson(payload).toByteArray(Charsets.UTF_8).size
    }

    /**
     * Calculate the path+query length by asking Retrofit to build the actual request.
     * This measures what appears in the HTTP request line: "GET {path}?{query} HTTP/1.1\r\n"
     * We only measure path+query, not the full URL (scheme/host are not part of the request line).
     *
     * Note: SignatureInterceptor adds &timestamp and &signature query params AFTER this calculation,
     * so we must account for that overhead when a secret key is configured.
     *
     * @return total length in bytes including signature overhead
     */
    private fun calculateV1GetPathLengthBytes(v1GetCall: Call<List<Any>>): Int {
        val url = v1GetCall.request().url
        // Path + query string (what appears in the HTTP request line)
        // URL is ASCII (percent-encoded), so String length matches byte length.
        val pathAndQuery = url.encodedPath + (url.encodedQuery?.let { "?$it" } ?: "")

        // Account for signature overhead added by SignatureInterceptor
        val signatureOverhead = if (configuration.secretKey.isValid()) {
            SIGNATURE_QUERY_PARAMS_OVERHEAD_BYTES
        } else {
            0
        }

        return pathAndQuery.length + signatureOverhead
    }
}
