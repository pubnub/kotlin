package com.pubnub.internal.endpoints.objects.member

import com.pubnub.api.endpoints.objects.member.ManageChannelMembers
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.logging.LogMessageType
import com.pubnub.api.models.consumer.objects.member.MemberInput
import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.objects.internal.CollectionQueryParameters
import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam
import com.pubnub.internal.extension.toPNMemberArrayResult
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.models.server.objects_api.ChangeMemberInput
import com.pubnub.internal.models.server.objects_api.EntityArrayEnvelope
import com.pubnub.internal.models.server.objects_api.ServerMemberInput
import com.pubnub.internal.models.server.objects_api.UUIDId
import org.slf4j.event.Level
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubImpl.manageChannelMembers]
 */
class ManageChannelMembersEndpoint(
    pubnub: PubNubImpl,
    private val userToSet: Collection<MemberInput>,
    private val userIdsRemove: Collection<String>,
    private val channel: String,
    private val collectionQueryParameters: CollectionQueryParameters,
    private val includeQueryParam: IncludeQueryParam,
) : EndpointCore<EntityArrayEnvelope<PNMember>, PNMemberArrayResult>(pubnub), ManageChannelMembers {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityArrayEnvelope<PNMember>> {
        log.trace(
            LogMessage(
                pubNubId = pubnub.instanceId,
                logLevel = Level.TRACE,
                location = this::class.java.toString(),
                type = LogMessageType.OBJECT,
                message = LogMessageContent.Object(
                    message = mapOf(
                        "channel" to channel,
                        "userToSet" to userToSet,
                        "userIdsRemove" to userIdsRemove,
                        "queryParams" to queryParams
                    )
                ),
                details = "ManageChannelMembers API call"
            )
        )
        val params =
            queryParams + collectionQueryParameters.createCollectionQueryParams() + includeQueryParam.createIncludeQueryParams()

        return retrofitManager.objectsService.patchChannelMembers(
            channel = channel,
            subKey = configuration.subscribeKey,
            options = params,
            body =
                ChangeMemberInput(
                    delete = userIdsRemove.map { ServerMemberInput(UUIDId(id = it)) },
                    set = userToSet.map {
                        ServerMemberInput(
                            uuid = UUIDId(id = it.uuid),
                            custom = it.custom,
                            status = it.status,
                            type = it.type,
                        )
                    },
                ),
        )
    }

    override fun createResponse(input: Response<EntityArrayEnvelope<PNMember>>): PNMemberArrayResult = input.toPNMemberArrayResult()

    override fun operationType(): PNOperationType = PNOperationType.ObjectsOperation()

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.APP_CONTEXT
}
