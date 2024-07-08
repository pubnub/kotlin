package com.pubnub.api.endpoints.objects.member

import cocoapods.PubNubSwift.AnyJSONObjC
import cocoapods.PubNubSwift.PubNubMembershipMetadataObjC
import cocoapods.PubNubSwift.PubNubObjC
import cocoapods.PubNubSwift.PubNubUUIDMetadataObjC
import cocoapods.PubNubSwift.removeChannelMembersWithChannel
import cocoapods.PubNubSwift.setChannelMembersWithChannel
import com.pubnub.api.models.consumer.objects.PNMemberKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.member.MemberInput
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
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
    private val pubnub: PubNubObjC,
    private val channel: String,
    private val uuids: List<MemberInput>,
    private val limit: Int?,
    private val page: PNPage?,
    private val filter: String?,
    private val sort: Collection<PNSortKey<PNMemberKey>>,
    private val includeCount: Boolean,
    private val includeCustom: Boolean,
    private val includeUUIDDetails: PNUUIDDetailsLevel?
): ManageChannelMembers {
    override fun async(callback: Consumer<Result<PNMemberArrayResult>>) {
        pubnub.setChannelMembersWithChannel(
            channel = channel,
            uuids = uuids.map { PubNubUUIDMetadataObjC(id = it.uuid, custom = AnyJSONObjC(it.custom?.value), status = it.status) },
            limit = limit?.let { NSNumber(it) },
            page = createPubNubHashedPage(from = page),
            filter = filter,
            sort = sort.map { it.key.fieldName },
            includeCount = includeCount,
            includeCustom = includeCustom,
            includeUUIDFields = includeUUIDDetails == PNUUIDDetailsLevel.UUID || includeUUIDDetails == PNUUIDDetailsLevel.UUID_WITH_CUSTOM,
            includeUUIDCustomFields = includeUUIDDetails == PNUUIDDetailsLevel.UUID_WITH_CUSTOM,
            onSuccess = callback.onSuccessHandler3 { memberships, totalCount, next ->
                PNMemberArrayResult(
                    status = 200,
                    data = memberships.filterAndMap { rawValue: PubNubMembershipMetadataObjC -> createPNMember(rawValue) },
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
class RemoveChannelMembersImpl(
    private val pubnub: PubNubObjC,
    private val channel: String,
    private val uuids: List<String>,
    private val limit: Int?,
    private val page: PNPage?,
    private val filter: String?,
    private val sort: Collection<PNSortKey<PNMemberKey>>,
    private val includeCount: Boolean,
    private val includeCustom: Boolean,
    private val includeUUIDDetails: PNUUIDDetailsLevel?
): ManageChannelMembers {
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
            onSuccess = callback.onSuccessHandler3 { memberships, totalCount, next ->
                PNMemberArrayResult(
                    status = 200,
                    data = memberships.filterAndMap { rawValue: PubNubMembershipMetadataObjC -> createPNMember(rawValue) },
                    totalCount = totalCount?.intValue,
                    next = next?.end()?.let { hash -> PNPage.PNNext(pageHash = hash) },
                    prev = next?.start()?.let { hash -> PNPage.PNPrev(pageHash = hash) }
                )
            },
            onFailure = callback.onFailureHandler()
        )
    }
}
