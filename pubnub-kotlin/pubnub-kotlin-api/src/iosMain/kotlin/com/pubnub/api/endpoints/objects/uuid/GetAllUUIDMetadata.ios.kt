package com.pubnub.api.endpoints.objects.uuid

import cocoapods.PubNubSwift.PubNubChannelMetadataObjC
import cocoapods.PubNubSwift.PubNubHashedPageObjC
import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.PubNubSortPropertyObjC
import cocoapods.PubNubSwift.PubNubUUIDMetadataObjC
import cocoapods.PubNubSwift.getAllChannelMetadataWithLimit
import cocoapods.PubNubSwift.getAllUUIDMetadataWithLimit
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.objects.PNKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult
import com.pubnub.api.onFailureHandler
import com.pubnub.api.onSuccessHandler
import com.pubnub.api.onSuccessHandler3
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber

/**
 * @see [PubNub.getAllUUIDMetadata]
 */
actual interface GetAllUUIDMetadata : Endpoint<PNUUIDMetadataArrayResult>

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
            page = PubNubHashedPageObjC(
                start = if (page is PNPage.PNNext) { page.pageHash } else { null },
                end = if (page is PNPage.PNPrev) { page.pageHash } else { null },
                totalCount = null
            ),
            filter = filter,
            sort = sort.map {
                PubNubSortPropertyObjC(
                    key = it.key.fieldName,
                    direction = it.dir
                )
            },
            includeCount = includeCount,
            includeCustom = includeCustom,
            onSuccess = callback.onSuccessHandler3() { data, totalCount, next ->
                PNUUIDMetadataArrayResult(
                    status = 200,
                    data = (data as List<PubNubUUIDMetadataObjC>).map { metadata ->
                        PNUUIDMetadata(
                            id = metadata.id(),
                            name = metadata.name(),
                            externalId = metadata.externalId(),
                            profileUrl = metadata.profileUrl(),
                            email = metadata.email(),
                            custom = metadata.custom() as? Map<String, Any?>, // TODO: Verify
                            updated = metadata.updated(),
                            eTag = metadata.eTag(),
                            type = metadata.type(),
                            status = metadata.status()
                        )
                    },
                    totalCount = totalCount?.intValue ?: 0,
                    next = next?.end()?.let { hash -> PNPage.PNNext(pageHash = hash) },
                    prev = next?.start()?.let { hash -> PNPage.PNPrev(pageHash = hash) }
                )
            },
            onFailure = callback.onFailureHandler()
        )
    }
}