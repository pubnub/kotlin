package com.pubnub.contract

import org.aeonbits.owner.Config
import org.aeonbits.owner.ConfigFactory

@Config.Sources("file:test.properties")
interface ContractTestKeysConfig : Config {
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

    @get:Config.Key("serverHostPort")
    val serverHostPort: String

    @get:Config.Key("serverMock")
    @get:Config.DefaultValue("true")
    val serverMock: Boolean

    @get:Config.Key("dataFilesLocation")
    @get:Config.DefaultValue("src/test/resources/sdk-specifications/features/data")
    val dataFilesLocation: String
}

val ContractTestConfig: ContractTestKeysConfig = ConfigFactory.create(ContractTestKeysConfig::class.java, System.getenv())
