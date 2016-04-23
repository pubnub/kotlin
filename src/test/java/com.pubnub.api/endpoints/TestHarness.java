package com.pubnub.api.endpoints;

import com.pubnub.api.core.PnConfiguration;
import com.pubnub.api.core.Pubnub;

public class TestHarness {

    protected Pubnub createPubNubInstance(int port) {
        PnConfiguration pnConfiguration = new PnConfiguration();
        pnConfiguration.setOrigin("localhost" + ":" + port);
        pnConfiguration.setSecure(false);
        pnConfiguration.setSubscribeKey("mySubscribeKey");
        pnConfiguration.setPublishKey("myPublishKey");
        pnConfiguration.setUuid("myUUID");

        class MockedTimePubnub extends Pubnub {

            public MockedTimePubnub(PnConfiguration initialConfig) {
                super(initialConfig);
            }

            @Override
            public int getTimestamp() {
                return 1337;
            }

            @Override
            public String getVersion() {
                return "suchJava";
            }

        }

        return new MockedTimePubnub(pnConfiguration);
    }
}
