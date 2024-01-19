package com.pubnub.api.integration.pam;

public class PamUserIntegrationTest extends AccessManagerIntegrationTest {

    @Override
    public String getPamLevel() {
        return LEVEL_USER;
    }
}
