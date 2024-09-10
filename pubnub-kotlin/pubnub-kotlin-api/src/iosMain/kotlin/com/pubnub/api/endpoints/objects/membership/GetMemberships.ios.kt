package com.pubnub.api.endpoints.objects.membership

import cocoapods.PubNubSwift.KMPMembershipMetadata
import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.getMembershipsWithUuid
import com.pubnub.api.models.consumer.objects.PNMembershipKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.createPNChannelMembership
import com.pubnub.kmp.createPubNubHashedPage
import com.pubnub.kmp.filterAndMap
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler3
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber

/**
 * @see [PubNub.getMemberships]
 */
actual interface GetMemberships : PNFuture<PNChannelMembershipArrayResult>

@OptIn(ExperimentalForeignApi::class)
class GetMembershipsImpl(
    private val pubnub: KMPPubNub,
    private val uuid: String?,
    private val limit: Int?,
    private val page: PNPage?,
    private val filter: String?,
    private val sort: Collection<PNSortKey<PNMembershipKey>>,
    private val includeCount: Boolean,
    private val includeCustom: Boolean,
    private val includeChannelDetails: PNChannelDetailsLevel?,
    private val includeChannelType: Boolean
) : GetMemberships {
    override fun async(callback: Consumer<Result<PNChannelMembershipArrayResult>>) {
        pubnub.getMembershipsWithUuid(
            uuid = uuid,
            limit = limit?.let { NSNumber(it) },
            page = createPubNubHashedPage(from = page),
            filter = filter,
            sort = sort.map { it.key.fieldName },
            includeCount = includeCount,
            includeCustom = includeCustom,
            includeChannelFields = includeChannelDetails == PNChannelDetailsLevel.CHANNEL || includeChannelDetails == PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM,
            includeChannelCustomFields = includeChannelDetails == PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM,
            includeChannelType = includeChannelType,
            onSuccess = callback.onSuccessHandler3 { memberships, totalCount, page ->
                PNChannelMembershipArrayResult(
                    status = 200,
                    data = memberships.filterAndMap { rawValue: KMPMembershipMetadata -> createPNChannelMembership(rawValue) },
                    totalCount = totalCount?.intValue,
                    next = page?.start()?.let { hash -> PNPage.PNNext(pageHash = hash) },
                    prev = page?.end()?.let { hash -> PNPage.PNPrev(pageHash = hash) }
                )
            },
            onFailure = callback.onFailureHandler()
        )
    }
}
