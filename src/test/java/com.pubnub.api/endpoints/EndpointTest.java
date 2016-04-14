package com.pubnub.api.endpoints;

import com.pubnub.api.core.PnConfiguration;
import com.pubnub.api.core.Pubnub;

public class EndpointTest {

    protected Pubnub createPubNubInstance(int port) {
        PnConfiguration pnConfiguration = new PnConfiguration();
        pnConfiguration.setOrigin("localhost" + ":" + port);
        pnConfiguration.setSecure(false);
        pnConfiguration.setSubscribeKey("mySubscribeKey");
        pnConfiguration.setPublishKey("myPublishKey");
        pnConfiguration.setUuid("myUUID");

        return new Pubnub(pnConfiguration);
    }
}
