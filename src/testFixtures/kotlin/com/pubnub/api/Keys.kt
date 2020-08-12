package com.pubnub.api

import dev.nohus.autokonfig.AutoKonfig
import dev.nohus.autokonfig.types.StringSetting
import dev.nohus.autokonfig.withEnvironmentVariables
import dev.nohus.autokonfig.withResourceConfig

object Keys {
    private fun AutoKonfig.withSafeResourceConfig(resource: String) = apply {
        try {
            withResourceConfig(resource)
        } catch (e: Exception) {
        }
    }

    private val config = AutoKonfig()
        .withSafeResourceConfig("config.properties")
        .withEnvironmentVariables()

    val pubKey by config.StringSetting()
    val subKey by config.StringSetting()
    val pamPubKey by config.StringSetting()
    val pamSubKey by config.StringSetting()
    val pamSecKey by config.StringSetting()
}
