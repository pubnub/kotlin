package com.pubnub.api.endpoints;

import com.pubnub.api.core.PnConfiguration;
import com.pubnub.api.core.Pubnub;
import okhttp3.mockwebserver.MockWebServer;

/**
 * Created by Max on 3/31/16.
 */
public class EndpointTest {

    protected Pubnub createPubNubInstance(MockWebServer server) {
        PnConfiguration pnConfiguration = new PnConfiguration();
        pnConfiguration.setOrigin(server.getHostName() + ":" + server.getPort());
        pnConfiguration.setSecure(false);
        pnConfiguration.setSubscribeKey("mySubscribeKey");
        pnConfiguration.setPublishKey("myPublishKey");
        pnConfiguration.setUuid("myUUID");

        return new Pubnub(pnConfiguration);
    }

}
