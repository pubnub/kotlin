package com.pubnub.internal.endpoints.objects.membership

import com.pubnub.api.endpoints.objects.membership.GetMemberships
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.objects.internal.CollectionQueryParameters
import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam
import com.pubnub.internal.extension.toPNChannelMembershipArrayResult
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.objects_api.EntityArrayEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubImpl.getMemberships]
 */
class GetMembershipsEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val uuid: String,
    private val collectionQueryParameters: CollectionQueryParameters,
    private val includeQueryParam: IncludeQueryParam,
) : EndpointCore<EntityArrayEnvelope<PNChannelMembership>, PNChannelMembershipArrayResult>(pubnub),
    GetMemberships {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityArrayEnvelope<PNChannelMembership>> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "uuid" to uuid
                    ),
                    operation = this::class.simpleName
                ),
                details = "GetMemberships API call",
            )
        )
        val params =
            queryParams + collectionQueryParameters.createCollectionQueryParams() + includeQueryParam.createIncludeQueryParams()

        return retrofitManager.objectsService.getMemberships(
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
