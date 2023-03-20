package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.managers.TokenManager

class TokenRetrieverImpl(val tokenManager: TokenManager) : TokenRetriever {

    override fun getToken(): String? {
        return tokenManager.getToken()
    }
}
