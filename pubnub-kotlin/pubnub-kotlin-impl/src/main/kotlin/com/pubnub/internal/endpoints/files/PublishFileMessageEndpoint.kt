package com.pubnub.internal.endpoints.files

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.files.PublishFileMessage
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.files.PNBaseFile
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.crypto.encryptString
import com.pubnub.internal.endpoints.pubsub.PublishEndpoint.Companion.CUSTOM_MESSAGE_TYPE_QUERY_PARAM
import com.pubnub.internal.extension.numericString
import com.pubnub.internal.extension.quoted
import com.pubnub.internal.models.server.files.FileUploadNotification
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubImpl.publishFileMessage]
 */
open class PublishFileMessageEndpoint(
    private val channel: String,
    fileName: String,
    fileId: String,
    private val message: Any? = null,
    private val meta: Any? = null,
    private val ttl: Int? = null,
    private val shouldStore: Boolean? = null,
    private val customMessageType: String? = null,
    pubNub: PubNubImpl,
) : EndpointCore<List<Any>, PNPublishFileMessageResult>(pubNub), PublishFileMessage {
    private val pnFile = PNBaseFile(fileId, fileName)

    @Throws(PubNubException::class)
    override fun validateParams() {
        if (channel.isEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
    }

    @Throws(PubNubException::class)
    override fun doWork(queryParams: HashMap<String, String>): Call<List<Any>> {
        val stringifiedMessage: String = pubnub.mapper.toJson(FileUploadNotification(message, pnFile))
        val messageAsString = configuration.cryptoModule?.encryptString(stringifiedMessage)?.quoted() ?: stringifiedMessage
        meta?.let {
            val stringifiedMeta: String = pubnub.mapper.toJson(it)
            queryParams["meta"] = stringifiedMeta
        }
        shouldStore?.numericString?.let { queryParams["store"] = it }
        ttl?.let { queryParams["ttl"] = it.toString() }
        customMessageType?.let { queryParams[CUSTOM_MESSAGE_TYPE_QUERY_PARAM] = it }

        return retrofitManager.filesService.notifyAboutFileUpload(
            configuration.publishKey,
            configuration.subscribeKey,
            channel,
            messageAsString,
            queryParams,
        )
    }

    @Throws(PubNubException::class)
    override fun createResponse(input: Response<List<Any>>): PNPublishFileMessageResult {
        return input.body()?.let { body ->
            val timetoken = body[2].toString().toLong()
            PNPublishFileMessageResult(timetoken)
        } ?: throw PubNubException(PubNubError.INTERNAL_ERROR)
    }

    override fun getAffectedChannels() = listOf(channel)

    override fun getAffectedChannelGroups(): List<String> = listOf()

    override fun operationType(): PNOperationType = PNOperationType.FileOperation

    override fun isAuthRequired(): Boolean = true

    override fun isSubKeyRequired(): Boolean = true

    override fun isPubKeyRequired(): Boolean = true

    internal class Factory(private val pubNub: PubNubImpl) {
        fun create(
            channel: String,
            fileName: String,
            fileId: String,
            message: Any? = null,
            meta: Any? = null,
            ttl: Int? = null,
            shouldStore: Boolean? = null,
            customMessageType: String? = null
        ): ExtendedRemoteAction<PNPublishFileMessageResult> {
            return PublishFileMessageEndpoint(
                channel = channel,
                fileName = fileName,
                fileId = fileId,
                message = message,
                meta = meta,
                ttl = ttl,
                shouldStore = shouldStore,
                customMessageType = customMessageType,
                pubNub = pubNub,
            )
        }
    }

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.PUBLISH
}
