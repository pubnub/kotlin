package com.pubnub.internal.endpoints.datasync.membership

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.datasync.membership.RemoveMembership
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.membership.PNDataSyncRemoveMembershipResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import retrofit2.Call
import retrofit2.Response

/**
 * @see [com.pubnub.api.datasync.DataSync.removeMembership]
 */
class RemoveMembershipEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val membershipId: String,
    private val ifMatch: String?,
) : EndpointCore<Void, PNDataSyncRemoveMembershipResult>(pubnub), RemoveMembership {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun validateParams() {
        super.validateParams()
        if (membershipId.isBlank()) {
            throw PubNubException(PubNubError.ENTITY_ID_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Void> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "membershipId" to membershipId,
                        "ifMatch" to (ifMatch ?: "")
                    ),
                    operation = this::class.simpleName
                ),
                details = "RemoveMembership API call",
            )
        )
        return retrofitManager.dataSyncService.deleteMembership(
            subKey = configuration.subscribeKey,
            membershipId = membershipId,
            ifMatch = ifMatch,
            options = queryParams,
        )
    }

    override fun createResponse(input: Response<Void>): PNDataSyncRemoveMembershipResult {
        // DeleteSuccessResponse has an empty body, so surface the HTTP status code.
        return PNDataSyncRemoveMembershipResult(input.code())
    }

    override fun operationType(): PNOperationType = PNOperationType.PNRemoveMembershipOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}
