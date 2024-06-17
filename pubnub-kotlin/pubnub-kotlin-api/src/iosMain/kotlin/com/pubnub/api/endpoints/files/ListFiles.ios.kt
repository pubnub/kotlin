package com.pubnub.api.endpoints.files

import cocoapods.PubNubSwift.PubNubFileObjC
import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.listFilesWithChannel
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.files.PNListFilesResult
import com.pubnub.api.models.consumer.files.PNUploadedFile
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.createPubNubHashedPage
import com.pubnub.kmp.transform
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler2
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber

/**
 * @see [PubNub.listFiles]
 */
actual interface ListFiles : PNFuture<PNListFilesResult>

@OptIn(ExperimentalForeignApi::class)
class ListFilesImpl(
    private val pubnub: PubNubObjC,
    private val channel: String,
    private val limit: Int?,
    private val next: PNPage.PNNext?
): ListFiles {
    override fun async(callback: Consumer<Result<PNListFilesResult>>) {
        pubnub.listFilesWithChannel(
            channel = channel,
            limit = limit?.let { NSNumber(it) },
            next = createPubNubHashedPage(from = next),
            onSuccess = callback.onSuccessHandler2 { files, nextPageHash ->
                PNListFilesResult(
                    count = files?.size ?: 0, // TODO: count property is not retrieved from ListFilesSuccessResponse in Swift SDK
                    next = nextPageHash?.let { PNPage.PNNext(pageHash = it) },
                    status = 200,
                    data = files.transform { rawValue: PubNubFileObjC ->
                        PNUploadedFile(
                            id = rawValue.id(),
                            name = rawValue.name(),
                            size = rawValue.size().toInt(),
                            created = rawValue.createdDate().toString() // TODO: Map to expected date format
                        )
                    }
                )
            },
            onFailure = callback.onFailureHandler()
        )
    }
}
