package com.pubnub.internal.endpoints.objects.member

import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubCore
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
 * @see [PubNubCore.manageChannelMembers]
 */
class ManageChannelMembersEndpoint(
    pubnub: PubNubCore,
    private val uuidsToSet: Collection<MemberInput>,
    private val uuidsToRemove: Collection<String>,
    private val channel: String,
    private val collectionQueryParameters: CollectionQueryParameters,
    private val includeQueryParam: IncludeQueryParam,
) : EndpointCore<EntityArrayEnvelope<PNMember>, PNMemberArrayResult>(pubnub), ManageChannelMembersInterface {
    override fun doWork(queryParams: HashMap<String, String>): Call<EntityArrayEnvelope<PNMember>> {
        val params =
            queryParams + collectionQueryParameters.createCollectionQueryParams() + includeQueryParam.createIncludeQueryParams()

        return retrofitManager.objectsService.patchChannelMembers(
            channel = channel,
            subKey = configuration.subscribeKey,
            options = params,
            body =
                ChangeMemberInput(
                    delete = uuidsToRemove.map { ServerMemberInput(UUIDId(id = it)) },
                    set =
                        uuidsToSet.map {
                            ServerMemberInput(
                                uuid = UUIDId(id = it.uuid),
                                custom = it.custom,
                                status = it.status,
                            )
                        },
                ),
        )
    }

    override fun createResponse(input: Response<EntityArrayEnvelope<PNMember>>): PNMemberArrayResult = input.toPNMemberArrayResult()

    override fun operationType(): PNOperationType = PNOperationType.ObjectsOperation()

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.APP_CONTEXT
}
