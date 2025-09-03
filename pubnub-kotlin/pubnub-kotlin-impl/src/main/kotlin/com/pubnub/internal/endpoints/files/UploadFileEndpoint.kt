package com.pubnub.internal.endpoints.files

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogConfig
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.logging.LogMessageType
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.models.server.files.FileUploadRequestDetails
import com.pubnub.internal.models.server.files.FormField
import com.pubnub.internal.services.S3Service
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.function.Consumer
import javax.net.ssl.SSLException
import javax.xml.parsers.DocumentBuilderFactory

internal class UploadFileEndpoint(
    private val s3Service: S3Service,
    private val fileName: String,
    private val content: ByteArray,
    private val key: FormField,
    private val formParams: List<FormField>,
    private val baseUrl: String,
    private val logConfig: LogConfig
) : ExtendedRemoteAction<Unit> {
    private var call: Call<Unit>? = null
    private val log = LoggerManager.instance.getLogger(logConfig, this::class.java)

    @Throws(PubNubException::class)
    private fun prepareCall(): Call<Unit> {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        addFormParamsWithKeyFirst(key, formParams, builder)
        val mediaType = getMediaType(formParams.findContentType())

        builder.addFormDataPart(FILE_PART_MULTIPART, fileName, content.toRequestBody(mediaType, 0, content.size))
        return s3Service.upload(baseUrl, builder.build())
    }

    private fun List<FormField>.findContentType(): String? {
        return find { (key, _) ->
            key.equals(CONTENT_TYPE_HEADER, ignoreCase = true)
        }?.value
    }

    private fun getMediaType(contentType: String?): MediaType {
        return if (contentType == null) {
            APPLICATION_OCTET_STREAM
        } else {
            try {
                contentType.toMediaType()
            } catch (t: Throwable) {
                log.warn(
                    LogMessage(
                        location = this::class.java.toString(),
                        type = LogMessageType.TEXT,
                        message = LogMessageContent.Text("Content-Type: $contentType was not recognized by MediaType.get: ${t.message}")
                    )
                )
                APPLICATION_OCTET_STREAM
            }
        }
    }

    @Throws(PubNubException::class)
    override fun sync() {
        call = prepareCall()
        val serverResponse =
            try {
                call!!.execute()
            } catch (e: IOException) {
                throw PubNubException(
                    errorMessage = e.message,
                    affectedCall = call,
                    pubnubError = PubNubError.PARSING_ERROR,
                    cause = e,
                )
            }
        if (!serverResponse.isSuccessful) {
            throw createException(serverResponse)
        }
    }

    override fun async(callback: Consumer<Result<Unit>>) {
        try {
            call = prepareCall()
            call!!.enqueue(
                object : Callback<Unit> {
                    override fun onResponse(
                        performedCall: Call<Unit>,
                        response: Response<Unit>,
                    ) {
                        if (!response.isSuccessful) {
                            val ex = createException(response)
                            callback.accept(Result.failure(ex))
                            return
                        }
                        callback.accept(Result.success(Unit))
                    }

                    override fun onFailure(
                        performedCall: Call<Unit>,
                        throwable: Throwable,
                    ) {
                        if (call!!.isCanceled) {
                            return
                        }
                        val error =
                            when (throwable) {
                                is UnknownHostException, is SocketException, is SSLException -> PubNubError.CONNECT_EXCEPTION
                                is SocketTimeoutException -> PubNubError.SUBSCRIBE_TIMEOUT
                                else ->
                                    if (performedCall.isCanceled) {
                                        PubNubError.HTTP_ERROR
                                    } else {
                                        PubNubError.HTTP_ERROR
                                    }
                            }
                        callback.accept(
                            Result.failure(
                                PubNubException(error).copy(
                                    errorMessage = throwable.message ?: error.message,
                                    cause = throwable,
                                ),
                            ),
                        )
                    }
                },
            )
        } catch (e: Throwable) {
            callback.accept(Result.failure(PubNubException.from(e)))
        }
    }

    override fun retry() {}

    override fun silentCancel() {
        if (!call!!.isCanceled) {
            call!!.cancel()
        }
    }

    private fun createException(response: Response<Unit>): PubNubException {
        return try {
            PubNubException(
                errorMessage = response.readErrorMessage(),
                affectedCall = call,
                statusCode = response.code(),
            )
        } catch (e: Exception) {
            PubNubException(
                errorMessage = e.message,
                affectedCall = call,
                statusCode = response.code(),
            )
        }
    }

    private fun Response<Unit>.readErrorMessage(): String {
        val dbFactory = DocumentBuilderFactory.newInstance()
        dbFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true)
        dbFactory.isXIncludeAware = false
        val dBuilder = dbFactory.newDocumentBuilder()
        val doc = dBuilder.parse(errorBody()!!.byteStream())
        doc.documentElement.normalize()
        val elements = doc.getElementsByTagName("Message")
        return elements.item(0)?.firstChild?.nodeValue ?: "N/A"
    }

    internal class Factory(private val pubNub: PubNubImpl) {
        fun create(
            fileName: String,
            content: ByteArray,
            fileUploadRequestDetails: FileUploadRequestDetails,
        ): ExtendedRemoteAction<Unit> {
            return UploadFileEndpoint(
                pubNub.retrofitManager.s3Service,
                fileName,
                content,
                fileUploadRequestDetails.keyFormField,
                fileUploadRequestDetails.formFields,
                fileUploadRequestDetails.url,
                pubNub.logConfig
            )
        }
    }

    companion object {
        private val APPLICATION_OCTET_STREAM = "application/octet-stream".toMediaType()
        private const val CONTENT_TYPE_HEADER = "Content-Type"
        private const val FILE_PART_MULTIPART = "file"

        private fun addFormParamsWithKeyFirst(
            keyValue: FormField,
            formParams: List<FormField>,
            builder: MultipartBody.Builder,
        ) {
            builder.addFormDataPart(keyValue.key, keyValue.value)
            formParams
                .filter { it.key != keyValue.key }
                .forEach { builder.addFormDataPart(it.key, it.value) }
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.FileOperation
}
