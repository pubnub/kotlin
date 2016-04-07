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
        pnConfiguration.setUUID("myownUUID");
        pnConfiguration.setOrigin(server.getHostName() + ":" + server.getPort());
        pnConfiguration.setSecure(false);
        pnConfiguration.setSubscribeKey("mySubscribeKey");
        pnConfiguration.setPublishKey("myPublishKey");
        pnConfiguration.setUUID("myUUID");

        return new Pubnub(pnConfiguration);
    }

}
