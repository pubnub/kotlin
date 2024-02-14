package com.pubnub.internal.endpoints.files

import com.pubnub.internal.Endpoint
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.files.PNDeleteFileResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.BasePubNub.PubNubImpl
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubImpl.deleteFile]
 */
class DeleteFile(
    private val channel: String,
    private val fileName: String,
    private val fileId: String,
    pubNub: PubNubImpl
) : Endpoint<Unit, PNDeleteFileResult?>(pubNub), IDeleteFile {
    @Throws(PubNubException::class)
    override fun validateParams() {
        if (channel.isEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
    }

    @Throws(PubNubException::class)
    override fun doWork(queryParams: HashMap<String, String>): Call<Unit> =
        pubnub.retrofitManager.filesService.deleteFile(
            pubnub.configuration.subscribeKey,
            channel,
            fileId,
            fileName,
            queryParams
        )

    @Throws(PubNubException::class)
    override fun createResponse(input: Response<Unit>): PNDeleteFileResult = if (input.isSuccessful) {
        PNDeleteFileResult(input.code())
    } else {
        throw PubNubException(PubNubError.HTTP_ERROR)
    }

    override fun getAffectedChannels() = listOf(channel)
    override fun getAffectedChannelGroups(): List<String> = listOf()
    override fun operationType(): PNOperationType = PNOperationType.FileOperation
    override fun isAuthRequired(): Boolean = true
    override fun isSubKeyRequired(): Boolean = true
    override fun isPubKeyRequired(): Boolean = false
    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.FILE_PERSISTENCE
}
