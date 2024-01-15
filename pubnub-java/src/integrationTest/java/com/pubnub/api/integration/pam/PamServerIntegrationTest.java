package com.pubnub.api.integration.pam;

public class PamServerIntegrationTest extends AccessManagerIntegrationTest {

    @Override
    public String getPamLevel() {
        return LEVEL_APP;
    }

    @Override
    boolean performOnServer() {
        return true;
    }
}
