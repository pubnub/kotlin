package com.pubnub.internal.endpoints.datasync.entity

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.datasync.entity.GetEntities
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.PNDataSyncClassLevel
import com.pubnub.api.models.consumer.datasync.PNDataSyncPage
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField
import com.pubnub.api.models.consumer.datasync.entity.PNDataSyncEntity
import com.pubnub.api.models.consumer.datasync.entity.PNDataSyncGetEntitiesResult
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
    private val className: String,
    private val classVersion: Int?,
    private val classLevel: PNDataSyncClassLevel?,
    private val filterFast: String?,
    private val filter: String?,
    private val sort: List<PNDataSyncSortField>,
    private val limit: Int?,
    private val cursor: String?,
) : EndpointCore<EntitiesEnvelope<PNDataSyncEntity>, PNDataSyncGetEntitiesResult>(pubnub), GetEntities {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun validateParams() {
        super.validateParams()
        if (className.isBlank()) {
            throw PubNubException(PubNubError.ENTITY_CLASS_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<EntitiesEnvelope<PNDataSyncEntity>> {
        val sortParam = sort.joinToString(",") {
            if (it.ascending) {
                it.property
            } else {
                "${it.property}:desc"
            }
        }
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "className" to className,
                        "classVersion" to (classVersion ?: ""),
                        "classLevel" to (classLevel?.value ?: ""),
                        "filterFast" to (filterFast ?: ""),
                        "filter" to (filter ?: ""),
                        "sort" to sortParam,
                        "limit" to (limit ?: ""),
                        "cursor" to (cursor ?: "")
                    ),
                    operation = this::class.simpleName
                ),
                details = "GetEntities API call",
            )
        )
        queryParams["entity_class"] = className
        classVersion?.let { queryParams["entity_class_version"] = it.toString() }
        classLevel?.let { queryParams["entity_class_level"] = it.value }
        filterFast?.let { queryParams["filter_fast"] = it }
        filter?.let { queryParams["filter"] = it }
        if (sortParam.isNotEmpty()) {
            queryParams["sort"] = sortParam
        }
        limit?.let { queryParams["limit"] = it.toString() }
        cursor?.let { queryParams["cursor"] = it }
        return retrofitManager.dataSyncService.getEntities(
            subKey = configuration.subscribeKey,
            options = queryParams,
        )
    }

    override fun createResponse(input: Response<EntitiesEnvelope<PNDataSyncEntity>>): PNDataSyncGetEntitiesResult {
        return input.body()!!.let {
            PNDataSyncGetEntitiesResult(
                status = it.status,
                data = it.data,
                next = PNDataSyncPage(
                    cursor = it.meta?.nextCursor,
                    hasNext = it.meta?.hasNext ?: false,
                    limit = it.meta?.limit,
                ),
            )
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNGetEntitiesOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}
