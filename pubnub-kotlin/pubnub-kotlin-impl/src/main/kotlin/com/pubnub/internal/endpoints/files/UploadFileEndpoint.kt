package com.pubnub.internal.endpoints.files

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.api.v2.callbacks.Result
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
import java.io.ByteArrayInputStream
import java.time.Instant
import java.util.function.Consumer
import javax.xml.parsers.DocumentBuilderFactory

internal class UploadFileEndpoint(
    private val fileName: String,
    private val content: ByteArray,
    private val key: FormField,
    private val formParams: List<FormField>,
    private val baseUrl: String,
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
    override fun createResponse(input: Response<Unit>) {
        // S3 returns empty response on success, just return Unit
        return
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Unit> {
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

    @Throws(PubNubException::class)
    override fun sync() {
        try {
            return super.sync()
        } catch (e: PubNubException) {
            throw parseS3XmlError(e)
        }
    }

    override fun async(callback: Consumer<Result<Unit>>) {
        super.async { result ->
            result.onFailure { throwable ->
                if (throwable is PubNubException) {
                    callback.accept(Result.failure(parseS3XmlError(throwable)))
                } else {
                    callback.accept(result)
                }
            }
            result.onSuccess {
                callback.accept(result)
            }
        }
    }

    internal fun parseS3XmlError(exception: PubNubException): PubNubException {
        val errorMessage = exception.errorMessage

        // Check if error message looks like XML
        if (errorMessage == null || !errorMessage.trim().startsWith("<")) {
            return exception
        }

        return try {
            val dbFactory = DocumentBuilderFactory.newInstance()
            dbFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true)
            dbFactory.isXIncludeAware = false
            val dBuilder = dbFactory.newDocumentBuilder()
            val doc = dBuilder.parse(ByteArrayInputStream(errorMessage.toByteArray()))
            doc.documentElement.normalize()

            // Extract Code element
            val codeElements = doc.getElementsByTagName("Code")
            val s3ErrorCode = codeElements.item(0)?.firstChild?.nodeValue

            // Extract Message element
            val messageElements = doc.getElementsByTagName("Message")
            val baseMessage = messageElements.item(0)?.firstChild?.nodeValue ?: errorMessage

            // Handle specific S3 error types
            when (s3ErrorCode) {
                "AccessDenied" -> {
                    // Check if this is a policy expiration error
                    if (baseMessage.contains("Policy expired", ignoreCase = true)) {
                        throw PubNubException(PubNubError.S3_PRE_SIGNED_URL_HAS_EXPIRED)
                    }
                    exception.copy(errorMessage = baseMessage)
                }
                "EntityTooLarge" -> {
                    // Extract size information for better error message
                    val proposedSize = doc.getElementsByTagName("ProposedSize").item(0)?.firstChild?.nodeValue
                    val maxSizeAllowed = doc.getElementsByTagName("MaxSizeAllowed").item(0)?.firstChild?.nodeValue

                    val detailedMessage = if (proposedSize != null && maxSizeAllowed != null) {
                        "File size ($proposedSize bytes) exceeds maximum allowed size ($maxSizeAllowed bytes)"
                    } else {
                        "File size exceeds maximum allowed size"
                    }
                    throw PubNubException(PubNubError.FILE_TOO_LARGE).copy(errorMessage = detailedMessage)
                }
                else -> {
                    // For all other errors, just return the cleaned message
                    exception.copy(errorMessage = baseMessage)
                }
            }
        } catch (e: Exception) {
            when (e) {
                is PubNubException -> throw e // Re-throw our mapped errors
                else -> {
                    // If XML parsing fails, return original exception
                    log.warn(
                        LogMessage(
                            message = LogMessageContent.Text("Failed to parse S3 XML error: ${e.message}"),
                        )
                    )
                    exception
                }
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
