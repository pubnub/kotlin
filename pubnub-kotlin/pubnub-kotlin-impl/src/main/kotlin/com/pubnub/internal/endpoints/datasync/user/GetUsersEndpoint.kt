package com.pubnub.internal.endpoints.datasync.user

import com.pubnub.api.endpoints.datasync.user.GetUsers
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.user.PNGetUsersResult
import com.pubnub.api.models.consumer.datasync.user.PNUser
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.datasync.EntitiesEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [com.pubnub.api.datasync.DataSync.getUsers]
 */
class GetUsersEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val entityClass: String?,
    private val entityClassVersion: Int?,
    private val entityClassLevel: String?,
    private val filter: String?,
    private val filterAdvanced: String?,
    private val sort: String?,
    private val limit: Int?,
    private val cursor: String?,
) : EndpointCore<EntitiesEnvelope<PNUser>, PNGetUsersResult>(pubnub), GetUsers {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun doWork(queryParams: HashMap<String, String>): Call<EntitiesEnvelope<PNUser>> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "entityClass" to (entityClass ?: ""),
                        "entityClassVersion" to (entityClassVersion ?: ""),
                        "entityClassLevel" to (entityClassLevel ?: ""),
                        "filter" to (filter ?: ""),
                        "filterAdvanced" to (filterAdvanced ?: ""),
                        "sort" to (sort ?: ""),
                        "limit" to (limit ?: ""),
                        "cursor" to (cursor ?: "")
                    ),
                    operation = this::class.simpleName
                ),
                details = "GetUsers API call",
            )
        )
        entityClass?.let { queryParams["entity_class"] = it }
        entityClassVersion?.let { queryParams["entity_class_version"] = it.toString() }
        entityClassLevel?.let { queryParams["entity_class_level"] = it }
        filter?.let { queryParams["filter"] = it }
        filterAdvanced?.let { queryParams["filter_advanced"] = it }
        sort?.let { queryParams["sort"] = it }
        limit?.let { queryParams["limit"] = it.toString() }
        cursor?.let { queryParams["cursor"] = it }
        return retrofitManager.dataSyncService.getUsers(
            subKey = configuration.subscribeKey,
            options = queryParams,
        )
    }

    override fun createResponse(input: Response<EntitiesEnvelope<PNUser>>): PNGetUsersResult {
        return input.body()!!.let {
            PNGetUsersResult(
                status = it.status,
                data = it.data,
                next = it.meta?.nextCursor,
                hasNext = it.meta?.hasNext ?: false,
                limit = it.meta?.limit,
            )
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNGetUsersOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}
