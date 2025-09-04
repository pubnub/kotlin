package com.pubnub.internal.endpoints.objects.uuid

import com.pubnub.api.endpoints.objects.uuid.SetUUIDMetadata
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import com.pubnub.internal.models.server.objects_api.UUIDMetadataInput
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubImpl.setUUIDMetadata]
 */
class SetUUIDMetadataEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val uuid: String?,
    private val name: String?,
    private val externalId: String?,
    private val profileUrl: String?,
    private val email: String?,
    private val custom: Any?,
    private val withInclude: IncludeQueryParam,
    private val type: String?,
    private val status: String?,
    private val ifMatchesEtag: String?,
) : EndpointCore<EntityEnvelope<PNUUIDMetadata>, PNUUIDMetadataResult>(pubnub), SetUUIDMetadata {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<PNUUIDMetadata>> {
        log.trace(
            LogMessage(
                message = LogMessageContent.Object(
                    message = mapOf(
                        "uuid" to (uuid ?: configuration.userId.value),
                        "name" to (name ?: ""),
                        "externalId" to (externalId ?: ""),
                        "profileUrl" to (profileUrl ?: ""),
                        "email" to (email ?: ""),
                        "custom" to (custom ?: ""),
                        "type" to (type ?: ""),
                        "status" to (status ?: ""),
                        "ifMatchesEtag" to (ifMatchesEtag ?: ""),
                        "queryParams" to queryParams
                    )
                ),
                details = "SetUUIDMetadata API call",
            )
        )
        val params = queryParams + withInclude.createIncludeQueryParams()
        return retrofitManager.objectsService.setUUIDMetadata(
            subKey = configuration.subscribeKey,
            body =
                UUIDMetadataInput(
                    name = name,
                    custom = custom,
                    email = email,
                    externalId = externalId,
                    profileUrl = profileUrl,
                    type = type,
                    status = status,
                ),
            uuid = uuid ?: configuration.userId.value,
            options = params,
            ifMatchesEtag = ifMatchesEtag
        )
    }

    override fun createResponse(input: Response<EntityEnvelope<PNUUIDMetadata>>): PNUUIDMetadataResult {
        return input.body()!!.let {
            PNUUIDMetadataResult(
                status = it.status,
                data = it.data,
            )
        }
    }

    override fun operationType(): PNOperationType {
        return PNOperationType.PNSetUUIDMetadataOperation
    }

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.APP_CONTEXT
}
