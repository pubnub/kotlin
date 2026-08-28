package com.pubnub.internal.endpoints.datasync.user

import com.pubnub.api.endpoints.datasync.user.GetUsers
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.PNDataSyncClassLevel
import com.pubnub.api.models.consumer.datasync.PNDataSyncPage
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField
import com.pubnub.api.models.consumer.datasync.user.DataSyncGetUsersResult
import com.pubnub.api.models.consumer.datasync.user.DataSyncUser
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
    private val filter: String?,
    private val filterAdvanced: String?,
    private val sort: List<PNDataSyncSortField>,
    private val limit: Int?,
    private val cursor: String?,
) : EndpointCore<EntitiesEnvelope<DataSyncUser>, DataSyncGetUsersResult>(pubnub), GetUsers {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun doWork(queryParams: HashMap<String, String>): Call<EntitiesEnvelope<DataSyncUser>> {
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
                        "filter" to (filter ?: ""),
                        "filterAdvanced" to (filterAdvanced ?: ""),
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
        filter?.let { queryParams["filter"] = it }
        filterAdvanced?.let { queryParams["filter_advanced"] = it }
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

    override fun createResponse(input: Response<EntitiesEnvelope<DataSyncUser>>): DataSyncGetUsersResult {
        return input.body()!!.let {
            DataSyncGetUsersResult(
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
