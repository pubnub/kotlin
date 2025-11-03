package com.pubnub.internal.endpoints.files

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.models.server.files.FileUploadRequestDetails
import com.pubnub.internal.models.server.files.FormField
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response
import java.time.Instant

internal class UploadFileEndpoint(
    private val fileName: String,
    private val content: ByteArray,
    private val key: FormField,
    private val formParams: List<FormField>,
    private val baseUrl: String,
    private val expirationDate: String?,
    pubNub: PubNubImpl,
) : EndpointCore<Unit, Unit>(pubNub) {
    private val log = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    @Throws(PubNubException::class)
    override fun validateParams() {
        if (baseUrl.isBlank()) {
            throw PubNubException(PubNubError.INVALID_ARGUMENTS).copy(
                errorMessage = "S3 upload URL cannot be empty"
            )
        }

        if (fileName.isBlank()) {
            throw PubNubException(PubNubError.INVALID_ARGUMENTS).copy(
                errorMessage = "File name cannot be empty"
            )
        }
    }

    @Throws(PubNubException::class)
    override fun createResponse(input: Response<Unit>): Unit {
        // S3 returns empty response on success, just return Unit
        return Unit
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Unit> {
        // Check URL expiration before EACH attempt (including retries)
        expirationDate?.let { expirationDateString ->
            try {
                val expiration = Instant.parse(expirationDateString)
                if (Instant.now().isAfter(expiration)) {
                    throw PubNubException(PubNubError.HTTP_ERROR).copy(
                        errorMessage = "S3 pre-signed URL has expired. Expiration time: $expiration. " +
                            "The URL is only valid for ~60 seconds. Cannot retry upload - please call sendFile() again."
                    )
                }
            } catch (e: Exception) {
                when (e) {
                    is PubNubException -> throw e // Re-throw our expiration error
                    else -> {
                        // If parsing fails, log warning but don't fail the upload
                        log.warn(
                            LogMessage(
                                message = LogMessageContent.Text("Failed to parse S3 URL expiration date: $expirationDateString - ${e.message}"),
                            )
                        )
                    }
                }
            }
        }

        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        addFormParamsWithKeyFirst(key, formParams, builder)
        val mediaType = getMediaType(formParams.findContentType())

        builder.addFormDataPart(FILE_PART_MULTIPART, fileName, content.toRequestBody(mediaType, 0, content.size))
        return retrofitManager.s3Service.upload(baseUrl, builder.build())
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
                        message = LogMessageContent.Text("Content-Type: $contentType was not recognized by MediaType.get: ${t.message}"),
                    )
                )
                APPLICATION_OCTET_STREAM
            }
        }
    }

    override fun getAffectedChannels(): List<String> = emptyList()

    override fun getAffectedChannelGroups(): List<String> = emptyList()

    override fun isAuthRequired(): Boolean = false

    override fun isSubKeyRequired(): Boolean = false

    override fun isPubKeyRequired(): Boolean = false

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.FILE_PERSISTENCE

    internal class Factory(private val pubNub: PubNubImpl) {
        fun create(
            fileName: String,
            content: ByteArray,
            fileUploadRequestDetails: FileUploadRequestDetails,
        ): UploadFileEndpoint {
            return UploadFileEndpoint(
                fileName = fileName,
                content = content,
                key = fileUploadRequestDetails.keyFormField,
                formParams = fileUploadRequestDetails.formFields,
                baseUrl = fileUploadRequestDetails.url,
                expirationDate = fileUploadRequestDetails.expirationDate,
                pubNub = pubNub
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
