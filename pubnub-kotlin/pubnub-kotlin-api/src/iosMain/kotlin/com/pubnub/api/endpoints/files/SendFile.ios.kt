package com.pubnub.api.endpoints.files

import cocoapods.PubNubSwift.PubNubDataContentObjC
import cocoapods.PubNubSwift.PubNubFileContentObjC
import cocoapods.PubNubSwift.PubNubInputStreamContentObjC
import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.PubNubUploadableObjC
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
import platform.Foundation.NSInputStream
import platform.Foundation.NSNumber

/**
 * @see [PubNub.sendFile]
 */
actual interface SendFile : PNFuture<PNFileUploadResult>

@OptIn(ExperimentalForeignApi::class)
class SendFileImpl(
    private val pubnub: PubNubObjC,
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

    private fun mapContent(content: Uploadable): PubNubUploadableObjC? {
        return when (content) {
            is DataUploadContent -> return PubNubDataContentObjC(
                data = content.data,
                contentType = content.contentType
            )
            is FileUploadContent -> PubNubFileContentObjC(
                fileURL = content.url
            )
            is StreamUploadContent -> PubNubInputStreamContentObjC(
                stream = NSInputStream(content.url),
                contentType = content.contentType,
                contentLength = content.contentLength.toLong()
            )
            else -> null
        }
    }
}
