package com.pubnub.api.endpoints.access

import com.pubnub.api.Endpoint
import com.pubnub.api.PNConfiguration.Companion.isValid
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.server.access_manager.v3.RevokeTokenResponse
import retrofit2.Call
import retrofit2.Response
import java.net.URLEncoder

class RevokeToken(
    pubnub: PubNub,
    private val token: String
) : Endpoint<RevokeTokenResponse, Unit>(pubnub) {
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
        return pubnub.retrofitManager
            .accessManagerService
            .revokeToken(pubnub.configuration.subscribeKey, repairEncoding(token), queryParams)
    }

    override fun createResponse(input: Response<RevokeTokenResponse>): Unit = Unit

    override fun operationType(): PNOperationType = PNOperationType.PNAccessManagerRevokeToken
    override fun isAuthRequired(): Boolean = false

    private fun repairEncoding(token: String): String {
        return URLEncoder.encode(token, "utf-8").replace("+", "%20")
    }
}
