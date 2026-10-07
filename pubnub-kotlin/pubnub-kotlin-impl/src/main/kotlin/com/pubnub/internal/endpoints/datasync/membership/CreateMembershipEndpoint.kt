package com.pubnub.internal.endpoints.datasync.membership

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.datasync.membership.CreateMembership
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.membership.PNDataSyncCreateMembershipResult
import com.pubnub.api.models.consumer.datasync.membership.PNDataSyncMembership
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.datasync.CreateMembershipRequest
import com.pubnub.internal.models.server.datasync.CreateMembershipRequestData
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [com.pubnub.api.datasync.DataSync.createMembership]
 */
class CreateMembershipEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val channelId: String,
    private val userId: String,
    private val classVersion: Int,
    private val membershipId: String?,
    private val status: String?,
    private val payload: Any?,
) : EndpointCore<EntityEnvelope<PNDataSyncMembership>, PNDataSyncCreateMembershipResult>(pubnub), CreateMembership {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun validateParams() {
        super.validateParams()
        // channelId / userId are required on create (unlike the optional membershipId, which the server
        // generates when omitted).
        if (channelId.isBlank() || userId.isBlank()) {
            throw PubNubException(PubNubError.ENTITY_ID_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<PNDataSyncMembership>> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "channelId" to channelId,
                        "userId" to userId,
                        "classVersion" to classVersion,
                        "membershipId" to (membershipId ?: ""),
                        "status" to (status ?: ""),
                        "payload" to (payload ?: "")
                    ),
                    operation = this::class.simpleName
                ),
                details = "CreateMembership API call",
            )
        )
        return retrofitManager.dataSyncService.createMembership(
            subKey = configuration.subscribeKey,
            body =
                CreateMembershipRequest(
                    data =
                        CreateMembershipRequestData(
                            id = membershipId,
                            channelId = channelId,
                            userId = userId,
                            relationshipClassVersion = classVersion,
                            status = status,
                            payload = payload,
                        ),
                ),
            options = queryParams,
        )
    }

    override fun createResponse(
        input: Response<EntityEnvelope<PNDataSyncMembership>>,
    ): PNDataSyncCreateMembershipResult {
        return input.body()!!.let {
            PNDataSyncCreateMembershipResult(
                status = it.status,
                data = it.data,
            )
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNCreateMembershipOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}
