package com.pubnub;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;

@Config.Sources("file:../test.properties")
interface TestConfiguration extends Config {
    @Config.Key("subKey")
    String getSubscribeKey();

    @Config.Key("pubKey")
    String getPublishKey();

    TestConfiguration instance = ConfigFactory.create(TestConfiguration.class, System.getenv());
}

