package com.pubnub.internal.endpoints.datasync.membership

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.datasync.membership.SetMembership
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.membership.PNDataSyncMembership
import com.pubnub.api.models.consumer.datasync.membership.PNDataSyncSetMembershipResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.datasync.SetMembershipRequest
import com.pubnub.internal.models.server.datasync.SetMembershipRequestData
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [com.pubnub.api.datasync.DataSync.setMembership]
 */
class SetMembershipEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val membershipId: String,
    private val classVersion: Int,
    private val status: String?,
    private val payload: Any?,
    private val ifMatch: String?,
) : EndpointCore<EntityEnvelope<PNDataSyncMembership>, PNDataSyncSetMembershipResult>(pubnub), SetMembership {
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
                        "membershipId" to membershipId,
                        "classVersion" to classVersion,
                        "status" to (status ?: ""),
                        "payload" to (payload ?: ""),
                        "ifMatch" to (ifMatch ?: "")
                    ),
                    operation = this::class.simpleName
                ),
                details = "SetMembership API call",
            )
        )
        return retrofitManager.dataSyncService.setMembership(
            subKey = configuration.subscribeKey,
            membershipId = membershipId,
            body =
                SetMembershipRequest(
                    data =
                        SetMembershipRequestData(
                            relationshipClassVersion = classVersion,
                            status = status,
                            payload = payload,
                        ),
                ),
            ifMatch = ifMatch,
            options = queryParams,
        )
    }

    override fun createResponse(
        input: Response<EntityEnvelope<PNDataSyncMembership>>,
    ): PNDataSyncSetMembershipResult {
        return input.body()!!.let {
            PNDataSyncSetMembershipResult(
                status = it.status,
                data = it.data,
            )
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNSetMembershipOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}
