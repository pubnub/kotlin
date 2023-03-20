package com.pubnub.api.endpoints.pubsub

interface TokenRetriever {
    fun getToken(): String?
}
