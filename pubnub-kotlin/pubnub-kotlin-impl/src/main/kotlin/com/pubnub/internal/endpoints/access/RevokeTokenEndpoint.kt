package com.pubnub.internal.endpoints.access

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.access.RevokeToken
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.api.v2.PNConfiguration.Companion.isValid
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.access_manager.v3.RevokeTokenResponse
import retrofit2.Call
import retrofit2.Response
import java.net.URLEncoder

class RevokeTokenEndpoint(
    pubnub: PubNubImpl,
    private val token: String,
) : EndpointCore<RevokeTokenResponse, Unit>(pubnub), RevokeToken {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun validateParams() {
        super.validateParams()
        if (!pubnub.configuration.secretKey.isValid()) {
            throw PubNubException(PubNubError.SECRET_KEY_MISSING)
        }
        if (token.isBlank()) {
            throw PubNubException(PubNubError.TOKEN_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<RevokeTokenResponse> {
        log.trace(
            LogMessage(
                message = LogMessageContent.Object(
                    message = mapOf(
                        "token" to token,
                        "queryParams" to queryParams
                    )
                ),
                details = "RevokeToken API call",
            )
        )

        return retrofitManager
            .accessManagerService
            .revokeToken(configuration.subscribeKey, repairEncoding(token), queryParams)
    }

    override fun createResponse(input: Response<RevokeTokenResponse>): Unit = Unit

    override fun operationType(): PNOperationType = PNOperationType.PNAccessManagerRevokeToken

    override fun isAuthRequired(): Boolean = false

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.ACCESS_MANAGER

    private fun repairEncoding(token: String): String {
        return URLEncoder.encode(token, "utf-8").replace("+", "%20")
    }
}
