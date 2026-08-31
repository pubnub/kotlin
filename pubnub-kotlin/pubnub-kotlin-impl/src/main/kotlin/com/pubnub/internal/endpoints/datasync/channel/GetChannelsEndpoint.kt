package com.pubnub.internal.endpoints.datasync.channel

import com.pubnub.api.endpoints.datasync.channel.GetChannels
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.PNDataSyncClassLevel
import com.pubnub.api.models.consumer.datasync.PNDataSyncPage
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField
import com.pubnub.api.models.consumer.datasync.channel.PNDataSyncChannel
import com.pubnub.api.models.consumer.datasync.channel.PNDataSyncGetChannelsResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.datasync.EntitiesEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [com.pubnub.api.datasync.DataSync.getChannels]
 */
class GetChannelsEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val className: String?,
    private val classVersion: Int?,
    private val classLevel: PNDataSyncClassLevel?,
    private val filter: String?,
    private val filterAdvanced: String?,
    private val sort: List<PNDataSyncSortField>,
    private val limit: Int?,
    private val cursor: String?,
) : EndpointCore<EntitiesEnvelope<PNDataSyncChannel>, PNDataSyncGetChannelsResult>(pubnub), GetChannels {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun doWork(queryParams: HashMap<String, String>): Call<EntitiesEnvelope<PNDataSyncChannel>> {
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
                details = "GetChannels API call",
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
        return retrofitManager.dataSyncService.getChannels(
            subKey = configuration.subscribeKey,
            options = queryParams,
        )
    }

    override fun createResponse(input: Response<EntitiesEnvelope<PNDataSyncChannel>>): PNDataSyncGetChannelsResult {
        return input.body()!!.let {
            PNDataSyncGetChannelsResult(
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

    override fun operationType(): PNOperationType = PNOperationType.PNGetChannelsOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}
