package com.pubnub.internal.endpoints.objects.member

import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.BasePubNub.PubNubImpl
import com.pubnub.internal.endpoints.objects.internal.CollectionQueryParameters
import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam
import com.pubnub.internal.extension.toPNMemberArrayResult
import com.pubnub.internal.models.consumer.objects.member.MemberInput
import com.pubnub.internal.models.consumer.objects.member.PNMember
import com.pubnub.internal.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.internal.models.server.objects_api.ChangeMemberInput
import com.pubnub.internal.models.server.objects_api.EntityArrayEnvelope
import com.pubnub.internal.models.server.objects_api.ServerMemberInput
import com.pubnub.internal.models.server.objects_api.UUIDId
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubImpl.manageChannelMembers]
 */
class ManageChannelMembers(
    pubnub: PubNubImpl,
    private val uuidsToSet: Collection<MemberInput>,
    private val uuidsToRemove: Collection<String>,
    private val channel: String,
    private val collectionQueryParameters: CollectionQueryParameters,
    private val includeQueryParam: IncludeQueryParam
) : com.pubnub.internal.Endpoint<EntityArrayEnvelope<PNMember>, PNMemberArrayResult>(pubnub), IManageChannelMembers {
    override fun doWork(queryParams: HashMap<String, String>): Call<EntityArrayEnvelope<PNMember>> {
        val params =
            queryParams + collectionQueryParameters.createCollectionQueryParams() + includeQueryParam.createIncludeQueryParams()

        return pubnub.retrofitManager.objectsService.patchChannelMembers(
            channel = channel,
            subKey = pubnub.configuration.subscribeKey,
            options = params,
            body = ChangeMemberInput(
                delete = uuidsToRemove.map { ServerMemberInput(UUIDId(id = it)) },
                set = uuidsToSet.map { ServerMemberInput(uuid = UUIDId(id = it.uuid), custom = it.custom, status = it.status) }
            )
        )
    }

    override fun createResponse(input: Response<EntityArrayEnvelope<PNMember>>): PNMemberArrayResult =
        input.toPNMemberArrayResult()

    override fun operationType(): PNOperationType = PNOperationType.ObjectsOperation()

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.APP_CONTEXT
}
