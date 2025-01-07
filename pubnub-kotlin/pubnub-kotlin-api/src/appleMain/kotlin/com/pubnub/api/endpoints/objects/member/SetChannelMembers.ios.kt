package com.pubnub.api.endpoints.objects.member

import cocoapods.PubNubSwift.KMPAnyJSON
import cocoapods.PubNubSwift.KMPMemberIncludeFields
import cocoapods.PubNubSwift.KMPMembershipMetadata
import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.KMPUserMetadata
import cocoapods.PubNubSwift.setChannelMembersWithChannel
import com.pubnub.api.models.consumer.objects.PNMemberKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.member.MemberInclude
import com.pubnub.api.models.consumer.objects.member.MemberInput
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
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
    private val channelId: String,
    private val users: List<MemberInput>,
    private val limit: Int?,
    private val page: PNPage?,
    private val filter: String?,
    private val sort: Collection<PNSortKey<PNMemberKey>>,
    private val includeFields: MemberInclude
) : ManageChannelMembers {
    override fun async(callback: Consumer<Result<PNMemberArrayResult>>) {
        pubnub.setChannelMembersWithChannel(
            channel = channelId,
            users = users.map { KMPUserMetadata(it.uuid, KMPAnyJSON(it.custom), it.type, it.status) },
            limit = limit?.let { NSNumber(it) },
            page = createPubNubHashedPage(from = page),
            filter = filter,
            sort = sort.map { it.key.fieldName },
            include = KMPMemberIncludeFields(
                includeCustom = includeFields.includeCustom,
                includeStatus = includeFields.includeStatus,
                includeType = includeFields.includeType,
                includeTotalCount = includeFields.includeTotalCount,
                includeUser = includeFields.includeUser,
                includeUserCustom = includeFields.includeUserCustom,
                includeUserType = includeFields.includeUserType,
                includeUserStatus = includeFields.includeUserStatus
            ),
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
