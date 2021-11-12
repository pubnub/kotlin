package com.pubnub.contract

import dev.nohus.autokonfig.AutoKonfig
import dev.nohus.autokonfig.types.BooleanSetting
import dev.nohus.autokonfig.types.StringSetting
import dev.nohus.autokonfig.withEnvironmentVariables
import dev.nohus.autokonfig.withResourceConfig

object ContractTestConfig {
    private fun AutoKonfig.withSafeResourceConfig(resource: String) = apply {
        try {
            withResourceConfig(resource)
        } catch (e: Exception) {
        }
    }

    private val config = AutoKonfig()
        .withSafeResourceConfig("config.properties")
        .withEnvironmentVariables()

    val pamSubKey by config.StringSetting()
    val pamPubKey by config.StringSetting()
    val pamSecKey by config.StringSetting()
    val pubKey by config.StringSetting()
    val subKey by config.StringSetting()
    val serverHostPort by config.StringSetting()
    val serverMock by config.BooleanSetting(true)
}
