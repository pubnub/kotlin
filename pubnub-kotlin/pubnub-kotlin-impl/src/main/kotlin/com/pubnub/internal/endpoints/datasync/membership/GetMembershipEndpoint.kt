package com.pubnub.internal.endpoints.datasync.membership

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.datasync.membership.GetMembership
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.membership.PNDataSyncGetMembershipResult
import com.pubnub.api.models.consumer.datasync.membership.PNDataSyncMembership
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [com.pubnub.api.datasync.DataSync.getMembership]
 */
class GetMembershipEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val membershipId: String,
) : EndpointCore<EntityEnvelope<PNDataSyncMembership>, PNDataSyncGetMembershipResult>(pubnub), GetMembership {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun validateParams() {
        super.validateParams()
        if (membershipId.isBlank()) {
            throw PubNubException(PubNubError.ENTITY_ID_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<PNDataSyncMembership>> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "membershipId" to membershipId
                    ),
                    operation = this::class.simpleName
                ),
                details = "GetMembership API call",
            )
        )
        return retrofitManager.dataSyncService.getMembership(
            subKey = configuration.subscribeKey,
            membershipId = membershipId,
            options = queryParams,
        )
    }

    override fun createResponse(input: Response<EntityEnvelope<PNDataSyncMembership>>): PNDataSyncGetMembershipResult {
        return input.body()!!.let {
            PNDataSyncGetMembershipResult(
                status = it.status,
                data = it.data,
            )
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNGetMembershipOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}
