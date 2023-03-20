package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PNConfiguration.Companion.isValid
import com.pubnub.api.UserId

class ConfigurationNeededForEndpointImpl(val pnConfiguration: PNConfiguration, private val pnVersion: String) :
    ConfigurationNeededForEndpoint {
    override fun getSubscribeKey(): String {
        return pnConfiguration.subscribeKey
    }

    override fun getPublishKey(): String {
        return pnConfiguration.publishKey
    }

    override fun generatePnsdk(): String {
        return pnConfiguration.generatePnsdk(pnVersion)
    }

    override fun getUserId(): UserId {
        return pnConfiguration.userId
    }

    override fun includeInstanceIdentifier(): Boolean {
        return pnConfiguration.includeInstanceIdentifier
    }

    override fun includeRequestIdentifier(): Boolean {
        return pnConfiguration.includeRequestIdentifier
    }

    override fun isAuthKeyValid(): Boolean {
        return pnConfiguration.authKey.isValid()
    }

    override fun getAuthKey(): String {
        return pnConfiguration.authKey
    }
}
