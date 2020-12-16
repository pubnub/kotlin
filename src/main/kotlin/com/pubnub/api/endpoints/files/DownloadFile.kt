package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.files.PNDownloadFileResult
import com.pubnub.api.vendor.FileEncryptionUtil
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNub.downloadFile]
 */
class DownloadFile(
    private val channel: String,
    private val fileName: String,
    private val fileId: String,
    private val cipherKey: String? = null,
    pubNub: PubNub
) : Endpoint<ResponseBody, PNDownloadFileResult>(pubNub) {

    @Throws(PubNubException::class)
    override fun validateParams() {
        if (channel.isEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
    }

    @Throws(PubNubException::class)
    override fun doWork(queryParams: HashMap<String, String>): Call<ResponseBody> =
        pubnub.retrofitManager.filesService.downloadFile(
            pubnub.configuration.subscribeKey,
            channel,
            fileId,
            fileName,
            queryParams
        )

    @Throws(PubNubException::class)
    override fun createResponse(input: Response<ResponseBody>): PNDownloadFileResult {
        if (!input.isSuccessful) {
            throw PubNubException(PubNubError.HTTP_ERROR)
        }
        if (input.body() == null) {
            throw PubNubException(PubNubError.INTERNAL_ERROR)
        }
        val effectiveCipherKey: String? = FileEncryptionUtil.effectiveCipherKey(pubnub, cipherKey)
        val byteStream = if (effectiveCipherKey == null) input.body()!!.byteStream()
        else FileEncryptionUtil.decrypt(input.body()!!.byteStream(), effectiveCipherKey)

        return PNDownloadFileResult(fileName, byteStream)
    }

    override fun getAffectedChannels() = listOf(channel)
    override fun getAffectedChannelGroups(): List<String> = listOf()
    override fun operationType(): PNOperationType = PNOperationType.FileOperation
    override fun isAuthRequired(): Boolean = true
    override fun isSubKeyRequired(): Boolean = true
    override fun isPubKeyRequired(): Boolean = false
}
