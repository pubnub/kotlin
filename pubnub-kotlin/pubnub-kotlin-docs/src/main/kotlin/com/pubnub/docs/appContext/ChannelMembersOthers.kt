package com.pubnub.docs.appContext

import com.pubnub.api.models.consumer.objects.PNMemberKey
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.member.MemberInclude
import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.docs.SnippetBase

class ChannelMembersOthers : SnippetBase() {
    private fun getChannelMembersBasic() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/objects#basic-usage-12

        val pubnub = createPubNub()

        // snippet.getChannelMembersBasic
        pubnub.getChannelMembers("myChannel")
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        // snippet.end
    }

    private fun setChannelMembersBasic() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/objects#basic-usage-13

        val pubnub = createPubNub()

        // snippet.setChannelMembersBasic
        pubnub.setChannelMembers(
            channel = "myChannel",
            users = listOf(
                PNMember.Partial(
                    uuidId = "myUserId",
                    custom = mapOf("custom" to "customValue"),
                    status = "status",
                    type = "typeABC"
                )
            ),
            include = MemberInclude(includeUser = true, includeStatus = true, includeType = true),
            sort = listOf(PNSortKey.PNAsc(PNMemberKey.STATUS)),
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end
    }

    private fun removeChannelMembersBasic() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/objects#basic-usage-14

        val pubnub = createPubNub()

        // snippet.removeChannelMembersBasic
        pubnub.removeChannelMembers(
            channel = "myChannel",
            userIds = listOf("myUserId"),
            include = MemberInclude(includeUser = true, includeStatus = true, includeType = true),
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end
    }

    private fun manageChannelMembersBasic() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/objects#basic-usage-15

        val pubnub = createPubNub()

        // snippet.manageChannelMembersBasic
        val pnMemberArrayResult: PNMemberArrayResult = pubnub.manageChannelMembers(
            channel = "myChannel",
            usersToSet = listOf(PNMember.Partial(uuidId = "myUserId", status = "myStatus", type = "distinctType")),
            userIdsToRemove = listOf(),
            include = MemberInclude(includeStatus = true, includeType = true)
        ).sync()
        // snippet.end
    }
}
