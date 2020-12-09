package com.pubnub.api.integration.managers.subscription;

import org.aeonbits.owner.Config;

@Config.Sources({"file:it_pam_test.properties"})
public interface ITPamTestConfig extends Config {

    @Key("SUB_KEY")
    String subscribeKey();

    @Key("PUB_KEY")
    String publishKey();

    @Key("SEC_KEY")
    String secretKey();

    @Key("AUTH_KEY")
    String authKey();
}
