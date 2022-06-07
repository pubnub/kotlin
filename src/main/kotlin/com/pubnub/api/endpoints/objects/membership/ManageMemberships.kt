package com.pubnub.api.endpoints.objects.membership

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.objects.internal.CollectionQueryParameters
import com.pubnub.api.endpoints.objects.internal.Include
import com.pubnub.api.endpoints.objects.internal.ReturningChannelDetailsCustom
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.api.models.consumer.objects.membership.PNChannelWithCustom
import com.pubnub.api.models.server.objects_api.ChangeMembershipInput
import com.pubnub.api.models.server.objects_api.ChannelId
import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope
import com.pubnub.api.models.server.objects_api.MembershipInput
import com.pubnub.extension.toPNChannelMembershipArrayResult
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNub.manageMemberships]
 */
class ManageMemberships internal constructor(
    pubnub: PubNub,
    private val channelsToSet: Collection<PNChannelWithCustom>,
    private val channelsToRemove: Collection<String>,
    private val uuid: String,
    private val collectionQueryParameters: CollectionQueryParameters,
    private val withIncludes: Include
) : Endpoint<EntityArrayEnvelope<PNChannelMembership>, PNChannelMembershipArrayResult>(pubnub) {

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityArrayEnvelope<PNChannelMembership>> {
        val params = queryParams + collectionQueryParameters.createCollectionQueryParams() +
            withIncludes.createIncludeQueryParams()
        return pubnub.retrofitManager.objectsService.patchMemberships(
            uuid = uuid,
            subKey = pubnub.configuration.subscribeKey,
            options = params,
            body = ChangeMembershipInput(
                set = channelsToSet.map { MembershipInput(channel = ChannelId(it.channel), custom = it.custom, type = null) },
                delete = channelsToRemove.map { MembershipInput(channel = ChannelId(id = it)) }
            )
        )
    }

    override fun createResponse(input: Response<EntityArrayEnvelope<PNChannelMembership>>): PNChannelMembershipArrayResult? =
        input.toPNChannelMembershipArrayResult()

    override fun operationType(): PNOperationType = PNOperationType.ObjectsOperation()
}
