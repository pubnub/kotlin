package com.pubnub.internal

import com.google.gson.JsonElement
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.internal.PNConfiguration.Companion.isValid
import com.pubnub.internal.retry.RetryableBase
import com.pubnub.internal.retry.RetryableCallback
import com.pubnub.internal.retry.RetryableRestCaller
import org.slf4j.LoggerFactory
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.function.Consumer

/**
 * Base class for all PubNub API operation implementations.
 *
 * @param Input Server's response.
 * @param Output Parsed and encapsulated response for endusers.
 * @property pubnub The client instance.
 */
abstract class Endpoint<Input, Output> protected constructor(protected val pubnub: InternalPubNubClient) :
    ExtendedRemoteAction<Output> {

    private val log = LoggerFactory.getLogger(this.javaClass.simpleName)

    internal companion object {
        private const val SERVER_RESPONSE_BAD_REQUEST = 400
        private const val SERVER_RESPONSE_FORBIDDEN = 403
        private const val SERVER_RESPONSE_NOT_FOUND = 404
    }

    private lateinit var cachedCallback: Consumer<Result<Output>>
    private lateinit var call: Call<Input>
    private var silenceFailures = false
    private val retryableRestCaller =
        RetryableRestCaller<Input>(
            pubnub.configuration.retryConfiguration,
            getEndpointGroupName(),
            isEndpointRetryable()
        )

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
    override fun sync(): Output {
        validateParams()
        call = doWork(createBaseParams())
        val response = retryableRestCaller.execute(call)
        return handleResponse(response)
    }

    private fun handleResponse(response: Response<Input>): Output {
        when {
            response.isSuccessful -> {
                return checkAndCreateResponse(response)
            }

            else -> {
                val (errorString, errorJson) = extractErrorBody(response)
                throw createException(response, errorString, errorJson)
            }
        }
    }

    /**
     * Executes the call asynchronously. This function does not block the thread.
     *
     * @param callback The callback to receive the response in.
     */
    override fun async(callback: Consumer<Result<Output>>) {
        cachedCallback = callback

        try {
            validateParams()
            call = doWork(createBaseParams())
        } catch (pubnubException: PubNubException) {
            callback.accept(Result.failure(pubnubException))
            return
        }

        call.enqueue(object : RetryableCallback<Input>(
            call = call,
            retryConfiguration = pubnub.configuration.retryConfiguration,
            endpointGroupName = getEndpointGroupName(),
            isEndpointRetryable = isEndpointRetryable(),
            executorService = pubnub.executorService
        ) {
                override fun onFinalResponse(call: Call<Input>, response: Response<Input>) {
                    when {
                        response.isSuccessful -> {
                            // query params
                            try {
                                Result.success(checkAndCreateResponse(response))
                            } catch (e: PubNubException) {
                                Result.failure(e)
                            }.let { result ->
                                callback.accept(result)
                            }
                        }

                        else -> {
                            val (errorString, errorJson) = extractErrorBody(response)

                            callback.accept(
                                Result.failure(
                                    createException(
                                        response, errorString, errorJson
                                    )
                                )
                            )
                        }
                    }
                }

                override fun onFinalFailure(call: Call<Input>, t: Throwable) {
                    if (silenceFailures)
                        return

                    val error: PubNubError =
                        when (t) {
                            is UnknownHostException, is ConnectException -> {
                                PubNubError.CONNECT_EXCEPTION
                            }

                            is SocketTimeoutException -> {
                                PubNubError.SUBSCRIBE_TIMEOUT
                            }

                            is IOException -> {
                                PubNubError.PARSING_ERROR
                            }

                            is IllegalStateException -> {
                                PubNubError.PARSING_ERROR
                            }

                            else -> {
                                PubNubError.HTTP_ERROR
                            }
                        }

                    val pubnubException = PubNubException(
                        errorMessage = t.toString(),
                        pubnubError = error,
                        cause = t,
                        remoteAction = this@Endpoint
                    )
                    callback.accept(Result.failure(pubnubException))
                }
            })
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

    private fun createException(
        response: Response<Input>,
        errorString: String? = null,
        errorBody: JsonElement? = null
    ): PubNubException {

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

        val affectedChannels =
            errorChannels.ifEmpty {
                try {
                    getAffectedChannels()
                } catch (e: UninitializedPropertyAccessException) {
                    emptyList<String>()
                }
            }

        val affectedChannelGroups =
            errorGroups.ifEmpty {
                try {
                    getAffectedChannelGroups()
                } catch (e: UninitializedPropertyAccessException) {
                    emptyList<String>()
                }
            }

        return PubNubException(
            pubnubError = PubNubError.HTTP_ERROR,
            errorMessage = errorString,
            jso = errorBody?.toString(),
            statusCode = response.code(),
            affectedCall = call,
            retryAfterHeaderValue = response.headers()[RetryableBase.RETRY_AFTER_HEADER_NAME]?.toIntOrNull(),
            affectedChannels = affectedChannels,
            affectedChannelGroups = affectedChannelGroups,
            requestInfo = PubNubException.RequestInfo(
                tlsEnabled = response.raw().request.url.isHttps,
                origin = response.raw().request.url.host,
                uuid = response.raw().request.url.queryParameter("uuid"),
                authKey = response.raw().request.url.queryParameter("auth"),
                clientRequest = response.raw().request,
            ),
            remoteAction = this
        )
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

    private fun checkAndCreateResponse(input: Response<Input>): Output {
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
                jso = pubnub.mapper.toJson(input.body()),
                cause = e
            )
        } catch (e: IllegalStateException) {
            throw PubNubException(
                pubnubError = PubNubError.PARSING_ERROR,
                errorMessage = e.toString(),
                affectedCall = call,
                statusCode = input.code(),
                jso = pubnub.mapper.toJson(input.body()),
                cause = e
            )
        } catch (e: IndexOutOfBoundsException) {
            throw PubNubException(
                pubnubError = PubNubError.PARSING_ERROR,
                errorMessage = e.toString(),
                affectedCall = call,
                statusCode = input.code(),
                jso = pubnub.mapper.toJson(input.body()),
                cause = e
            )
        } catch (e: NullPointerException) {
            throw PubNubException(
                pubnubError = PubNubError.PARSING_ERROR,
                errorMessage = e.toString(),
                affectedCall = call,
                statusCode = input.code(),
                jso = pubnub.mapper.toJson(input.body()),
                cause = e
            )
        } catch (e: IllegalArgumentException) {
            throw PubNubException(
                pubnubError = PubNubError.PARSING_ERROR,
                errorMessage = e.toString(),
                affectedCall = call,
                statusCode = input.code(),
                jso = pubnub.mapper.toJson(input.body()),
                cause = e
            )
        } catch (e: TypeCastException) {
            throw PubNubException(
                pubnubError = PubNubError.PARSING_ERROR,
                errorMessage = e.toString(),
                affectedCall = call,
                statusCode = input.code(),
                jso = pubnub.mapper.toJson(input.body()),
                cause = e
            )
        } catch (e: ClassCastException) {
            throw PubNubException(
                pubnubError = PubNubError.PARSING_ERROR,
                errorMessage = e.toString(),
                affectedCall = call,
                statusCode = input.code(),
                jso = pubnub.mapper.toJson(input.body()),
                cause = e
            )
        } catch (e: UninitializedPropertyAccessException) {
            throw PubNubException(
                pubnubError = PubNubError.PARSING_ERROR,
                errorMessage = e.toString(),
                affectedCall = call,
                statusCode = input.code(),
                jso = pubnub.mapper.toJson(input.body()),
                cause = e
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
    protected abstract fun createResponse(input: Response<Input>): Output

    protected open fun isSubKeyRequired() = true
    protected open fun isPubKeyRequired() = false
    protected open fun isAuthRequired() = true
    protected abstract fun getEndpointGroupName(): RetryableEndpointGroup
    protected open fun isEndpointRetryable(): Boolean = true
}
