package com.pubnub.internal.endpoints.objects.member

import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.CoreEndpoint
import com.pubnub.internal.CorePubNubClient
import com.pubnub.internal.endpoints.objects.internal.CollectionQueryParameters
import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam
import com.pubnub.internal.extension.toPNMemberArrayResult
import com.pubnub.internal.models.consumer.objects.member.PNMember
import com.pubnub.internal.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.internal.models.server.objects_api.EntityArrayEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [CorePubNubClient.getChannelMembers]
 */
class GetChannelMembersEndpoint internal constructor(
    pubnub: CorePubNubClient,
    private val channel: String,
    private val collectionQueryParameters: CollectionQueryParameters,
    private val includeQueryParam: IncludeQueryParam,
) : CoreEndpoint<EntityArrayEnvelope<PNMember>, PNMemberArrayResult>(pubnub), GetChannelMembersInterface {
    override fun doWork(queryParams: HashMap<String, String>): Call<EntityArrayEnvelope<PNMember>> {
        val params =
            queryParams + collectionQueryParameters.createCollectionQueryParams() + includeQueryParam.createIncludeQueryParams()

        return pubnub.retrofitManager.objectsService.getChannelMembers(
            channel = channel,
            subKey = pubnub.configuration.subscribeKey,
            options = params,
        )
    }

    override fun createResponse(input: Response<EntityArrayEnvelope<PNMember>>): PNMemberArrayResult = input.toPNMemberArrayResult()

    override fun operationType(): PNOperationType = PNOperationType.ObjectsOperation()

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.APP_CONTEXT
}
