package com.pubnub.api.integration.util;

import org.aeonbits.owner.Config;

@Config.Sources({"file:test.properties"})
public interface ITTestConfig extends Config {

    @Config.Key("subKey")
    String subscribeKey();

    @Config.Key("pubKey")
    String publishKey();

    @Config.Key("pamSubKey")
    String pamSubKey();

    @Config.Key("pamPubKey")
    String pamPubKey();

    @Config.Key("pamSecKey")
    String pamSecKey();
}
