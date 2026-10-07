package com.pubnub.internal.endpoints.datasync.user

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.datasync.user.SetUser
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.user.PNDataSyncSetUserResult
import com.pubnub.api.models.consumer.datasync.user.PNDataSyncUser
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.datasync.SetEntityRequest
import com.pubnub.internal.models.server.datasync.SetEntityRequestData
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [com.pubnub.api.datasync.DataSync.setUser]
 */
class SetUserEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val userId: String,
    private val classVersion: Int,
    private val status: String?,
    private val payload: Any?,
    private val ifMatch: String?,
) : EndpointCore<EntityEnvelope<PNDataSyncUser>, PNDataSyncSetUserResult>(pubnub), SetUser {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun validateParams() {
        super.validateParams()
        if (userId.isBlank()) {
            throw PubNubException(PubNubError.ENTITY_ID_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<PNDataSyncUser>> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "userId" to userId,
                        "classVersion" to classVersion,
                        "status" to (status ?: ""),
                        "payload" to (payload ?: ""),
                        "ifMatch" to (ifMatch ?: "")
                    ),
                    operation = this::class.simpleName
                ),
                details = "SetUser API call",
            )
        )
        return retrofitManager.dataSyncService.setUser(
            subKey = configuration.subscribeKey,
            userId = userId,
            body =
                SetEntityRequest(
                    data =
                        SetEntityRequestData(
                            entityClassVersion = classVersion,
                            status = status,
                            payload = payload,
                        ),
                ),
            ifMatch = ifMatch,
            options = queryParams,
        )
    }

    override fun createResponse(input: Response<EntityEnvelope<PNDataSyncUser>>): PNDataSyncSetUserResult {
        return input.body()!!.let {
            PNDataSyncSetUserResult(
                status = it.status,
                data = it.data,
            )
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNSetUserOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}
