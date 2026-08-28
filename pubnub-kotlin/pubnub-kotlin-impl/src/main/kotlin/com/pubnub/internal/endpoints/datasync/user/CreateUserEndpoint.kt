package com.pubnub.internal.endpoints.datasync.user

import com.pubnub.api.endpoints.datasync.user.CreateUser
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.PNDataSyncClassLevel
import com.pubnub.api.models.consumer.datasync.user.DataSyncCreateUserResult
import com.pubnub.api.models.consumer.datasync.user.DataSyncUser
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.datasync.CreateUserRequest
import com.pubnub.internal.models.server.datasync.CreateUserRequestData
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [com.pubnub.api.datasync.DataSync.createUser]
 */
class CreateUserEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val className: String?,
    private val classVersion: Int,
    private val classLevel: PNDataSyncClassLevel?,
    private val userId: String?,
    private val status: String?,
    private val payload: Any?,
) : EndpointCore<EntityEnvelope<DataSyncUser>, DataSyncCreateUserResult>(pubnub), CreateUser {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<DataSyncUser>> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "className" to (className ?: ""),
                        "classVersion" to classVersion,
                        "classLevel" to (classLevel?.value ?: ""),
                        "userId" to (userId ?: ""),
                        "status" to (status ?: ""),
                        "payload" to (payload ?: "")
                    ),
                    operation = this::class.simpleName
                ),
                details = "CreateUser API call",
            )
        )
        return retrofitManager.dataSyncService.createUser(
            subKey = configuration.subscribeKey,
            body =
                CreateUserRequest(
                    data =
                        CreateUserRequestData(
                            id = userId,
                            entityClass = className,
                            entityClassVersion = classVersion,
                            entityClassLevel = classLevel?.value,
                            status = status,
                            payload = payload,
                        ),
                ),
            options = queryParams,
        )
    }

    override fun createResponse(input: Response<EntityEnvelope<DataSyncUser>>): DataSyncCreateUserResult {
        return input.body()!!.let {
            DataSyncCreateUserResult(
                status = it.status,
                data = it.data,
            )
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNCreateUserOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}
