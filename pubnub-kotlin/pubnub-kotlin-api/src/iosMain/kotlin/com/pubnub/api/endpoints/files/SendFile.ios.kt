package com.pubnub.api.endpoints.files

import cocoapods.PubNubSwift.KMPDataUploadContent
import cocoapods.PubNubSwift.KMPFileUploadContent
import cocoapods.PubNubSwift.KMPInputStreamUploadContent
import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.KMPUploadable
import cocoapods.PubNubSwift.sendFileWithChannel
import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.files.PNBaseFile
import com.pubnub.api.models.consumer.files.PNFileUploadResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.DataUploadContent
import com.pubnub.kmp.FileUploadContent
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.StreamUploadContent
import com.pubnub.kmp.Uploadable
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler2
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber

/**
 * @see [PubNub.sendFile]
 */
actual interface SendFile : PNFuture<PNFileUploadResult>

@OptIn(ExperimentalForeignApi::class)
class SendFileImpl(
    private val pubnub: KMPPubNub,
    private val channel: String,
    private val fileName: String,
    private val inputStream: Uploadable,
    private val message: Any?,
    private val meta: Any?,
    private val ttl: Int?,
    private val shouldStore: Boolean?,
) : SendFile {
    override fun async(callback: Consumer<Result<PNFileUploadResult>>) {
        mapContent(inputStream)?.let { content ->
            pubnub.sendFileWithChannel(
                channel = channel,
                fileName = fileName,
                message = message,
                content = content,
                meta = meta,
                ttl = ttl?.let { NSNumber(it) },
                shouldStore = shouldStore?.let { NSNumber(it) },
                onSuccess = callback.onSuccessHandler2 { uploadedFile, timetoken ->
                    PNFileUploadResult(
                        status = 200,
                        timetoken = timetoken.toLong(),
                        file = PNBaseFile(
                            id = uploadedFile?.id().orEmpty(),
                            name = uploadedFile?.name().orEmpty()
                        )
                    )
                },
                onFailure = callback.onFailureHandler()
            )
        } ?: callback.accept(Result.failure(PubNubException("Invalid argument $inputStream")))
    }

    private fun mapContent(content: Uploadable): KMPUploadable? {
        return when (content) {
            is DataUploadContent -> return KMPDataUploadContent(
                data = content.data,
                contentType = content.contentType
            )
            is FileUploadContent -> KMPFileUploadContent(
                fileURL = content.url
            )
            is StreamUploadContent -> KMPInputStreamUploadContent(
                stream = content.stream,
                contentType = content.contentType,
                contentLength = content.contentLength.toLong()
            )
            else -> null
        }
    }
}
