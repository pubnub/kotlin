package com.pubnub.kmp.endpoints.objects.membership

import cocoapods.PubNubSwift.PubNubMembershipMetadataObjC
import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.removeMembershipsWithChannels
import cocoapods.PubNubSwift.setMembershipsWithChannels
import com.pubnub.api.models.consumer.objects.PNMembershipKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.membership.ChannelMembershipInput
import com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.createPNChannelMembership
import com.pubnub.kmp.createPubNubHashedPage
import com.pubnub.kmp.endpoints.objects.membership.ManageMemberships
import com.pubnub.kmp.filterAndMap
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler3
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber

@OptIn(ExperimentalForeignApi::class)
class AddMembershipsImpl(
    private val pubnub: PubNubObjC,
    private val channels: List<ChannelMembershipInput>,
    private val uuid: String?,
    private val limit: Int?,
    private val page: PNPage?,
    private val filter: String?,
    private val sort: Collection<PNSortKey<PNMembershipKey>>,
    private val includeCount: Boolean,
    private val includeCustom: Boolean,
    private val includeChannelDetails: PNChannelDetailsLevel?
): ManageMemberships {
    override fun async(callback: Consumer<Result<PNChannelMembershipArrayResult>>) {
        pubnub.setMembershipsWithChannels(
            channels = channels,
            uuid = uuid,
            limit = limit?.let { NSNumber(it) },
            page = createPubNubHashedPage(from = page),
            filter = filter,
            sort = sort.map { it.key.fieldName },
            includeCount = includeCount,
            includeCustom = includeCustom,
            includeChannelFields = includeChannelDetails == PNChannelDetailsLevel.CHANNEL || includeChannelDetails == PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM,
            includeChannelCustomFields = includeChannelDetails == PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM,
            onSuccess = callback.onSuccessHandler3 { memberships, totalCount, next ->
                PNChannelMembershipArrayResult(
                    status = 200,
                    data = memberships.filterAndMap { rawValue: PubNubMembershipMetadataObjC -> createPNChannelMembership(rawValue) },
                    totalCount = totalCount?.intValue,
                    next = next?.end()?.let { hash -> PNPage.PNNext(pageHash = hash) },
                    prev = next?.start()?.let { hash -> PNPage.PNPrev(pageHash = hash) }
                )
            },
            onFailure = callback.onFailureHandler()
        )
    }
}

@OptIn(ExperimentalForeignApi::class)
class RemoveMembershipsImpl(
    private val pubnub: PubNubObjC,
    private val channels: List<String>,
    private val uuid: String?,
    private val limit: Int?,
    private val page: PNPage?,
    private val filter: String?,
    private val sort: Collection<PNSortKey<PNMembershipKey>>,
    private val includeCount: Boolean,
    private val includeCustom: Boolean,
    private val includeChannelDetails: PNChannelDetailsLevel?
): ManageMemberships {
    override fun async(callback: Consumer<Result<PNChannelMembershipArrayResult>>) {
        pubnub.removeMembershipsWithChannels(
            channels = channels,
            uuid = uuid,
            limit = limit?.let { NSNumber(it) },
            page = createPubNubHashedPage(from = page),
            filter = filter,
            sort = sort.map { it.key.fieldName },
            includeCount = includeCount,
            includeCustom = includeCustom,
            includeChannelFields = includeChannelDetails == PNChannelDetailsLevel.CHANNEL || includeChannelDetails == PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM,
            includeChannelCustomFields = includeChannelDetails == PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM,
            onSuccess = callback.onSuccessHandler3 { memberships, totalCount, next ->
                PNChannelMembershipArrayResult(
                    status = 200,
                    data = memberships.filterAndMap { rawValue: PubNubMembershipMetadataObjC -> createPNChannelMembership(rawValue) },
                    totalCount = totalCount?.intValue,
                    next = next?.end()?.let { hash -> PNPage.PNNext(pageHash = hash) },
                    prev = next?.start()?.let { hash -> PNPage.PNPrev(pageHash = hash) }
                )
            },
            onFailure = callback.onFailureHandler()
        )
    }
}
