package com.pubnub.api

import com.google.gson.JsonElement
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.enums.PNStatusCategory.*
import com.pubnub.api.models.consumer.PNStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*

abstract class Endpoint<Input, Output>(protected val pubnub: PubNub) {

    companion object {
        private const val SERVER_RESPONSE_SUCCESS = 200
        private const val SERVER_RESPONSE_BAD_REQUEST = 400
        private const val SERVER_RESPONSE_FORBIDDEN = 403
        private const val SERVER_RESPONSE_NOT_FOUND = 404
    }

    private lateinit var cachedCallback: (result: Output?, status: PNStatus) -> Unit
    private lateinit var call: Call<Input>
    private var silenceFailures = false

    var queryParam: Map<String, String> = emptyMap()

    fun sync(): Output? {
        validateParams()

        call = doWork(createBaseParams())

        val response =
            try {
                call.execute()
            } catch (e: Exception) {
                throw PubNubException(PubNubError.PARSING_ERROR).apply {
                    errorMessage = e.toString()
                    affectedCall = call
                }
            }

        when {
            response.isSuccessful -> {
                storeRequestLatency(response)
                return checkAndCreateResponse(response)
            }
            else -> {
                val (errorString, errorJson) = extractErrorBody(response)
                throw PubNubException(PubNubError.HTTP_ERROR).apply {
                    errorMessage = errorString
                    jso = errorJson.toString()
                    statusCode = response.code()
                    affectedCall = call
                }
            }
        }
    }

    fun async(callback: (result: Output?, status: PNStatus) -> Unit) {
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

        call.enqueue(object : Callback<Input> {

            override fun onResponse(call: Call<Input>, response: Response<Input>) {

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

                        val exception = PubNubException(PubNubError.HTTP_ERROR).apply {
                            errorMessage = errorString
                            jso = errorJson.toString()
                            statusCode = response.code()
                            affectedCall = call
                        }

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

                if (silenceFailures) {
                    return
                }

                lateinit var pnStatusCategory: PNStatusCategory

                val pubnubException = PubNubException(t.toString())

                try {
                    throw t
                } catch (networkException: UnknownHostException) {
                    pubnubException.pubnubError = PubNubError.CONNECTION_NOT_SET
                    pnStatusCategory = PNUnexpectedDisconnectCategory
                } catch (connectException: ConnectException) {
                    pubnubException.pubnubError = PubNubError.CONNECT_EXCEPTION
                    pnStatusCategory = PNUnexpectedDisconnectCategory
                } catch (socketTimeoutException: SocketTimeoutException) {
                    pubnubException.pubnubError = PubNubError.SUBSCRIBE_TIMEOUT
                    pnStatusCategory = PNTimeoutCategory
                } catch (ioException: IOException) {
                    pubnubException.pubnubError = PubNubError.PARSING_ERROR
                    pnStatusCategory = PNMalformedResponseCategory
                } catch (ioException: IllegalStateException) {
                    pubnubException.pubnubError = PubNubError.PARSING_ERROR
                    pnStatusCategory = PNMalformedResponseCategory
                } catch (throwable1: Throwable) {
                    pubnubException.pubnubError = PubNubError.HTTP_ERROR
                    pnStatusCategory = if (call.isCanceled) {
                        PNCancelledCategory
                    } else {
                        PNBadRequestCategory
                    }
                }

                callback.invoke(
                    null,
                    createStatusResponse(
                        category = pnStatusCategory,
                        exception = pubnubException
                    )
                )
            }
        })
    }

    private fun storeRequestLatency(response: Response<Input>) {
        pubnub.telemetryManager.storeLatency(
            latency = with(response.raw()) {
                receivedResponseAtMillis() - sentRequestAtMillis()
            },
            type = operationType()
        )
    }

    private fun createBaseParams(): HashMap<String, String> {
        val map = hashMapOf<String, String>()

        map.putAll(queryParam)

        map["pnsdk"] = "PubNub-Kotlin/${pubnub.version}"
        map["uuid"] = pubnub.configuration.uuid

        if (pubnub.configuration.includeInstanceIdentifier) {
            map["instanceid"] = pubnub.instanceId
        }

        if (pubnub.configuration.includeRequestIdentifier) {
            map["requestid"] = pubnub.requestId()
        }

        if (isAuthRequired() && pubnub.configuration.isAuthKeyValid()) {
            map["auth"] = pubnub.configuration.authKey
        }

        map.putAll(pubnub.telemetryManager.operationsLatency())
        return map
    }

