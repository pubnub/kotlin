package com.pubnub.api.endpoints.files

import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.deleteFileWithChannel
import com.pubnub.api.models.consumer.files.PNDeleteFileResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessReturnValue
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * @see [PubNub.deleteFile]
 */
actual interface DeleteFile : PNFuture<PNDeleteFileResult>

@OptIn(ExperimentalForeignApi::class)
class DeleteFileImpl(
    private val pubnub: KMPPubNub,
    private val channel: String,
    private val fileName: String,
    private val fileId: String
) : DeleteFile {
    override fun async(callback: Consumer<Result<PNDeleteFileResult>>) {
        pubnub.deleteFileWithChannel(
            channel = channel,
            fileName = fileName,
            fileId = fileId,
            onSuccess = callback.onSuccessReturnValue(PNDeleteFileResult(200)),
            onFailure = callback.onFailureHandler()
        )
    }
}
