package com.pubnub.api

import org.aeonbits.owner.Config
import org.aeonbits.owner.ConfigFactory

@Config.Sources("file:test.properties")
interface KeysConfig : Config {
    @get:Config.Key("subKey")
    val subKey: String

    @get:Config.Key("pubKey")
    val pubKey: String

    @get:Config.Key("pamSubKey")
    val pamSubKey: String

    @get:Config.Key("pamPubKey")
    val pamPubKey: String

    @get:Config.Key("pamSecKey")
    val pamSecKey: String
}

val Keys: KeysConfig = ConfigFactory.create(KeysConfig::class.java, System.getenv())
