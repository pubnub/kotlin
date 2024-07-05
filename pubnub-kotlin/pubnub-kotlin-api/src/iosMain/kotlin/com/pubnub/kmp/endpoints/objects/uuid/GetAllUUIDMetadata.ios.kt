package com.pubnub.kmp.endpoints.objects.uuid

import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.PubNubUUIDMetadataObjC
import cocoapods.PubNubSwift.getAllUUIDMetadataWithLimit
import com.pubnub.api.models.consumer.objects.PNKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.createObjectSortProperties
import com.pubnub.kmp.createPNUUIDMetadata
import com.pubnub.kmp.createPubNubHashedPage
import com.pubnub.kmp.endpoints.objects.uuid.GetAllUUIDMetadata
import com.pubnub.kmp.filterAndMap
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler3
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber

@OptIn(ExperimentalForeignApi::class)
class GetAllUUIDMetadataImpl(
    private val pubnub: PubNubObjC,
    private val limit: Int?,
    private val page: PNPage?,
    private val filter: String?,
    private val sort: Collection<PNSortKey<PNKey>>,
    private val includeCount: Boolean,
    private val includeCustom: Boolean
) : GetAllUUIDMetadata {
    override fun async(callback: Consumer<Result<PNUUIDMetadataArrayResult>>) {
        pubnub.getAllUUIDMetadataWithLimit(
            limit = limit?.let { NSNumber(it) },
            page = createPubNubHashedPage(from = page),
            filter = filter,
            sort = createObjectSortProperties(from = sort),
            includeCount = includeCount,
            includeCustom = includeCustom,
            onSuccess = callback.onSuccessHandler3 { uuids, totalCount, next ->
                PNUUIDMetadataArrayResult(
                    status = 200,
                    data = uuids.filterAndMap { rawValue: PubNubUUIDMetadataObjC -> createPNUUIDMetadata(from = rawValue) },
                    totalCount = totalCount?.intValue ?: 0,
                    next = next?.end()?.let { hash -> PNPage.PNNext(pageHash = hash) },
                    prev = next?.start()?.let { hash -> PNPage.PNPrev(pageHash = hash) }
                )
            },
            onFailure = callback.onFailureHandler()
        )
    }
}