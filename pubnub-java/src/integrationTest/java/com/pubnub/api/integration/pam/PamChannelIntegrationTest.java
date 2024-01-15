package com.pubnub.api.integration.pam;

public class PamChannelIntegrationTest extends AccessManagerIntegrationTest {

    @Override
    public String getPamLevel() {
        return LEVEL_CHANNEL;
    }
}
