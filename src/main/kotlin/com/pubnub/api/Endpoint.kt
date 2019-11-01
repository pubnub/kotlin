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

abstract class Endpoint<Input, Output>(internal val pubnub: PubNub) {


    companion object {
        private const val SERVER_RESPONSE_SUCCESS = 200
        private const val SERVER_RESPONSE_BAD_REQUEST = 400
        private const val SERVER_RESPONSE_FORBIDDEN = 403
        private const val SERVER_RESPONSE_NOT_FOUND = 404
    }

    // private lateinit var cachedCallback: PNCallback<Output>
    private lateinit var cachedCallback: (result: Output?, status: PNStatus) -> Unit
    private lateinit var call: Call<Input>
    private var silenceFailures = false

    fun sync(): Output? {
        validateParams()

        call = doWork(createBaseParams())

        val response =
            try {
                call.execute()
            } catch (e: IOException) {
                throw PubNubException(PubNubError.PARSING_ERROR).apply {
                    errorMessage = e.message
                    affectedCall = call
                }
            }

        when {
            response.isSuccessful -> {
                storeRequestLatency(response)
                return createResponse(response)
            }
            else -> {
                val (errorString, errorJson) = extractErrorBody(response)
                throw PubNubException(PubNubError.HTTP_ERROR).apply {
                    errorMessage = errorString
                    jso = errorJson
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
                        storeRequestLatency(response)
                        try {
                            Triple(PNAcknowledgmentCategory, createResponse(response), null)
                        } catch (e: PubNubException) {
                            Triple(PNMalformedResponseCategory, null, e)
                        }.let {
                            callback.invoke(
                                it.second,
                                createStatusResponse(it.first, response, it.third)
                            )
                        }
                    }
                    else -> {
                        val (errorString, errorJson) = extractErrorBody(response)

                        val exception = with(PubNubException(errorString)) {
                            jso = errorJson
                            statusCode = response.code()
                            this
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
                                pnStatusCategory,
                                response,
                                exception
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

                val pubnubException = PubNubException(t.message)

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
        // todo
        println("storeRequestLatency: ${operationType().queryParam}")
    }

    protected fun encodeParams(params: Map<String, String>): Map<String, String> {
        val encodedParams = HashMap(params)
        if (encodedParams.containsKey("auth")) {
            encodedParams["auth"] = encodedParams["auth"]?.let { PubNubUtil.urlEncode(it) }
        }
        return encodedParams
    }

    private fun createBaseParams(): HashMap<String, String> {
        val map = hashMapOf(
            "pnsdk" to "PubNub-Kotlin/${pubnub.version}",
            "uuid" to pubnub.config.uuid,
            "instanceid" to pubnub.instanceId,
            "requestid" to pubnub.requestId()
        )
        if (isAuthRequired()) {
            pubnub.config.authKey?.let {
                map["auth"] = it
            }
        }
        return map
    }


    private fun createStatusResponse(
        category: PNStatusCategory,
        response: Response<Input>? = null,
        exception: PubNubException? = null
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


        pnStatus.affectedChannels = getAffectedChannels()
        pnStatus.affectedChannelGroups = getAffectedChannelGroups()

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

    protected abstract fun getAffectedChannels(): List<String?>

    protected abstract fun getAffectedChannelGroups(): List<String?>

    @Throws(PubNubException::class)
    protected open fun validateParams() {
        if (isSubKeyRequired() && pubnub.config.subscribeKey.isNullOrBlank()) {
            throw PubNubException(PubNubError.SUBSCRIBE_KEY_MISSING)
        }
        if (isPubKeyRequired() && pubnub.config.publishKey.isNullOrBlank()) {
            throw PubNubException(PubNubError.PUBLISH_KEY_MISSING)
        }
    }

    @Throws(PubNubException::class)
    protected abstract fun doWork(queryParams: HashMap<String, String>): Call<Input>

    @Throws(PubNubException::class)
    protected abstract fun createResponse(input: Response<Input>): Output?

    protected abstract fun operationType(): PNOperationType

    protected abstract fun isSubKeyRequired(): Boolean
    protected abstract fun isPubKeyRequired(): Boolean
    protected abstract fun isAuthRequired(): Boolean
}