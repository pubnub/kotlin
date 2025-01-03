package com.pubnub.api.endpoints.objects.uuid

import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.KMPUserIncludeFields
import cocoapods.PubNubSwift.KMPUserMetadata
import cocoapods.PubNubSwift.getAllUserMetadataWithLimit
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
import com.pubnub.kmp.filterAndMap
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler3
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber

/**
 * @see [PubNub.getAllUUIDMetadata]
 */
actual interface GetAllUUIDMetadata : PNFuture<PNUUIDMetadataArrayResult>

@OptIn(ExperimentalForeignApi::class)
class GetAllUUIDMetadataImpl(
    private val pubnub: KMPPubNub,
    private val limit: Int?,
    private val page: PNPage?,
    private val filter: String?,
    private val sort: Collection<PNSortKey<PNKey>>,
    private val includeCount: Boolean,
    private val includeCustom: Boolean
) : GetAllUUIDMetadata {
    override fun async(callback: Consumer<Result<PNUUIDMetadataArrayResult>>) {
        pubnub.getAllUserMetadataWithLimit(
            limit = limit?.let { NSNumber(it) },
            page = createPubNubHashedPage(from = page),
            filter = filter,
            sort = createObjectSortProperties(from = sort),
            include = KMPUserIncludeFields(
                includeCustom = includeCustom,
                includeTotalCount = includeCount,
                includeType = true,
                includeStatus = true
            ),
            onSuccess = callback.onSuccessHandler3 { uuids, totalCount, next ->
                PNUUIDMetadataArrayResult(
                    status = 200,
                    data = uuids.filterAndMap { rawValue: KMPUserMetadata -> createPNUUIDMetadata(from = rawValue) },
                    totalCount = totalCount?.intValue ?: 0,
                    next = next?.start()?.let { hash -> PNPage.PNNext(pageHash = hash) },
                    prev = next?.end()?.let { hash -> PNPage.PNPrev(pageHash = hash) }
                )
            },
            onFailure = callback.onFailureHandler()
        )
    }
}
