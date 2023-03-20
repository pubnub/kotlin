package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.UserId

interface ConfigurationNeededForEndpoint {
    fun getSubscribeKey(): String
    fun getPublishKey(): String
    fun generatePnsdk(): String
    fun getUserId(): UserId
    fun includeInstanceIdentifier(): Boolean
    fun includeRequestIdentifier(): Boolean
    fun isAuthKeyValid(): Boolean
    fun getAuthKey(): String
}
