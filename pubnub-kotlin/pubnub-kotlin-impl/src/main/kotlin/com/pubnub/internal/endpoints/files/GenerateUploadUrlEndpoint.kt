package com.pubnub.internal.endpoints.files

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.logging.LogMessageType
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.ExtendedLogger
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.models.server.files.FileUploadRequestDetails
import com.pubnub.internal.models.server.files.FormField
import com.pubnub.internal.models.server.files.GenerateUploadUrlPayload
import com.pubnub.internal.models.server.files.GeneratedUploadUrlResponse
import org.slf4j.event.Level
import retrofit2.Call
import retrofit2.Response

internal class GenerateUploadUrlEndpoint(
    private val channel: String,
    private val fileName: String,
    pubNub: PubNubImpl,
) : EndpointCore<GeneratedUploadUrlResponse, FileUploadRequestDetails>(pubNub) {
    private val log: ExtendedLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    @Throws(PubNubException::class)
    override fun validateParams() {
        if (channel.isEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
    }

    @Throws(PubNubException::class)
    override fun createResponse(input: Response<GeneratedUploadUrlResponse>): FileUploadRequestDetails {
        if (input.body() == null) {
            throw PubNubException(PubNubError.INTERNAL_ERROR).copy(errorMessage = "Empty body, but GeneratedUploadUrlResponse expected")
        }
        val response = input.body()!!
        val keyFormField = getKeyFormField(response)
        return FileUploadRequestDetails(
            response.status,
            response.data,
            response.fileUploadRequest.url,
            response.fileUploadRequest.method,
            response.fileUploadRequest.expirationDate,
            keyFormField,
            response.fileUploadRequest.formFields,
        )
    }

    @Throws(PubNubException::class)
    private fun getKeyFormField(response: GeneratedUploadUrlResponse): FormField {
        val formFields: List<FormField> = response.fileUploadRequest.formFields
        return formFields.find { it.key == "key" } ?: throw PubNubException(PubNubError.INTERNAL_ERROR).copy(
            errorMessage = "Couldn't find `key` form field in GeneratedUploadUrlResponse",
        )
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<GeneratedUploadUrlResponse> {
        log.trace(
            LogMessage(
                pubNubId = pubnub.instanceId,
                logLevel = Level.TRACE,
                location = this::class.java.toString(),
                type = LogMessageType.OBJECT,
                message = LogMessageContent.Object(
                    message = mapOf(
                        "channel" to channel,
                        "fileName" to fileName,
                        "queryParams" to queryParams
                    )
                ),
                details = "GenerateUploadUrl API call"
            )
        )

        return retrofitManager.filesService.generateUploadUrl(
            configuration.subscribeKey,
            channel,
            GenerateUploadUrlPayload(fileName),
            queryParams,
        )
    }

    override fun getAffectedChannels() = listOf(channel)

    override fun getAffectedChannelGroups(): List<String> = listOf()

    override fun operationType(): PNOperationType = PNOperationType.FileOperation

    override fun isAuthRequired(): Boolean = true

    override fun isSubKeyRequired(): Boolean = true

    override fun isPubKeyRequired(): Boolean = false

    internal class Factory(private val pubNub: PubNubImpl) {
        fun create(
            channel: String,
            fileName: String,
        ): ExtendedRemoteAction<FileUploadRequestDetails> {
            return GenerateUploadUrlEndpoint(channel, fileName, pubNub)
        }
    }

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.FILE_PERSISTENCE
}
