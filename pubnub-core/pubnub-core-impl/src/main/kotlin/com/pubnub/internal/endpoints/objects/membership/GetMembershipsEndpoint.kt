package com.pubnub.internal.endpoints.objects.membership

import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubCore
import com.pubnub.internal.endpoints.objects.internal.CollectionQueryParameters
import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam
import com.pubnub.internal.extension.toPNChannelMembershipArrayResult
import com.pubnub.internal.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.internal.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.internal.models.server.objects_api.EntityArrayEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubCore.getMemberships]
 */
class GetMembershipsEndpoint internal constructor(
    pubnub: PubNubCore,
    private val uuid: String,
    private val collectionQueryParameters: CollectionQueryParameters,
    private val includeQueryParam: IncludeQueryParam,
) : EndpointCore<EntityArrayEnvelope<PNChannelMembership>, PNChannelMembershipArrayResult>(pubnub),
    GetMembershipsInterface {
    override fun doWork(queryParams: HashMap<String, String>): Call<EntityArrayEnvelope<PNChannelMembership>> {
        val params =
            queryParams + collectionQueryParameters.createCollectionQueryParams() + includeQueryParam.createIncludeQueryParams()

        return pubnub.retrofitManager.objectsService.getMemberships(
            uuid = uuid,
            subKey = configuration.subscribeKey,
            options = params,
        )
    }

    override fun createResponse(input: Response<EntityArrayEnvelope<PNChannelMembership>>): PNChannelMembershipArrayResult =
        input.toPNChannelMembershipArrayResult()

    override fun operationType(): PNOperationType = PNOperationType.PNGetMembershipsOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.APP_CONTEXT
}
