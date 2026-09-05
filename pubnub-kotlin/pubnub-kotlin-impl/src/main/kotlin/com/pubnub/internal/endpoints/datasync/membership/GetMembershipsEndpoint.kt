package com.pubnub.internal.endpoints.datasync.membership

import com.pubnub.api.endpoints.datasync.membership.GetMemberships
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.PNDataSyncPage
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField
import com.pubnub.api.models.consumer.datasync.membership.PNDataSyncGetMembershipsResult
import com.pubnub.api.models.consumer.datasync.membership.PNDataSyncMembership
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.datasync.EntitiesEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [com.pubnub.api.datasync.DataSync.getMemberships]
 */
class GetMembershipsEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val channelId: String?,
    private val userId: String?,
    private val classVersion: Int?,
    private val filterFast: String?,
    private val filter: String?,
    private val sort: List<PNDataSyncSortField>,
    private val limit: Int?,
    private val cursor: String?,
) : EndpointCore<EntitiesEnvelope<PNDataSyncMembership>, PNDataSyncGetMembershipsResult>(pubnub), GetMemberships {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun doWork(queryParams: HashMap<String, String>): Call<EntitiesEnvelope<PNDataSyncMembership>> {
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
                        "channelId" to (channelId ?: ""),
                        "userId" to (userId ?: ""),
                        "classVersion" to (classVersion ?: ""),
                        "filterFast" to (filterFast ?: ""),
                        "filter" to (filter ?: ""),
                        "sort" to sortParam,
                        "limit" to (limit ?: ""),
                        "cursor" to (cursor ?: "")
                    ),
                    operation = this::class.simpleName
                ),
                details = "GetMemberships API call",
            )
        )
        channelId?.let { queryParams["channel_id"] = it }
        userId?.let { queryParams["user_id"] = it }
        classVersion?.let { queryParams["relationship_class_version"] = it.toString() }
        filterFast?.let { queryParams["filter_fast"] = it }
        filter?.let { queryParams["filter"] = it }
        if (sortParam.isNotEmpty()) {
            queryParams["sort"] = sortParam
        }
        limit?.let { queryParams["limit"] = it.toString() }
        cursor?.let { queryParams["cursor"] = it }
        return retrofitManager.dataSyncService.getMemberships(
            subKey = configuration.subscribeKey,
            options = queryParams,
        )
    }

    override fun createResponse(
        input: Response<EntitiesEnvelope<PNDataSyncMembership>>,
    ): PNDataSyncGetMembershipsResult {
        return input.body()!!.let {
            PNDataSyncGetMembershipsResult(
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

    override fun operationType(): PNOperationType = PNOperationType.PNGetDataSyncMembershipsOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}
