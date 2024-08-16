package com.pubnub.internal.endpoints.access

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.access.GrantToken
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.api.v2.PNConfiguration.Companion.isValid
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.models.server.access_manager.v3.GrantTokenRequestBody
import com.pubnub.internal.models.server.access_manager.v3.GrantTokenResponse
import retrofit2.Call
import retrofit2.Response

class GrantTokenEndpoint(
    pubnub: PubNubImpl,
    override val ttl: Int,
    private val meta: Any?,
    private val authorizedUUID: String?,
    private val channels: List<ChannelGrant>,
    private val channelGroups: List<ChannelGroupGrant>,
    private val uuids: List<UUIDGrant>,
) : EndpointCore<GrantTokenResponse, PNGrantTokenResult>(pubnub), GrantToken {
    override fun getAffectedChannels(): List<String> = channels.map { it.id }

    override fun getAffectedChannelGroups(): List<String> = channelGroups.map { it.id }

    override fun validateParams() {
        if (!pubnub.configuration.secretKey.isValid()) {
            throw PubNubException(PubNubError.SECRET_KEY_MISSING)
        }
        if (!configuration.subscribeKey.isValid()) {
            throw PubNubException(PubNubError.SUBSCRIBE_KEY_MISSING)
        }
        if ((channels + channelGroups + uuids).isEmpty()) {
            throw PubNubException(
                pubnubError = PubNubError.RESOURCES_MISSING,
                errorMessage = "At least one grant required",
            )
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<GrantTokenResponse> {
        val requestBody: GrantTokenRequestBody =
            GrantTokenRequestBody.of(
                ttl = ttl,
                channels = channels,
                groups = channelGroups,
                uuids = uuids,
                meta = meta,
                uuid = authorizedUUID,
            )
        return retrofitManager
            .accessManagerService
            .grantToken(configuration.subscribeKey, requestBody, queryParams)
    }

    override fun createResponse(input: Response<GrantTokenResponse>): PNGrantTokenResult {
        return input.body()!!.data.token.let { PNGrantTokenResult(it) }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNAccessManagerGrantToken

    override fun isAuthRequired(): Boolean = false

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.ACCESS_MANAGER
}
