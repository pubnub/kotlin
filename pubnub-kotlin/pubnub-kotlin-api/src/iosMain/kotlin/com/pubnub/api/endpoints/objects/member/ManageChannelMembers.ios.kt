package com.pubnub.api.endpoints.objects.member

import cocoapods.PubNubSwift.KMPAnyJSON
import cocoapods.PubNubSwift.KMPMembershipMetadata
import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.KMPUUIDMetadata
import cocoapods.PubNubSwift.removeChannelMembersWithChannel
import cocoapods.PubNubSwift.setChannelMembersWithChannel
import com.pubnub.api.models.consumer.objects.PNMemberKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.member.MemberInput
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.createPNMember
import com.pubnub.kmp.createPubNubHashedPage
import com.pubnub.kmp.filterAndMap
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler3
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber

/**
 * @see [PubNub.manageChannelMembers]
 */
actual interface ManageChannelMembers : PNFuture<PNMemberArrayResult>

@OptIn(ExperimentalForeignApi::class)
class SetChannelMembersImpl(
    private val pubnub: KMPPubNub,
    private val channel: String,
    private val uuids: List<MemberInput>,
    private val limit: Int?,
    private val page: PNPage?,
    private val filter: String?,
    private val sort: Collection<PNSortKey<PNMemberKey>>,
    private val includeCount: Boolean,
    private val includeCustom: Boolean,
    private val includeUUIDDetails: PNUUIDDetailsLevel?,
    private val includeUUIDType: Boolean
) : ManageChannelMembers {
    override fun async(callback: Consumer<Result<PNMemberArrayResult>>) {
        pubnub.setChannelMembersWithChannel(
            channel = channel,
            uuids = uuids.map { KMPUUIDMetadata(id = it.uuid, custom = KMPAnyJSON(it.custom?.value), status = it.status) },
            limit = limit?.let { NSNumber(it) },
            page = createPubNubHashedPage(from = page),
            filter = filter,
            sort = sort.map { it.key.fieldName },
            includeCount = includeCount,
            includeCustom = includeCustom,
            includeUUIDFields = includeUUIDDetails == PNUUIDDetailsLevel.UUID || includeUUIDDetails == PNUUIDDetailsLevel.UUID_WITH_CUSTOM,
            includeUUIDCustomFields = includeUUIDDetails == PNUUIDDetailsLevel.UUID_WITH_CUSTOM,
            includeUUIDType = includeUUIDType,
            onSuccess = callback.onSuccessHandler3 { memberships, totalCount, page ->
                PNMemberArrayResult(
                    status = 200,
                    data = memberships.filterAndMap { rawValue: KMPMembershipMetadata -> createPNMember(rawValue) },
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
class RemoveChannelMembersImpl(
    private val pubnub: KMPPubNub,
    private val channel: String,
    private val uuids: List<String>,
    private val limit: Int?,
    private val page: PNPage?,
    private val filter: String?,
    private val sort: Collection<PNSortKey<PNMemberKey>>,
    private val includeCount: Boolean,
    private val includeCustom: Boolean,
    private val includeUUIDDetails: PNUUIDDetailsLevel?,
    private val includeUUIDType: Boolean
) : ManageChannelMembers {
    override fun async(callback: Consumer<Result<PNMemberArrayResult>>) {
        pubnub.removeChannelMembersWithChannel(
            channel = channel,
            uuids = uuids,
            limit = limit?.let { NSNumber(it) },
            page = createPubNubHashedPage(from = page),
            filter = filter,
            sort = sort.map { it.key.fieldName },
            includeCount = includeCount,
            includeCustom = includeCustom,
            includeUUIDFields = includeUUIDDetails == PNUUIDDetailsLevel.UUID || includeUUIDDetails == PNUUIDDetailsLevel.UUID_WITH_CUSTOM,
            includeUUIDCustomFields = includeUUIDDetails == PNUUIDDetailsLevel.UUID_WITH_CUSTOM,
            includeUUIDType = includeUUIDType,
            onSuccess = callback.onSuccessHandler3 { memberships, totalCount, next ->
                PNMemberArrayResult(
                    status = 200,
                    data = memberships.filterAndMap { rawValue: KMPMembershipMetadata -> createPNMember(rawValue) },
                    totalCount = totalCount?.intValue,
                    next = next?.end()?.let { hash -> PNPage.PNNext(pageHash = hash) },
                    prev = next?.start()?.let { hash -> PNPage.PNPrev(pageHash = hash) }
                )
            },
            onFailure = callback.onFailureHandler()
        )
    }
}
