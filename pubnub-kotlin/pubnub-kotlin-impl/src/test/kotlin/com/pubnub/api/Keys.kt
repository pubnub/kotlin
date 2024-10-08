package com.pubnub.api

import org.aeonbits.owner.Config
import org.aeonbits.owner.ConfigFactory

@Config.Sources("file:test.properties")
interface KeysConfig : Config {
    @get:Config.Key("SDK_SUB_KEY")
    val subscribeKey: String

    @get:Config.Key("SDK_PUB_KEY")
    val publishKey: String

    @get:Config.Key("SDK_PAM_SUB_KEY")
    val pamSubKey: String

    @get:Config.Key("SDK_PAM_PUB_KEY")
    val pamPubKey: String

    @get:Config.Key("SDK_PAM_SEC_KEY")
    val pamSecKey: String
}

val Keys: KeysConfig = ConfigFactory.create(KeysConfig::class.java, System.getenv())
