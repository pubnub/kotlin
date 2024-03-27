package com.pubnub.api.integration.pam;

import org.junit.Ignore;

@Ignore
public class PamUserIntegrationTest extends AccessManagerIntegrationTest {

    @Override
    public String getPamLevel() {
        return LEVEL_USER;
    }
}
