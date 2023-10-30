package com.pubnub.internal.endpoints.objects.membership

import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.BasePubNub.PubNubImpl
import com.pubnub.internal.endpoints.objects.internal.CollectionQueryParameters
import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam
import com.pubnub.internal.extension.toPNChannelMembershipArrayResult
import com.pubnub.internal.models.consumer.objects.membership.ChannelMembershipInput
import com.pubnub.internal.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.internal.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.internal.models.server.objects_api.ChangeMembershipInput
import com.pubnub.internal.models.server.objects_api.ChannelId
import com.pubnub.internal.models.server.objects_api.EntityArrayEnvelope
import com.pubnub.internal.models.server.objects_api.ServerMembershipInput
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubImpl.manageMemberships]
 */
class ManageMemberships internal constructor(
    pubnub: PubNubImpl,
    private val channelsToSet: Collection<ChannelMembershipInput>,
    private val channelsToRemove: Collection<String>,
    private val uuid: String,
    private val collectionQueryParameters: CollectionQueryParameters,
    private val includeQueryParam: IncludeQueryParam
) : com.pubnub.internal.Endpoint<EntityArrayEnvelope<PNChannelMembership>, PNChannelMembershipArrayResult>(pubnub),
    IManageMemberships {

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityArrayEnvelope<PNChannelMembership>> {
        val params = queryParams + collectionQueryParameters.createCollectionQueryParams() +
            includeQueryParam.createIncludeQueryParams()
        return pubnub.retrofitManager.objectsService.patchMemberships(
            uuid = uuid,
            subKey = pubnub.configuration.subscribeKey,
            options = params,
            body = ChangeMembershipInput(
                set = channelsToSet.map {
                    ServerMembershipInput(
                        channel = ChannelId(id = it.channel),
                        custom = it.custom,
                        status = it.status
                    )
                },
                delete = channelsToRemove.map { ServerMembershipInput(channel = ChannelId(id = it)) }
            )
        )
    }

    override fun createResponse(input: Response<EntityArrayEnvelope<PNChannelMembership>>): PNChannelMembershipArrayResult =
        input.toPNChannelMembershipArrayResult()

    override fun operationType(): PNOperationType = PNOperationType.ObjectsOperation()

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.APP_CONTEXT
}
