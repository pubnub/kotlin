package com.pubnub.internal.endpoints.datasync.user

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.datasync.user.RemoveUser
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.user.PNRemoveUserResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import retrofit2.Call
import retrofit2.Response

/**
 * @see [com.pubnub.api.datasync.UserApi.delete]
 */
class RemoveUserEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val userId: String,
    private val ifMatch: String?,
) : EndpointCore<Void, PNRemoveUserResult>(pubnub), RemoveUser {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun validateParams() {
        super.validateParams()
        if (userId.isBlank()) {
            throw PubNubException(PubNubError.ENTITY_ID_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Void> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "userId" to userId,
                        "ifMatch" to (ifMatch ?: "")
                    ),
                    operation = this::class.simpleName
                ),
                details = "RemoveUser API call",
            )
        )
        return retrofitManager.dataSyncService.deleteUser(
            subKey = configuration.subscribeKey,
            userId = userId,
            ifMatch = ifMatch,
            options = queryParams,
        )
    }

    override fun createResponse(input: Response<Void>): PNRemoveUserResult {
        // DeleteSuccessResponse has an empty body, so surface the HTTP status code.
        return PNRemoveUserResult(input.code())
    }

    override fun operationType(): PNOperationType = PNOperationType.PNRemoveUserOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}