    /**
     * cancel the operation but do not alert anybody, useful for restarting the heartbeats and subscribe loops.
     */
    fun silentCancel() {
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
                tlsEnabled = it.raw().request().url().isHttps
                origin = it.raw().request().url().host()
                uuid = it.raw().request().url().queryParameter("uuid")
                authKey = it.raw().request().url().queryParameter("auth")
                clientRequest = it.raw().request()
            }
        }

        val errorChannels = mutableListOf<String>()
        val errorGroups = mutableListOf<String>()

        if (errorBody != null) {
            if (pubnub.mapper.isJsonObject(errorBody) && pubnub.mapper.hasField(errorBody, "payload")) {

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

    internal fun retry() {
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
            throw pubnubException.apply {
                statusCode = input.code()
                jso = pubnub.mapper.toJson(input.body())
                affectedCall = call
            }
        } catch (e: KotlinNullPointerException) {
            throw PubNubException(PubNubError.PARSING_ERROR).apply {
                errorMessage = e.toString()
                affectedCall = call
                statusCode = input.code()
                jso = pubnub.mapper.toJson(input.body())
            }
        } catch (e: IllegalStateException) {
            throw PubNubException(PubNubError.PARSING_ERROR).apply {
                errorMessage = e.toString()
                affectedCall = call
                statusCode = input.code()
                jso = pubnub.mapper.toJson(input.body())
            }
        } catch (e: IndexOutOfBoundsException) {
            throw PubNubException(PubNubError.PARSING_ERROR).apply {
                errorMessage = e.toString()
                affectedCall = call
                statusCode = input.code()
                jso = pubnub.mapper.toJson(input.body())
            }
        } catch (e: NullPointerException) {
            throw PubNubException(PubNubError.PARSING_ERROR).apply {
                errorMessage = e.toString()
                affectedCall = call
                statusCode = input.code()
                jso = pubnub.mapper.toJson(input.body())
            }
        } catch (e: IllegalArgumentException) {
            throw PubNubException(PubNubError.PARSING_ERROR).apply {
                errorMessage = e.toString()
                affectedCall = call
                statusCode = input.code()
                jso = pubnub.mapper.toJson(input.body())
            }
        } catch (e: TypeCastException) {
            throw PubNubException(PubNubError.PARSING_ERROR).apply {
                errorMessage = e.toString()
                affectedCall = call
                statusCode = input.code()
                jso = pubnub.mapper.toJson(input.body())
            }
        } catch (e: ClassCastException) {
            throw PubNubException(PubNubError.PARSING_ERROR).apply {
                errorMessage = e.toString()
                affectedCall = call
                statusCode = input.code()
                jso = pubnub.mapper.toJson(input.body())
            }
        } catch (e: UninitializedPropertyAccessException) {
            throw PubNubException(PubNubError.PARSING_ERROR).apply {
                errorMessage = e.toString()
                affectedCall = call
                statusCode = input.code()
                jso = pubnub.mapper.toJson(input.body())
            }
        }
    }

    // protected abstract fun getAffectedChannels(): List<String?>
    // protected abstract fun getAffectedChannelGroups(): List<String?>

    protected open fun getAffectedChannels() = emptyList<String>()
    protected open fun getAffectedChannelGroups(): List<String> = emptyList()

    protected open fun validateParams() {
        if (isSubKeyRequired() && !pubnub.configuration.isSubscribeKeyValid()) {
            throw PubNubException(PubNubError.SUBSCRIBE_KEY_MISSING)
        }
        if (isPubKeyRequired() && !pubnub.configuration.isPublishKeyValid()) {
            throw PubNubException(PubNubError.PUBLISH_KEY_MISSING)
        }
    }

    protected abstract fun doWork(queryParams: HashMap<String, String>): Call<Input>
    protected abstract fun createResponse(input: Response<Input>): Output?

    protected abstract fun operationType(): PNOperationType

    protected open fun isSubKeyRequired() = true
    protected open fun isPubKeyRequired() = false
    protected open fun isAuthRequired() = true
}

