package com.pubnub.internal.endpoints.datasync.user

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.datasync.user.GetUser
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.user.DataSyncGetUserResult
import com.pubnub.api.models.consumer.datasync.user.DataSyncUser
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [com.pubnub.api.datasync.DataSync.getUser]
 */
class GetUserEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val userId: String,
) : EndpointCore<EntityEnvelope<DataSyncUser>, DataSyncGetUserResult>(pubnub), GetUser {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun validateParams() {
        super.validateParams()
        if (userId.isBlank()) {
            throw PubNubException(PubNubError.ENTITY_ID_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<DataSyncUser>> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "userId" to userId
                    ),
                    operation = this::class.simpleName
                ),
                details = "GetUser API call",
            )
        )
        return retrofitManager.dataSyncService.getUser(
            subKey = configuration.subscribeKey,
            userId = userId,
            options = queryParams,
        )
    }

    override fun createResponse(input: Response<EntityEnvelope<DataSyncUser>>): DataSyncGetUserResult {
        return input.body()!!.let {
            DataSyncGetUserResult(
                status = it.status,
                data = it.data,
            )
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNGetUserOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}
