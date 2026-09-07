package com.pubnub.internal.endpoints.datasync.relationship

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.datasync.relationship.GetRelationships
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.PNDataSyncPage
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField
import com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncGetRelationshipsResult
import com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncRelationship
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.datasync.EntitiesEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [com.pubnub.api.datasync.DataSync.getRelationships]
 */
class GetRelationshipsEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val className: String,
    private val entityAId: String?,
    private val entityBId: String?,
    private val classVersion: Int?,
    private val filterFast: String?,
    private val filter: String?,
    private val sort: List<PNDataSyncSortField>,
    private val limit: Int?,
    private val cursor: String?,
) : EndpointCore<EntitiesEnvelope<PNDataSyncRelationship>, PNDataSyncGetRelationshipsResult>(pubnub), GetRelationships {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun validateParams() {
        super.validateParams()
        if (className.isBlank()) {
            throw PubNubException(PubNubError.ENTITY_CLASS_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<EntitiesEnvelope<PNDataSyncRelationship>> {
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
                        "entityAId" to (entityAId ?: ""),
                        "entityBId" to (entityBId ?: ""),
                        "classVersion" to (classVersion ?: ""),
                        "filterFast" to (filterFast ?: ""),
                        "filter" to (filter ?: ""),
                        "sort" to sortParam,
                        "limit" to (limit ?: ""),
                        "cursor" to (cursor ?: "")
                    ),
                    operation = this::class.simpleName
                ),
                details = "GetRelationships API call",
            )
        )
        // relationship_class is required — emit unconditionally (like GetEntitiesEndpoint's entity_class).
        queryParams["relationship_class"] = className
        entityAId?.let { queryParams["entity_a_id"] = it }
        entityBId?.let { queryParams["entity_b_id"] = it }
        classVersion?.let { queryParams["relationship_class_version"] = it.toString() }
        filterFast?.let { queryParams["filter_fast"] = it }
        filter?.let { queryParams["filter"] = it }
        if (sortParam.isNotEmpty()) {
            queryParams["sort"] = sortParam
        }
        limit?.let { queryParams["limit"] = it.toString() }
        cursor?.let { queryParams["cursor"] = it }
        return retrofitManager.dataSyncService.getRelationships(
            subKey = configuration.subscribeKey,
            options = queryParams,
        )
    }

    override fun createResponse(
        input: Response<EntitiesEnvelope<PNDataSyncRelationship>>,
    ): PNDataSyncGetRelationshipsResult {
        return input.body()!!.let {
            PNDataSyncGetRelationshipsResult(
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

    override fun operationType(): PNOperationType = PNOperationType.PNGetRelationshipsOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}
