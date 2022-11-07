package com.pubnub.api.endpoints.objects.member

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.objects.internal.CollectionQueryParameters
import com.pubnub.api.endpoints.objects.internal.IncludeQueryParam
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.api.models.server.objects_api.ChangeMemberInput
import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope
import com.pubnub.api.models.server.objects_api.MemberInput
import com.pubnub.extension.toPNMemberArrayResult
import retrofit2.Call
import retrofit2.Response
import com.pubnub.api.models.consumer.objects.member.MemberInput as ConsumerMemberInput

/**
 * @see [PubNub.manageChannelMembers]
 */
class ManageChannelMembers(
    pubnub: PubNub,
    private val uuidsToSet: Collection<ConsumerMemberInput>,
    private val uuidsToRemove: Collection<String>,
    private val channel: String,
    private val collectionQueryParameters: CollectionQueryParameters,
    private val includeQueryParam: IncludeQueryParam
) : Endpoint<EntityArrayEnvelope<PNMember>, PNMemberArrayResult>(pubnub) {
    override fun doWork(queryParams: HashMap<String, String>): Call<EntityArrayEnvelope<PNMember>> {
        val params = queryParams + collectionQueryParameters.createCollectionQueryParams() +
            includeQueryParam.createIncludeQueryParams()

        return pubnub.retrofitManager.objectsService.patchChannelMembers(
            channel = channel,
            subKey = pubnub.configuration.subscribeKey,
            options = params,
            body = ChangeMemberInput(
                delete = uuidsToRemove.map { MemberInput(it) },
                set = uuidsToSet.map { MemberInput(uuid = it.uuid, custom = it.custom, status = it.status) }
            )
        )
    }

    override fun createResponse(input: Response<EntityArrayEnvelope<PNMember>>): PNMemberArrayResult? =
        input.toPNMemberArrayResult()

    override fun operationType(): PNOperationType = PNOperationType.ObjectsOperation()
}
