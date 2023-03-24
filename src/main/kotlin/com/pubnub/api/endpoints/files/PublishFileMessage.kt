package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.api.PNConfiguration.Companion.isValid
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.SpaceId
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.files.PNBaseFile
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult
import com.pubnub.api.models.server.files.FileUploadNotification
import com.pubnub.api.vendor.Crypto
import com.pubnub.extension.numericString
import com.pubnub.extension.quoted
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNub.publishFileMessage]
 */
open class PublishFileMessage(
    private val channel: String,
    fileName: String,
    fileId: String,
    private val message: Any? = null,
    private val meta: Any? = null,
    private val ttl: Int? = null,
    private val shouldStore: Boolean? = null,
    private val spaceId: SpaceId? = null,
    private val type: String? = null,
    pubNub: PubNub
) : Endpoint<List<Any>, PNPublishFileMessageResult>(pubNub) {
    private val pnFile = PNBaseFile(fileId, fileName)

    companion object {
        internal const val SPACE_ID_QUERY_PARAM = "space-id"
        internal const val TYPE_QUERY_PARAM = "type"
    }

    @Throws(PubNubException::class)
    override fun validateParams() {
        if (channel.isEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
    }

    @Throws(PubNubException::class)
    override fun doWork(queryParams: HashMap<String, String>): Call<List<Any>> {
        val stringifiedMessage: String = pubnub.mapper.toJson(FileUploadNotification(message, pnFile))
        val messageAsString = if (pubnub.configuration.cipherKey.isValid()) {
            val crypto = Crypto(pubnub.configuration.cipherKey, pubnub.configuration.useRandomInitializationVector)
            crypto.encrypt(stringifiedMessage).quoted()
        } else {
            stringifiedMessage
        }
        meta?.let {
            val stringifiedMeta: String = pubnub.mapper.toJson(it)
            queryParams["meta"] = stringifiedMeta
        }
        shouldStore?.numericString?.let { queryParams["store"] = it }
        ttl?.let { queryParams["ttl"] = it.toString() }
        spaceId?.run { queryParams[SPACE_ID_QUERY_PARAM] = spaceId.value }
        type?.run { queryParams[TYPE_QUERY_PARAM] = this }

        return pubnub.retrofitManager.filesService.notifyAboutFileUpload(
            pubnub.configuration.publishKey,
            pubnub.configuration.subscribeKey,
            channel,
            messageAsString,
            queryParams
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

    internal class Factory(private val pubNub: PubNub) {
        fun create(
            channel: String,
            fileName: String,
            fileId: String,
            message: Any? = null,
            meta: Any? = null,
            ttl: Int? = null,
            shouldStore: Boolean? = null,
            spaceId: SpaceId? = null,
            type: String? = null,
        ): ExtendedRemoteAction<PNPublishFileMessageResult> {
            return PublishFileMessage(
                channel = channel,
                fileName = fileName,
                fileId = fileId,
                message = message,
                meta = meta,
                ttl = ttl,
                shouldStore = shouldStore,
                pubNub = pubNub,
                spaceId = spaceId,
                type = type
            )
        }
    }
}
