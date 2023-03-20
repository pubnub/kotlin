package com.pubnub.api.endpoints.pubsub

interface ConfigurationNeededForPublish {
    fun useEncryption(): Boolean
    fun getPublishKey(): String
    fun getSubscribeKey(): String
    fun getCipherKey(): String
    fun useRandomInitializationVector(): Boolean
}
