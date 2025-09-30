package com.pubnub.api.integration;

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.integration.util.CustomLoggerTestImpl;
import com.pubnub.api.integration.util.ITTestConfig;
import com.pubnub.api.integration.util.Utils;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.java.v2.PNConfigurationOverride;
import org.aeonbits.owner.ConfigFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PNConfigurationIntegrationTests {
    final ITTestConfig itTestConfig = ConfigFactory.create(ITTestConfig.class, System.getenv());
    
    @Test
    public void createPubnubWithImmutableConfiguration() throws PubNubException {
        String expectedUuid = PubNub.generateUUID();

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId(expectedUuid), itTestConfig.subscribeKey());
        PubNub pubNub = PubNub.create(configBuilder.build());

        Assert.assertEquals(expectedUuid, pubNub.getConfiguration().getUserId().getValue());
        Assert.assertEquals(itTestConfig.subscribeKey(), pubNub.getConfiguration().getSubscribeKey());

        configBuilder.setUserId(new UserId("unexpected"));
        configBuilder.build();

        Assert.assertEquals(expectedUuid, pubNub.getConfiguration().getUserId().getValue());
    }

    @Test
    public void createConfigurationWithConfigurationActionBlock() throws PubNubException {
        String expectedUuid = PubNub.generateUUID();

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId(expectedUuid), itTestConfig.subscribeKey())
                .publishKey(itTestConfig.publishKey())
                .customLoggers( Arrays.asList(new CustomLoggerTestImpl()))
                .secretKey("fa")
                ;
        PubNub pubNub = PubNub.create(configBuilder.build());

        Assert.assertEquals(expectedUuid, pubNub.getConfiguration().getUserId().getValue());
        Assert.assertEquals(itTestConfig.subscribeKey(), pubNub.getConfiguration().getSubscribeKey());
        Assert.assertEquals(itTestConfig.publishKey(), pubNub.getConfiguration().getPublishKey());
        Assert.assertTrue(pubNub.getConfiguration().getCustomLoggers().get(0) instanceof CustomLoggerTestImpl);
    }

    @Test
    public void createConfigurationOverride() throws PubNubException {
        String expectedUuid = PubNub.generateUUID();

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId(expectedUuid), itTestConfig.subscribeKey())
            .publishKey(itTestConfig.publishKey());

        PNConfiguration config = configBuilder.build();

        Assert.assertEquals(itTestConfig.subscribeKey(), config.getSubscribeKey());
        Assert.assertEquals(itTestConfig.publishKey(), config.getPublishKey());


        PNConfiguration overrideConfig = PNConfigurationOverride.from(config).publishKey("overridePublishKey").build();

        Assert.assertEquals(itTestConfig.subscribeKey(), overrideConfig.getSubscribeKey());
        Assert.assertEquals("overridePublishKey", overrideConfig.getPublishKey());
    }

    @Test
    public void useConfigurationOverrideWithPublish() throws PubNubException {
        String expectedUuid = PubNub.generateUUID();

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId(expectedUuid), itTestConfig.subscribeKey())
                .publishKey("rubbishKey");
        PNConfiguration config = configBuilder.build();
        PubNub pubnub = PubNub.create(config);

        PNConfiguration overrideConfig = PNConfigurationOverride.from(config).publishKey(itTestConfig.publishKey()).build();

        pubnub.publish().channel(Utils.randomChannel()).message("message").overrideConfiguration(overrideConfig).sync();
        // no exception expected
    }
}
