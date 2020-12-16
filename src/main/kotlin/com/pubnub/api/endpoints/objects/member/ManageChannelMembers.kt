package com.pubnub.api.endpoints.objects.member

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.objects.internal.ReturningCollection
import com.pubnub.api.endpoints.objects.internal.ReturningUUIDDetailsCustom
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.api.models.consumer.objects.member.PNUUIDWithCustom
import com.pubnub.api.models.server.objects_api.ChangeMemberInput
import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope
import com.pubnub.api.models.server.objects_api.MemberInput
import com.pubnub.api.models.server.objects_api.UUIDId
import com.pubnub.extension.toPNMemberArrayResult
import retrofit2.Call
import retrofit2.Response
import java.util.HashMap

/**
 * @see [PubNub.manageChannelMembers]
 */
class ManageChannelMembers(
    pubnub: PubNub,
    private val uuidsToSet: Collection<PNUUIDWithCustom>,
    private val uuidsToRemove: Collection<String>,
    private val channel: String,
    private val returningCollection: ReturningCollection,
    private val withUUIDDetailsCustom: ReturningUUIDDetailsCustom
) : Endpoint<EntityArrayEnvelope<PNMember>, PNMemberArrayResult>(pubnub) {
    override fun doWork(queryParams: HashMap<String, String>): Call<EntityArrayEnvelope<PNMember>> {
        val params = queryParams + returningCollection.createCollectionQueryParams() +
            withUUIDDetailsCustom.createIncludeQueryParams()
        return pubnub.retrofitManager.objectsService.patchChannelMembers(
            channel = channel,
            subKey = pubnub.configuration.subscribeKey,
            options = params,
            body = ChangeMemberInput(
                delete = uuidsToRemove.map { MemberInput(UUIDId(id = it)) },
                set = uuidsToSet.map { MemberInput(uuid = UUIDId(id = it.uuid), custom = it.custom) }
            )
        )
    }

    override fun createResponse(input: Response<EntityArrayEnvelope<PNMember>>): PNMemberArrayResult? =
        input.toPNMemberArrayResult()

    override fun operationType(): PNOperationType = PNOperationType.ObjectsOperation()
}
