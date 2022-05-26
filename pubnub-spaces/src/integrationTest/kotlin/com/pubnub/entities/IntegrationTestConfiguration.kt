package com.pubnub.entities

import org.aeonbits.owner.Config
import org.aeonbits.owner.ConfigFactory

@Config.Sources("file:../test.properties")
interface IntegrationTestConfiguration : Config {
    @get:Config.Key("subKey")
    val subscribeKey: String

    @get:Config.Key("pubKey")
    val publishKey: String
}

val IntegTestConf: IntegrationTestConfiguration = ConfigFactory.create(IntegrationTestConfiguration::class.java, System.getenv())