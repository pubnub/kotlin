package com.pubnub.api

import com.google.gson.JsonElement
import com.pubnub.api.PNConfiguration.Companion.isValid
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.enums.PNStatusCategory.PNAccessDeniedCategory
import com.pubnub.api.enums.PNStatusCategory.PNAcknowledgmentCategory
import com.pubnub.api.enums.PNStatusCategory.PNBadRequestCategory
import com.pubnub.api.enums.PNStatusCategory.PNCancelledCategory
import com.pubnub.api.enums.PNStatusCategory.PNMalformedResponseCategory
import com.pubnub.api.enums.PNStatusCategory.PNNotFoundCategory
import com.pubnub.api.enums.PNStatusCategory.PNTimeoutCategory
import com.pubnub.api.enums.PNStatusCategory.PNUnexpectedDisconnectCategory
import com.pubnub.api.enums.PNStatusCategory.PNUnknownCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.policies.RequestRetryPolicy
import com.pubnub.api.policies.RetryableEndpointName
import okhttp3.ResponseBody.Companion.toResponseBody
import org.slf4j.LoggerFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.random.Random

private const val NUMBER_OF_REGULAR_CALLS = 1

/**
 * Base class for all PubNub API operation implementations.
 *
 * @param Input Server's response.
 * @param Output Parsed and encapsulated response for endusers.
 * @property pubnub The client instance.
 */
