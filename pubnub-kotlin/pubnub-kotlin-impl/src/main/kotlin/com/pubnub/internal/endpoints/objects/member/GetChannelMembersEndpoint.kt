package com.pubnub.internal.endpoints.objects.member

import com.pubnub.api.endpoints.objects.member.GetChannelMembers
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.objects.internal.CollectionQueryParameters
import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam
import com.pubnub.internal.extension.toPNMemberArrayResult
import com.pubnub.internal.models.server.objects_api.EntityArrayEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubImpl.getChannelMembers]
 */
class GetChannelMembersEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val channel: String,
    private val collectionQueryParameters: CollectionQueryParameters,
    private val includeQueryParam: IncludeQueryParam,
) : EndpointCore<EntityArrayEnvelope<PNMember>, PNMemberArrayResult>(pubnub), GetChannelMembers {
    override fun doWork(queryParams: HashMap<String, String>): Call<EntityArrayEnvelope<PNMember>> {
        val params =
            queryParams + collectionQueryParameters.createCollectionQueryParams() + includeQueryParam.createIncludeQueryParams()

        return retrofitManager.objectsService.getChannelMembers(
            channel = channel,
            subKey = configuration.subscribeKey,
            options = params,
        )
    }

    override fun createResponse(input: Response<EntityArrayEnvelope<PNMember>>): PNMemberArrayResult = input.toPNMemberArrayResult()

    override fun operationType(): PNOperationType = PNOperationType.ObjectsOperation()

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.APP_CONTEXT
}
