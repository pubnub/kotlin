package com.pubnub.api.endpoints.objects.channel

import cocoapods.PubNubSwift.PubNubChannelMetadataObjC
import cocoapods.PubNubSwift.PubNubHashedPageObjC
import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.PubNubSortPropertyObjC
import cocoapods.PubNubSwift.getAllChannelMetadataWithLimit
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.objects.PNKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult
import com.pubnub.api.onFailureHandler
import com.pubnub.api.onSuccessHandler
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber

/**
 * @see [PubNub.getAllChannelMetadata]
 */
actual interface GetAllChannelMetadata : Endpoint<PNChannelMetadataArrayResult>

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
            onSuccess = { data, totalCount, next ->
                callback.accept(Result.success(
                    PNChannelMetadataArrayResult(
                        status = 200,
                        data = (data as List<PubNubChannelMetadataObjC>).map { metadata ->
                            PNChannelMetadata(
                                id = metadata.id(),
                                name = metadata.name(),
                                description = metadata.descr(),
                                custom = metadata.custom() as? Map<String, Any?>, // TODO: Verify
                                updated = metadata.updated(),
                                eTag = metadata.eTag(),
                                type = metadata.type(),
                                status = metadata.status()
                            )
                        },
                        totalCount = totalCount?.intValue,
                        next = next?.end()?.let { hash -> PNPage.PNNext(pageHash = hash) },
                        prev = next?.start()?.let { hash -> PNPage.PNPrev(pageHash = hash) }
                    )
                ))
            },
            onFailure = callback.onFailureHandler()
        )
    }
}