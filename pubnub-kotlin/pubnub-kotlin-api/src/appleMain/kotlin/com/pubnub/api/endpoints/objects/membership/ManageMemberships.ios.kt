package com.pubnub.api.endpoints.objects.membership

import cocoapods.PubNubSwift.KMPAnyJSON
import cocoapods.PubNubSwift.KMPChannelMetadata
import cocoapods.PubNubSwift.KMPMembershipIncludeFields
import cocoapods.PubNubSwift.KMPMembershipMetadata
import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.removeMembershipsWithChannels
import cocoapods.PubNubSwift.setMembershipsWithChannels
import com.pubnub.api.models.consumer.objects.PNMembershipKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.membership.ChannelMembershipInput
import com.pubnub.api.models.consumer.objects.membership.MembershipInclude
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
 * @see [PubNub.manageMemberships]
 */
actual interface ManageMemberships : PNFuture<PNChannelMembershipArrayResult>

@OptIn(ExperimentalForeignApi::class)
class AddMembershipsImpl(
    private val pubnub: KMPPubNub,
    private val channels: List<ChannelMembershipInput>,
    private val userId: String?,
    private val limit: Int?,
    private val page: PNPage?,
    private val filter: String?,
    private val sort: Collection<PNSortKey<PNMembershipKey>>,
    private val includeFields: MembershipInclude
) : ManageMemberships {
    override fun async(callback: Consumer<Result<PNChannelMembershipArrayResult>>) {
        pubnub.setMembershipsWithChannels(
            channels = channels.map { KMPChannelMetadata(it.channel, KMPAnyJSON(it.custom), it.type, it.status) },
            userId = userId,
            limit = limit?.let { NSNumber(it) },
            page = createPubNubHashedPage(from = page),
            filter = filter,
            sort = sort.map { it.key.fieldName },
            include = KMPMembershipIncludeFields(
                includeCustom = includeFields.includeCustom,
                includeStatus = includeFields.includeStatus,
                includeType = includeFields.includeType,
                includeTotalCount = includeFields.includeTotalCount,
                includeChannel = includeFields.includeChannel,
                includeChannelCustom = includeFields.includeChannelCustom,
                includeChannelType = includeFields.includeChannelType,
                includeChannelStatus = includeFields.includeChannelStatus
            ),
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

@OptIn(ExperimentalForeignApi::class)
class RemoveMembershipsImpl(
    private val pubnub: KMPPubNub,
    private val channels: List<String>,
    private val userId: String?,
    private val limit: Int?,
    private val page: PNPage?,
    private val filter: String?,
    private val sort: Collection<PNSortKey<PNMembershipKey>>,
    private val includeFields: MembershipInclude
) : ManageMemberships {
    override fun async(callback: Consumer<Result<PNChannelMembershipArrayResult>>) {
        pubnub.removeMembershipsWithChannels(
            channels = channels,
            userId = userId,
            limit = limit?.let { NSNumber(it) },
            page = createPubNubHashedPage(from = page),
            filter = filter,
            sort = sort.map { it.key.fieldName },
            include = KMPMembershipIncludeFields(
                includeCustom = includeFields.includeCustom,
                includeStatus = includeFields.includeStatus,
                includeType = includeFields.includeType,
                includeTotalCount = includeFields.includeTotalCount,
                includeChannel = includeFields.includeChannel,
                includeChannelCustom = includeFields.includeChannelCustom,
                includeChannelType = includeFields.includeChannelType,
                includeChannelStatus = includeFields.includeChannelStatus
            ),
            onSuccess = callback.onSuccessHandler3 { memberships, totalCount, next ->
                PNChannelMembershipArrayResult(
                    status = 200,
                    data = memberships.filterAndMap { rawValue: KMPMembershipMetadata -> createPNChannelMembership(rawValue) },
                    totalCount = totalCount?.intValue,
                    next = next?.end()?.let { hash -> PNPage.PNNext(pageHash = hash) },
                    prev = next?.start()?.let { hash -> PNPage.PNPrev(pageHash = hash) }
                )
            },
            onFailure = callback.onFailureHandler()
        )
    }
}
