package com.pubnub.api.integration.util;

import org.aeonbits.owner.Config;

@Config.Sources({"file:ittest.properties"})
public interface ITTestConfig extends Config {

    @Config.Key("SUB_KEY")
    String subscribeKey();

    @Config.Key("PUB_KEY")
    String publishKey();

    @Config.Key("PAM_SUB_KEY")
    String pamSubKey();

    @Config.Key("PAM_PUB_KEY")
    String pamPubKey();

    @Config.Key("PAM_SEC_KEY")
    String pamSecKey();
}
