package com.pubnub.api.endpoints.objects.membership

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.objects.internal.ReturningChannelDetailsCustom
import com.pubnub.api.endpoints.objects.internal.ReturningCollection
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope
import retrofit2.Call
import retrofit2.Response
import java.util.HashMap

/**
 * @see [PubNub.getMemberships]
 */
class GetMemberships internal constructor(
    pubnub: PubNub,
    private val uuid: String,
    private val returningCollection: ReturningCollection,
    private val withChannelDetailsCustom: ReturningChannelDetailsCustom
) : Endpoint<EntityArrayEnvelope<PNChannelMembership>, PNChannelMembershipArrayResult>(pubnub) {

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityArrayEnvelope<PNChannelMembership>> {
        val params = queryParams + returningCollection.createCollectionQueryParams() +
                withChannelDetailsCustom.createIncludeQueryParams()

        return pubnub.retrofitManager.objectsService.getMemberships(
            uuid = uuid,
            subKey = pubnub.configuration.subscribeKey,
            options = params
        )
    }

    override fun createResponse(input: Response<EntityArrayEnvelope<PNChannelMembership>>): PNChannelMembershipArrayResult? {
        return input.body()?.let { arrayEnvelope ->
            PNChannelMembershipArrayResult(
                    status = arrayEnvelope.status,
                    data = arrayEnvelope.data,
                    totalCount = arrayEnvelope.totalCount,
                    next = arrayEnvelope.next?.let { PNPage.PNNext(it) },
                    prev = arrayEnvelope.prev?.let { PNPage.PNPrev(it) }
            )
        }
    }

    override fun operationType(): PNOperationType {
        return PNOperationType.PNGetMembershipsOperation
    }
}
