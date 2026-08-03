package com.pubnub.internal.endpoints.datasync.user

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.datasync.user.UpdateUser
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.user.PNUpdateUserResult
import com.pubnub.api.models.consumer.datasync.user.PNUser
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.datasync.UpdateEntityRequest
import com.pubnub.internal.models.server.datasync.UpdateEntityRequestData
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [com.pubnub.api.datasync.UserApi.update]
 */
class UpdateUserEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val userId: String,
    private val entityClassVersion: Int,
    private val status: String?,
    private val payload: Any?,
    private val ifMatch: String?,
) : EndpointCore<EntityEnvelope<PNUser>, PNUpdateUserResult>(pubnub), UpdateUser {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun validateParams() {
        super.validateParams()
        if (userId.isBlank()) {
            throw PubNubException(PubNubError.ENTITY_ID_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<PNUser>> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "userId" to userId,
                        "entityClassVersion" to entityClassVersion,
                        "status" to (status ?: ""),
                        "payload" to (payload ?: ""),
                        "ifMatch" to (ifMatch ?: "")
                    ),
                    operation = this::class.simpleName
                ),
                details = "UpdateUser API call",
            )
        )
        return retrofitManager.dataSyncService.updateUser(
            subKey = configuration.subscribeKey,
            userId = userId,
            body =
                UpdateEntityRequest(
                    data =
                        UpdateEntityRequestData(
                            entityClassVersion = entityClassVersion,
                            status = status,
                            payload = payload,
                        ),
                ),
            ifMatch = ifMatch,
            options = queryParams,
        )
    }

    override fun createResponse(input: Response<EntityEnvelope<PNUser>>): PNUpdateUserResult {
        return input.body()!!.let {
            PNUpdateUserResult(
                status = it.status,
                data = it.data,
            )
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNUpdateUserOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}
