package com.pubnub.api.integration

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.integration.LoggingIntegrationTest.CustomLoggerTestImpl
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.PNConfigurationOverride
import com.pubnub.test.CommonUtils.randomChannel
import com.pubnub.test.Keys
import org.junit.Assert
import org.junit.Test

class PNConfigurationIntegrationTests : BaseIntegrationTest() {
    @Test
    fun `create pubnub with immutable configuration`() {
        val expectedUuid = PubNub.generateUUID()

        val configBuilder = PNConfiguration.builder(UserId(expectedUuid), Keys.subKey)
        val pubNub = PubNub.create(configBuilder.build())

        Assert.assertEquals(expectedUuid, pubNub.configuration.userId.value)
        Assert.assertEquals(Keys.subKey, pubNub.configuration.subscribeKey)

        configBuilder.userId = UserId("unexpected")
        configBuilder.build()

        Assert.assertEquals(expectedUuid, pubNub.configuration.userId.value)
    }

    @Test
    fun `create configuration with configuration action block`() {
        val expectedUuid = PubNub.generateUUID()
        val expectedAuthToken = "token"

        val configBuilder = PNConfiguration.builder(UserId(expectedUuid), Keys.subKey) {
            publishKey = Keys.pubKey
            authToken = expectedAuthToken
            logVerbosity = PNLogVerbosity.NONE
            customLoggers = listOf(CustomLoggerTestImpl())
        }
        val pubNub = PubNub.create(configBuilder.build())

        Assert.assertEquals(expectedUuid, pubNub.configuration.userId.value)
        Assert.assertEquals(Keys.subKey, pubNub.configuration.subscribeKey)
        Assert.assertEquals(Keys.pubKey, pubNub.configuration.publishKey)
        Assert.assertEquals(expectedAuthToken, pubNub.configuration.authToken)
        Assert.assertEquals(expectedAuthToken, pubNub.getToken())
    }

    @Test
    fun `create configuration override`() {
        val expectedUuid = PubNub.generateUUID()
        val expectedAuthToken = "token"

        val configBuilder = PNConfiguration.builder(UserId(expectedUuid), Keys.subKey) {
            publishKey = Keys.pubKey
            authToken = "token$expectedAuthToken"
        }
        val config = configBuilder.build()

        Assert.assertEquals(Keys.subKey, config.subscribeKey)
        Assert.assertEquals(Keys.pubKey, config.publishKey)

        val overrideConfig = PNConfigurationOverride.from(config).apply {
            publishKey = "overridePublishKey"
            authToken = expectedAuthToken
        }.build()

        Assert.assertEquals(Keys.subKey, overrideConfig.subscribeKey)
        Assert.assertEquals("overridePublishKey", overrideConfig.publishKey)
        Assert.assertEquals(expectedAuthToken, overrideConfig.authToken)
    }

    @Test
    fun `use configuration override with Publish`() {
        val expectedUuid = PubNub.generateUUID()
        val expectedAuthToken = "token"

        val configBuilder = PNConfiguration.builder(UserId(expectedUuid), Keys.subKey) {
            publishKey = "rubbishKey"
            authToken = "old$expectedAuthToken"
        }
        val config = configBuilder.build()
        val pubnub = PubNub.create(config)

        val overrideConfig = PNConfigurationOverride.from(config).apply {
            publishKey = Keys.pubKey
            authToken = expectedAuthToken
        }.build()

        pubnub.publish(randomChannel(), "message").overrideConfiguration(overrideConfig).sync()
        // no exception expected
    }

    @Test
    fun `use configuration override builder with Publish`() {
        val expectedUuid = PubNub.generateUUID()
        val expectedAuthToken = "token"

        val configBuilder = PNConfiguration.builder(UserId(expectedUuid), Keys.subKey) {
            publishKey = "rubbishKey"
            authToken = "old$expectedAuthToken"
        }
        val pubnub = PubNub.create(configBuilder.build())

        pubnub.publish(randomChannel(), "message").overrideConfiguration {
            publishKey = Keys.pubKey
            authToken = expectedAuthToken
        }.sync()
        // no exception expected
    }
}
