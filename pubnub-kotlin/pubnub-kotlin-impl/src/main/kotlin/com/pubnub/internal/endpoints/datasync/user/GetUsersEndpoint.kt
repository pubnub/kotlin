package com.pubnub.internal.endpoints.datasync.user

import com.pubnub.api.endpoints.datasync.user.GetUsers
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.PNDataSyncClassLevel
import com.pubnub.api.models.consumer.datasync.PNDataSyncPage
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField
import com.pubnub.api.models.consumer.datasync.user.PNDataSyncGetUsersResult
import com.pubnub.api.models.consumer.datasync.user.PNDataSyncUser
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
    private val className: String?,
    private val classVersion: Int?,
    private val classLevel: PNDataSyncClassLevel?,
    private val filterFast: String?,
    private val filter: String?,
    private val sort: List<PNDataSyncSortField>,
    private val limit: Int?,
    private val cursor: String?,
) : EndpointCore<EntitiesEnvelope<PNDataSyncUser>, PNDataSyncGetUsersResult>(pubnub), GetUsers {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun doWork(queryParams: HashMap<String, String>): Call<EntitiesEnvelope<PNDataSyncUser>> {
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
                        "className" to (className ?: ""),
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
                details = "GetUsers API call",
            )
        )
        className?.let { queryParams["entity_class"] = it }
        classVersion?.let { queryParams["entity_class_version"] = it.toString() }
        classLevel?.let { queryParams["entity_class_level"] = it.value }
        filterFast?.let { queryParams["filter_fast"] = it }
        filter?.let { queryParams["filter"] = it }
        if (sortParam.isNotEmpty()) {
            queryParams["sort"] = sortParam
        }
        limit?.let { queryParams["limit"] = it.toString() }
        cursor?.let { queryParams["cursor"] = it }
        return retrofitManager.dataSyncService.getUsers(
            subKey = configuration.subscribeKey,
            options = queryParams,
        )
    }

    override fun createResponse(input: Response<EntitiesEnvelope<PNDataSyncUser>>): PNDataSyncGetUsersResult {
        return input.body()!!.let {
            PNDataSyncGetUsersResult(
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

    override fun operationType(): PNOperationType = PNOperationType.PNGetUsersOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}
