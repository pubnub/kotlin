package com.pubnub.user

import org.aeonbits.owner.Config
import org.aeonbits.owner.ConfigFactory

@Config.Sources("file:../test.properties")
interface IntegrationTestConfiguration : Config {
    @get:Config.Key("subKey")
    val subscribeKey: String

    @get:Config.Key("pubKey")
    val publishKey: String

    @get:Config.Key("origin")
    val origin: String?
}

val IntegTestConf: IntegrationTestConfiguration = ConfigFactory.create(IntegrationTestConfiguration::class.java, System.getenv())