abstract class Endpoint<Input, Output> protected constructor(protected val pubnub: PubNub) :
    ExtendedRemoteAction<Output> {

    private val log = LoggerFactory.getLogger(this.javaClass.simpleName)

    private companion object {
        private const val SERVER_RESPONSE_BAD_REQUEST = 400
        private const val SERVER_RESPONSE_FORBIDDEN = 403
        private const val SERVER_RESPONSE_NOT_FOUND = 404
    }

    private lateinit var cachedCallback: (result: Output?, status: PNStatus) -> Unit
    internal lateinit var call: Call<Input>
    private var silenceFailures = false

    /**
     * Key-value object to pass with every PubNub API operation. Used for debugging purposes.
     * todo: it should be removed!
     */
    val queryParam: MutableMap<String, String> = mutableMapOf()

    /**
     * Executes the call synchronously. This function blocks the thread.
     *
     * @return A parsed and encapsulated response if the request has been successful, `null` otherwise.
     *
     * @throws [PubNubException] if anything goes wrong with the request.
     */
    override fun sync(): Output? {
        validateParams()
        val response = executeRestCallWithRetryPolicy()
        return handleResponse(response)
    }

    private fun executeRestCallWithRetryPolicy(): Response<Input> {
        val retryPolicy: RequestRetryPolicy = pubnub.configuration.newRetryPolicy
        var numberOfAttempts = 0
        val maxRetryNumber = getRetryCount(retryPolicy)
        var response: Response<Input> = Response.error(501, "".toResponseBody())
        call = doWork(createBaseParams())
        while (numberOfAttempts < NUMBER_OF_REGULAR_CALLS + maxRetryNumber) {
            response = executeRestCall(call)

            if (shouldRetry(response, retryPolicy)) {
                val effectiveDelay = getEffectiveDelay(retryPolicy)
                Thread.sleep((effectiveDelay * 1000L).toLong())
                call = call.clone()
                numberOfAttempts++
            } else {
                break
            }
        }
        return response
    }

    private fun getEffectiveDelay(retryPolicy: RequestRetryPolicy): Double {
        val randomDelay = Random.nextDouble(0.0, 2.0)
        val delayFromRetryPolicy = getDelay(retryPolicy)
        val effectiveDelay = (delayFromRetryPolicy + randomDelay).roundTo3DecimalPlaces()
        log.trace("Added random delay so effective retry delay is $effectiveDelay")
        return effectiveDelay
    }

    private fun Double.roundTo3DecimalPlaces() = "%.3f".format(this).toDouble()

    private fun shouldRetry(response: Response<Input>, retryPolicy: RequestRetryPolicy) =
        !response.isSuccessful && response.raw().code == 404 && isEndpointRetryable() && isRetryPolicySetForThisRestCall(
            retryPolicy
        ) // todo change to 429

    private fun isRetryPolicySetForThisRestCall(retryPolicy: RequestRetryPolicy): Boolean {
        return when (val retryPolicy = retryPolicy) {
            is RequestRetryPolicy.None -> false
            is RequestRetryPolicy.Linear -> {
                val excludedOperations = retryPolicy.excludedOperations
                theEndpointIsNotExcludedFromRetryPolicy(excludedOperations) ?: false
            }
            is RequestRetryPolicy.Exponential -> {
                val excludedOperations = retryPolicy.excludedOperations
                theEndpointIsNotExcludedFromRetryPolicy(excludedOperations) ?: false
            }
        }
    }

    private fun theEndpointIsNotExcludedFromRetryPolicy(excludedOperations: List<RetryableEndpointName>?) =
        excludedOperations?.contains(getEndpointName())?.not()

    private fun getDelay(retryPolicy: RequestRetryPolicy): Double {
        return when (retryPolicy) {
            is RequestRetryPolicy.None -> 0.0
            is RequestRetryPolicy.Linear -> retryPolicy.delay
            is RequestRetryPolicy.Exponential -> retryPolicy.maxDelayInSec.toDouble() // todo: change it
        }
    }

    private fun getRetryCount(retryPolicy: RequestRetryPolicy): Int {
        return when (retryPolicy) {
            is RequestRetryPolicy.None -> 0
            is RequestRetryPolicy.Linear -> retryPolicy.maxRetryNumber
            is RequestRetryPolicy.Exponential -> retryPolicy.maxRetryNumber
        }
    }

    private fun executeRestCall(call: Call<Input>) = try {
        call.execute()
    } catch (e: Exception) {
        throw PubNubException(
            pubnubError = PubNubError.PARSING_ERROR,
            errorMessage = e.toString(),
            affectedCall = call
        )
    }

    private fun handleResponse(response: Response<Input>): Output? {
        when {
            response.isSuccessful -> {
                storeRequestLatency(response)
                return checkAndCreateResponse(response)
            }
            else -> {
                val (errorString, errorJson) = extractErrorBody(response)
                throw PubNubException(
                    pubnubError = PubNubError.HTTP_ERROR,
                    errorMessage = errorString,
                    jso = errorJson.toString(),
                    statusCode = response.code(),
                    affectedCall = call
                )
            }
        }
    }

    /**
     * Executes the call asynchronously. This function does not block the thread.
     *
     * @param callback The callback to receive the response in.
     */
    override fun async(callback: (result: Output?, status: PNStatus) -> Unit) {
        cachedCallback = callback

        try {
            validateParams()
            call = doWork(createBaseParams())
        } catch (pubnubException: PubNubException) {
            callback.invoke(
                null,
                createStatusResponse(
                    category = PNBadRequestCategory,
                    exception = pubnubException
                )
            )
            return
        }

        call.enqueue(object : Callback<Input> { // nie blokować na czas wywołąnia

            override fun onResponse(call: Call<Input>, response: Response<Input>) { // szybkie

                when {
                    response.isSuccessful -> {
                        // query params
                        storeRequestLatency(response)
                        try {
                            Triple(PNAcknowledgmentCategory, checkAndCreateResponse(response), null)
                        } catch (e: PubNubException) {
                            Triple(PNMalformedResponseCategory, null, e)
                        }.let {
                            callback.invoke(
                                it.second,
                                createStatusResponse(
                                    category = it.first,
                                    response = response,
                                    exception = it.third
                                )
                            )
                        }
                    }
                    else -> {
                        val (errorString, errorJson) = extractErrorBody(response)

                        val exception = PubNubException(
                            pubnubError = PubNubError.HTTP_ERROR,
                            errorMessage = errorString,
                            jso = errorJson.toString(),
                            statusCode = response.code(),
                            affectedCall = call
                        )

                        val pnStatusCategory = when (response.code()) {
                            SERVER_RESPONSE_FORBIDDEN -> PNAccessDeniedCategory
                            SERVER_RESPONSE_BAD_REQUEST -> PNBadRequestCategory
                            SERVER_RESPONSE_NOT_FOUND -> PNNotFoundCategory
                            else -> PNUnknownCategory
                        }

                        callback.invoke(
                            null,
                            createStatusResponse(
                                category = pnStatusCategory,
                                response = response,
                                exception = exception,
                                errorBody = errorJson
                            )
                        )
                        return
                    }
                }
            }

            override fun onFailure(call: Call<Input>, t: Throwable) {

                if (silenceFailures)
                    return

                val (error: PubNubError, category: PNStatusCategory) =
                    when (t) {
                        is UnknownHostException -> {
                            PubNubError.CONNECTION_NOT_SET to PNUnexpectedDisconnectCategory
                        }
                        is ConnectException -> {
                            PubNubError.CONNECT_EXCEPTION to PNUnexpectedDisconnectCategory
                        }
                        is SocketTimeoutException -> {
                            PubNubError.SUBSCRIBE_TIMEOUT to PNTimeoutCategory
                        }
                        is IOException -> {
                            PubNubError.PARSING_ERROR to PNMalformedResponseCategory
                        }
                        is IllegalStateException -> {
                            PubNubError.PARSING_ERROR to PNMalformedResponseCategory
                        }
                        else -> {
                            PubNubError.HTTP_ERROR to if (call.isCanceled) PNCancelledCategory else PNBadRequestCategory
                        }
                    }

                val pubnubException = PubNubException(errorMessage = t.toString(), pubnubError = error)
                callback.invoke(
                    null,
                    createStatusResponse(
                        category = category,
                        exception = pubnubException
                    )
                )
            }
        })
    }

    private fun storeRequestLatency(response: Response<Input>) {
        pubnub.telemetryManager.storeLatency(
            latency = with(response.raw()) {
                receivedResponseAtMillis - sentRequestAtMillis
            },
            type = operationType()
        )
    }

    protected fun createBaseParams(): HashMap<String, String> {
        val map = hashMapOf<String, String>()

        map += queryParam

        map["pnsdk"] = pubnub.configuration.generatePnsdk(pubnub.version)
        map["uuid"] = pubnub.configuration.userId.value

        if (pubnub.configuration.includeInstanceIdentifier) {
            map["instanceid"] = pubnub.instanceId
        }

        if (pubnub.configuration.includeRequestIdentifier) {
            map["requestid"] = pubnub.requestId()
        }

        if (isAuthRequired()) {
            val token = pubnub.tokenManager.getToken()
            if (token != null) {
                map["auth"] = token
            } else if (pubnub.configuration.authKey.isValid()) {
                map["auth"] = pubnub.configuration.authKey
            }
        }

        map.putAll(pubnub.telemetryManager.operationsLatency())
        return map
    }

    /**
     * Cancel the operation but do not alert anybody, useful for restarting the heartbeats and subscribe loops.
     */
    override fun silentCancel() {
        if (::call.isInitialized) {
            if (!call.isCanceled) {
                silenceFailures = true
                call.cancel()
            }
        }
    }

    private fun createStatusResponse(
        category: PNStatusCategory,
        response: Response<Input>? = null,
        exception: PubNubException? = null,
        errorBody: JsonElement? = null
    ): PNStatus {

        val pnStatus = PNStatus(
            category = category,
            error = response == null || exception != null,
            operation = operationType(),
            exception = exception
        )

        pnStatus.executedEndpoint = this

        response?.let {

            with(pnStatus) {
                statusCode = it.code()
                tlsEnabled = it.raw().request.url.isHttps
                origin = it.raw().request.url.host
                uuid = it.raw().request.url.queryParameter("uuid")
                authKey = it.raw().request.url.queryParameter("auth")
                clientRequest = it.raw().request
            }
        }

        val errorChannels = mutableListOf<String>()
        val errorGroups = mutableListOf<String>()

        if (errorBody != null) {
            if (pubnub.mapper.isJsonObject(errorBody) && pubnub.mapper.hasField(
                    errorBody,
                    "payload"
                )
            ) {

                val payloadBody = pubnub.mapper.getField(errorBody, "payload")!!

                if (pubnub.mapper.hasField(payloadBody, "channels")) {
                    val iterator = pubnub.mapper.getArrayIterator(payloadBody, "channels")
                    while (iterator.hasNext()) {
                        errorChannels.add(pubnub.mapper.elementToString(iterator.next())!!)
                    }
                }

                if (pubnub.mapper.hasField(payloadBody, "channel-groups")) {
                    val iterator = pubnub.mapper.getArrayIterator(payloadBody, "channel-groups")
                    while (iterator.hasNext()) {
                        val node = iterator.next()

                        val channelGroupName = pubnub.mapper.elementToString(node)!!.let {
                            if (it.first().toString() == ":") {
                                it.substring(1)
                            } else {
                                it
                            }
                        }

                        errorGroups.add(channelGroupName)
                    }
                }
            }
        }

        pnStatus.affectedChannels =
            if (errorChannels.isNotEmpty()) {
                errorChannels
            } else {
                try {
                    getAffectedChannels()
                } catch (e: UninitializedPropertyAccessException) {
                    emptyList<String>()
                }
            }

        pnStatus.affectedChannelGroups =
            if (errorGroups.isNotEmpty()) {
                errorGroups
            } else {
                try {
                    getAffectedChannelGroups()
                } catch (e: UninitializedPropertyAccessException) {
                    emptyList<String>()
                }
            }

        return pnStatus
    }

    override fun retry() {
        silenceFailures = false
        async(cachedCallback)
    }

    private fun extractErrorBody(response: Response<Input>): Pair<String?, JsonElement?> {
        val errorBodyString = try {
            response.errorBody()?.string()
        } catch (e: IOException) {
            "N/A"
        }

        val errorBodyJson = try {
            pubnub.mapper.fromJson(errorBodyString, JsonElement::class.java)
        } catch (e: PubNubException) {
            null
        }

        return errorBodyString to errorBodyJson
    }

    private fun checkAndCreateResponse(input: Response<Input>): Output? {
        try {
            return createResponse(input)
        } catch (pubnubException: PubNubException) {
            throw pubnubException.copy(
                statusCode = input.code(),
                jso = pubnub.mapper.toJson(input.body()),
                affectedCall = call
            )
        } catch (e: KotlinNullPointerException) {
            throw PubNubException(
                pubnubError = PubNubError.PARSING_ERROR,
                errorMessage = e.toString(),
                affectedCall = call,
                statusCode = input.code(),
                jso = pubnub.mapper.toJson(input.body())
            )
        } catch (e: IllegalStateException) {
            throw PubNubException(
                pubnubError = PubNubError.PARSING_ERROR,
                errorMessage = e.toString(),
                affectedCall = call,
                statusCode = input.code(),
                jso = pubnub.mapper.toJson(input.body())
            )
        } catch (e: IndexOutOfBoundsException) {
            throw PubNubException(
                pubnubError = PubNubError.PARSING_ERROR,
                errorMessage = e.toString(),
                affectedCall = call,
                statusCode = input.code(),
                jso = pubnub.mapper.toJson(input.body())
            )
        } catch (e: NullPointerException) {
            throw PubNubException(
                pubnubError = PubNubError.PARSING_ERROR,
                errorMessage = e.toString(),
                affectedCall = call,
                statusCode = input.code(),
                jso = pubnub.mapper.toJson(input.body())
            )
        } catch (e: IllegalArgumentException) {
            throw PubNubException(
                pubnubError = PubNubError.PARSING_ERROR,
                errorMessage = e.toString(),
                affectedCall = call,
                statusCode = input.code(),
                jso = pubnub.mapper.toJson(input.body())
            )
        } catch (e: TypeCastException) {
            throw PubNubException(
                pubnubError = PubNubError.PARSING_ERROR,
                errorMessage = e.toString(),
                affectedCall = call,
                statusCode = input.code(),
                jso = pubnub.mapper.toJson(input.body())
            )
        } catch (e: ClassCastException) {
            throw PubNubException(
                pubnubError = PubNubError.PARSING_ERROR,
                errorMessage = e.toString(),
                affectedCall = call,
                statusCode = input.code(),
                jso = pubnub.mapper.toJson(input.body())
            )
        } catch (e: UninitializedPropertyAccessException) {
            throw PubNubException(
                pubnubError = PubNubError.PARSING_ERROR,
                errorMessage = e.toString(),
                affectedCall = call,
                statusCode = input.code(),
                jso = pubnub.mapper.toJson(input.body())
            )
        }
    }

    protected open fun getAffectedChannels() = emptyList<String>()
    protected open fun getAffectedChannelGroups(): List<String> = emptyList()

    protected open fun validateParams() {
        if (isSubKeyRequired() && !pubnub.configuration.subscribeKey.isValid()) {
            throw PubNubException(PubNubError.SUBSCRIBE_KEY_MISSING)
        }
        if (isPubKeyRequired() && !pubnub.configuration.publishKey.isValid()) {
            throw PubNubException(PubNubError.PUBLISH_KEY_MISSING)
        }
    }

    protected abstract fun doWork(queryParams: HashMap<String, String>): Call<Input>
    protected abstract fun createResponse(input: Response<Input>): Output?

    protected open fun isSubKeyRequired() = true
    protected open fun isPubKeyRequired() = false
    protected open fun isAuthRequired() = true
    protected open fun getEndpointName(): RetryableEndpointName? = null
    protected open fun isEndpointRetryable(): Boolean = false
}
