package com.pubnub.api.endpoints.objects.channel

import cocoapods.PubNubSwift.PubNubChannelMetadataObjC
import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.getAllChannelMetadataWithLimit
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.objects.PNKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.createObjectSortProperties
import com.pubnub.kmp.createPNChannelMetadata
import com.pubnub.kmp.createPubNubHashedPage
import com.pubnub.kmp.transform
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler3
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber

/**
 * @see [PubNub.getAllChannelMetadata]
 */
actual interface GetAllChannelMetadata : PNFuture<PNChannelMetadataArrayResult>

@OptIn(ExperimentalForeignApi::class)
class GetAllChannelMetadataImpl(
    private val pubnub: PubNubObjC,
    private val limit: Int?,
    private val page: PNPage?,
    private val filter: String?,
    private val sort: Collection<PNSortKey<PNKey>>,
    private val includeCount: Boolean,
    private val includeCustom: Boolean
): GetAllChannelMetadata {
    override fun async(callback: Consumer<Result<PNChannelMetadataArrayResult>>) {
        pubnub.getAllChannelMetadataWithLimit(
            limit = limit?.let { NSNumber(it) },
            page = createPubNubHashedPage(from = page),
            filter = filter,
            sort = createObjectSortProperties(from = sort),
            includeCount = includeCount,
            includeCustom = includeCustom,
            onSuccess = callback.onSuccessHandler3 { channels, totalCount, next ->
                PNChannelMetadataArrayResult(
                    status = 200,
                    data = channels.transform { rawValue: PubNubChannelMetadataObjC -> createPNChannelMetadata(rawValue) },
                    totalCount = totalCount?.intValue,
                    next = next?.end()?.let { hash -> PNPage.PNNext(pageHash = hash) },
                    prev = next?.start()?.let { hash -> PNPage.PNPrev(pageHash = hash) }
                )
            },
            onFailure = callback.onFailureHandler()
        )
    }
}