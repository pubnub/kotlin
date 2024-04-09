package com.pubnub.api.integration

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
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

        val configBuilder = PNConfiguration.builder(UserId(expectedUuid), Keys.subKey) {
            publishKey = Keys.pubKey
        }
        val pubNub = PubNub.create(configBuilder.build())

        Assert.assertEquals(expectedUuid, pubNub.configuration.userId.value)
        Assert.assertEquals(Keys.subKey, pubNub.configuration.subscribeKey)
        Assert.assertEquals(Keys.pubKey, pubNub.configuration.publishKey)
    }

    @Test
    fun `create configuration override`() {
        val expectedUuid = PubNub.generateUUID()

        val configBuilder = PNConfiguration.builder(UserId(expectedUuid), Keys.subKey) {
            publishKey = Keys.pubKey
        }
        val config = configBuilder.build()

        Assert.assertEquals(Keys.subKey, config.subscribeKey)
        Assert.assertEquals(Keys.pubKey, config.publishKey)


        val overrideConfig = PNConfigurationOverride.from(config).apply {
            publishKey = "overridePublishKey"
        }.build()

        Assert.assertEquals(Keys.subKey, overrideConfig.subscribeKey)
        Assert.assertEquals("overridePublishKey", overrideConfig.publishKey)
    }

    @Test
    fun `use configuration override with Publish`() {
        val expectedUuid = PubNub.generateUUID()

        val configBuilder = PNConfiguration.builder(UserId(expectedUuid), Keys.subKey) {
            publishKey = "rubbishKey"
        }
        val config = configBuilder.build()
        val pubnub = PubNub.create(config)

        val overrideConfig = PNConfigurationOverride.from(config).apply {
            publishKey = Keys.pubKey
        }.build()

        pubnub.publish(randomChannel(), "message").overrideConfiguration(overrideConfig).sync()
        // no exception expected
    }

    @Test
    fun `use configuration override builder with Publish`() {
        val expectedUuid = PubNub.generateUUID()

        val configBuilder = PNConfiguration.builder(UserId(expectedUuid), Keys.subKey) {
            publishKey = "rubbishKey"
        }
        val pubnub = PubNub.create(configBuilder.build())

        pubnub.publish(randomChannel(), "message").overrideConfiguration { publishKey = Keys.pubKey }.sync()
        // no exception expected
    }
}
