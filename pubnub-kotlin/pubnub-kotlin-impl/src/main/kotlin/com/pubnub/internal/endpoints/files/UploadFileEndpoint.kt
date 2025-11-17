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
import java.util.function.Consumer
import javax.xml.parsers.DocumentBuilderFactory

internal class UploadFileEndpoint(
    private val fileName: String,
    private val content: ByteArray,
    private val key: FormField,
    private val formParams: List<FormField>,
    private val baseUrl: String,
    private val isEncrypted: Boolean = false,
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
        log.debug(
            LogMessage(
                message = LogMessageContent.Text(
                    "S3 upload successful - fileName: $fileName, responseCode: ${input.code()}, responseMessage: ${input.message()}"
                )
            )
        )
        // S3 returns empty response on success, just return Unit
        return
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Unit> {
        val contentType = formParams.findContentType()
        log.debug(
            LogMessage(
                message = LogMessageContent.Text(
                    "Initiating S3 upload - fileName: $fileName, contentSize: ${content.size} bytes, contentType: $contentType, isEncrypted: $isEncrypted, formFieldsCount: ${formParams.size}"
                )
            )
        )

        // Override Content-Type for encrypted files to prevent UTF-8 corruption of binary data
        val modifiedFormParams = if (isEncrypted) {
            formParams.map { param ->
                if (param.key.equals(CONTENT_TYPE_HEADER, ignoreCase = true)) {
                    FormField(param.key, "application/octet-stream")
                } else {
                    param
                }
            }
        } else {
            formParams
        }

        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        addFormParamsWithKeyFirst(key, modifiedFormParams, builder)

        // For encrypted files, always use application/octet-stream to prevent charset interpretation
        val mediaType = if (isEncrypted) {
            APPLICATION_OCTET_STREAM
        } else {
            getMediaType(modifiedFormParams.findContentType())
        }

        builder.addFormDataPart(FILE_PART_MULTIPART, fileName, content.toRequestBody(mediaType, 0, content.size))

        log.debug(
            LogMessage(
                message = LogMessageContent.Text(
                    "Multipart request built - executing upload to S3 for fileName: $fileName with mediaType: $mediaType"
                )
            )
        )

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
        val startTime = System.currentTimeMillis()
        try {
            super.sync()
            val duration = System.currentTimeMillis() - startTime
            log.debug(
                LogMessage(
                    message = LogMessageContent.Text(
                        "Synchronous upload completed successfully - fileName: $fileName, duration: ${duration}ms"
                    )
                )
            )
        } catch (e: PubNubException) {
            val duration = System.currentTimeMillis() - startTime
            log.error(
                LogMessage(
                    message = LogMessageContent.Text(
                        "Synchronous upload failed - fileName: $fileName, duration: ${duration}ms, error: ${e.pubnubError}, statusCode: ${e.statusCode}, message: ${
                            e.errorMessage?.take(
                                200
                            )
                        }"
                    )
                )
            )
            throw parseS3XmlError(e)
        }
    }

    override fun async(callback: Consumer<Result<Unit>>) {
        val startTime = System.currentTimeMillis()
        super.async { result ->
            val duration = System.currentTimeMillis() - startTime
            result.onFailure { throwable ->
                if (throwable is PubNubException) {
                    log.error(
                        LogMessage(
                            message = LogMessageContent.Text(
                                "Asynchronous upload failed - fileName: $fileName, duration: ${duration}ms, error: ${throwable.pubnubError}, statusCode: ${throwable.statusCode}, message: ${
                                    throwable.errorMessage?.take(
                                        200
                                    )
                                }"
                            )
                        )
                    )
                    callback.accept(Result.failure(parseS3XmlError(throwable)))
                } else {
                    log.error(
                        LogMessage(
                            message = LogMessageContent.Text(
                                "Asynchronous upload failed with non-PubNub exception - fileName: $fileName, duration: ${duration}ms, exception: ${throwable.javaClass.simpleName}, message: ${
                                    throwable.message?.take(
                                        200
                                    )
                                }"
                            )
                        )
                    )
                    callback.accept(result)
                }
            }
            result.onSuccess {
                log.debug(
                    LogMessage(
                        message = LogMessageContent.Text(
                            "Asynchronous upload completed successfully - fileName: $fileName, duration: ${duration}ms"
                        )
                    )
                )
                callback.accept(result)
            }
        }
    }

    internal fun parseS3XmlError(exception: PubNubException): PubNubException {
        val errorMessage = exception.errorMessage
        if (!isS3XmlError(errorMessage)) {
            return exception
        }

        return try {
            val s3Error = parseS3XmlErrorResponse(errorMessage!!) ?: return exception
            handleS3ErrorCode(s3Error, exception)
        } catch (e: Exception) {
            handleParsingException(e, exception)
        }
    }

    private fun isS3XmlError(errorMessage: String?): Boolean {
        return errorMessage != null && errorMessage.trim().startsWith("<")
    }

    private data class S3ErrorResponse(
        val code: String?,
        val message: String,
        val proposedSize: String? = null,
        val maxSizeAllowed: String? = null
    )

    private fun parseS3XmlErrorResponse(errorMessage: String): S3ErrorResponse? {
        return try {
            val dbFactory = DocumentBuilderFactory.newInstance()
            dbFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true)
            dbFactory.isXIncludeAware = false
            val dBuilder = dbFactory.newDocumentBuilder()
            val doc = dBuilder.parse(ByteArrayInputStream(errorMessage.toByteArray()))
            doc.documentElement.normalize()

            val code = doc.getElementsByTagName("Code").item(0)?.firstChild?.nodeValue
            val message = doc.getElementsByTagName("Message").item(0)?.firstChild?.nodeValue ?: errorMessage
            val proposedSize = doc.getElementsByTagName("ProposedSize").item(0)?.firstChild?.nodeValue
            val maxSizeAllowed = doc.getElementsByTagName("MaxSizeAllowed").item(0)?.firstChild?.nodeValue

            S3ErrorResponse(code, message, proposedSize, maxSizeAllowed)
        } catch (e: Exception) {
            null
        }
    }

    private fun handleS3ErrorCode(s3Error: S3ErrorResponse, exception: PubNubException): PubNubException {
        return when (s3Error.code) {
            "AccessDenied" -> handleAccessDeniedError(s3Error.message, exception)
            "EntityTooLarge" -> handleEntityTooLargeError(s3Error.proposedSize, s3Error.maxSizeAllowed)
            else -> exception.copy(errorMessage = s3Error.message)
        }
    }

    private fun handleAccessDeniedError(message: String, exception: PubNubException): PubNubException {
        if (message.contains("Policy expired", ignoreCase = true)) {
            throw PubNubException(PubNubError.UPLOAD_URL_HAS_EXPIRED)
        }
        return exception.copy(errorMessage = message)
    }

    private fun handleEntityTooLargeError(proposedSize: String?, maxSizeAllowed: String?): Nothing {
        val detailedMessage = buildEntityTooLargeMessage(proposedSize, maxSizeAllowed)
        throw PubNubException(PubNubError.FILE_TOO_LARGE).copy(errorMessage = detailedMessage)
    }

    private fun buildEntityTooLargeMessage(proposedSize: String?, maxSizeAllowed: String?): String {
        return if (proposedSize != null && maxSizeAllowed != null) {
            "File size ($proposedSize bytes) exceeds maximum allowed size ($maxSizeAllowed bytes)"
        } else {
            "File size exceeds maximum allowed size"
        }
    }

    private fun handleParsingException(e: Exception, exception: PubNubException): PubNubException {
        if (e is PubNubException) {
            throw e
        }
        log.warn(
            LogMessage(
                message = LogMessageContent.Text("Failed to parse S3 XML error: ${e.message}"),
            )
        )
        return exception
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
            isEncrypted: Boolean = false,
        ): UploadFileEndpoint {
            return UploadFileEndpoint(
                fileName = fileName,
                content = content,
                key = fileUploadRequestDetails.keyFormField,
                formParams = fileUploadRequestDetails.formFields,
                baseUrl = fileUploadRequestDetails.url,
                isEncrypted = isEncrypted,
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
