package com.pubnub.api.endpoints.objects.membership

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.objects.internal.Include
import com.pubnub.api.endpoints.objects.internal.ReturningCollection
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
import java.util.HashMap

/**
 * @see [PubNub.manageMemberships]
 */
class ManageMemberships internal constructor(
    pubnub: PubNub,
    private val channelsToSet: Collection<PNChannelWithCustom>,
    private val channelsToRemove: Collection<String>,
    private val uuid: String,
    private val returningCollection: ReturningCollection,
    private val withIncludes: Include,
    private val status: String?
) : Endpoint<EntityArrayEnvelope<PNChannelMembership>, PNChannelMembershipArrayResult>(pubnub) {

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityArrayEnvelope<PNChannelMembership>> {
        val params = queryParams + returningCollection.createCollectionQueryParams() +
            withIncludes.createIncludeQueryParams()
        return pubnub.retrofitManager.objectsService.patchMemberships(
            uuid = uuid,
            subKey = pubnub.configuration.subscribeKey,
            options = params,
            body = ChangeMembershipInput(
                set = channelsToSet.map { MembershipInput(channel = ChannelId(it.channel), custom = it.custom, status = status) },
                delete = channelsToRemove.map { MembershipInput(channel = ChannelId(id = it)) }
            )
        )
    }

    override fun createResponse(input: Response<EntityArrayEnvelope<PNChannelMembership>>): PNChannelMembershipArrayResult? =
        input.toPNChannelMembershipArrayResult()

    override fun operationType(): PNOperationType = PNOperationType.ObjectsOperation()
}
