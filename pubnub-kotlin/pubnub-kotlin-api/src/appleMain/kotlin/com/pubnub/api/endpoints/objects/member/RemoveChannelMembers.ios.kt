package com.pubnub.api.endpoints.objects.member

import cocoapods.PubNubSwift.KMPMembershipMetadata
import cocoapods.PubNubSwift.KMPPubNub
import cocoapods.PubNubSwift.removeChannelMembersWithChannel
import com.pubnub.api.models.consumer.objects.PNMemberKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.member.MemberInclude
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.createPNMember
import com.pubnub.kmp.createPubNubHashedPage
import com.pubnub.kmp.filterAndMap
import com.pubnub.kmp.onFailureHandler
import com.pubnub.kmp.onSuccessHandler3
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber

@OptIn(ExperimentalForeignApi::class)
class RemoveChannelMembersImpl(
    private val pubnub: KMPPubNub,
    private val channel: String,
    private val userIds: List<String>,
    private val limit: Int?,
    private val page: PNPage?,
    private val filter: String?,
    private val sort: Collection<PNSortKey<PNMemberKey>>,
    private val includeFields: MemberInclude
) : ManageChannelMembers {
    override fun async(callback: Consumer<Result<PNMemberArrayResult>>) {
        pubnub.removeChannelMembersWithChannel(
            channel = channel,
            users = userIds,
            limit = limit?.let { NSNumber(it) },
            page = createPubNubHashedPage(from = page),
            filter = filter,
            sort = sort.map { it.key.fieldName },
            includeCount = includeFields.includeTotalCount,
            includeCustom = includeFields.includeCustom,
            includeUser = includeFields.includeUser,
            includeUserCustom = includeFields.includeUserCustom,
            includeUserType = includeFields.includeUserType,
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
