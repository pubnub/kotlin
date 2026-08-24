package com.pubnub.internal.endpoints.datasync.entity

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.datasync.entity.GetEntities
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.entity.PNEntity
import com.pubnub.api.models.consumer.datasync.entity.PNGetEntitiesResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.datasync.EntitiesEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [com.pubnub.api.datasync.DataSync.getEntities]
 */
class GetEntitiesEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val entityClass: String,
    private val entityClassVersion: Int?,
    private val entityClassLevel: String?,
    private val filter: String?,
    private val filterAdvanced: String?,
    private val sort: String?,
    private val limit: Int?,
    private val cursor: String?,
) : EndpointCore<EntitiesEnvelope<PNEntity>, PNGetEntitiesResult>(pubnub), GetEntities {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun validateParams() {
        super.validateParams()
        if (entityClass.isBlank()) {
            throw PubNubException(PubNubError.ENTITY_CLASS_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<EntitiesEnvelope<PNEntity>> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "entityClass" to entityClass,
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
                details = "GetEntities API call",
            )
        )
        queryParams["entity_class"] = entityClass
        entityClassVersion?.let { queryParams["entity_class_version"] = it.toString() }
        entityClassLevel?.let { queryParams["entity_class_level"] = it }
        filter?.let { queryParams["filter"] = it }
        filterAdvanced?.let { queryParams["filter_advanced"] = it }
        sort?.let { queryParams["sort"] = it }
        limit?.let { queryParams["limit"] = it.toString() }
        cursor?.let { queryParams["cursor"] = it }
        return retrofitManager.dataSyncService.getEntities(
            subKey = configuration.subscribeKey,
            options = queryParams,
        )
    }

    override fun createResponse(input: Response<EntitiesEnvelope<PNEntity>>): PNGetEntitiesResult {
        return input.body()!!.let {
            PNGetEntitiesResult(
                status = it.status,
                data = it.data,
                next = it.meta?.nextCursor,
                hasNext = it.meta?.hasNext ?: false,
                limit = it.meta?.limit,
            )
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNGetEntitiesOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}
