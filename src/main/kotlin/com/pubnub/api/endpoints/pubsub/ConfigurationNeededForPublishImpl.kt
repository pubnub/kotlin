package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PNConfiguration.Companion.isValid

class ConfigurationNeededForPublishImpl(val pnConfiguration: PNConfiguration) : ConfigurationNeededForPublish {
    override fun useEncryption(): Boolean {
        return pnConfiguration.cipherKey.isValid()
    }

    override fun getPublishKey(): String {
        return pnConfiguration.publishKey
    }

    override fun getSubscribeKey(): String {
        return pnConfiguration.subscribeKey
    }

    override fun getCipherKey(): String {
        return pnConfiguration.cipherKey
    }

    override fun useRandomInitializationVector(): Boolean {
        return pnConfiguration.useRandomInitializationVector
    }
}
