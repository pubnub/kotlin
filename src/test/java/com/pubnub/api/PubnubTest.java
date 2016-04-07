package com.pubnub.api;

import com.pubnub.api.core.PnConfiguration;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import org.junit.Assert;
import org.junit.Before;

import java.io.IOException;

public class PubnubTest {
    private Pubnub pubnub;
    private PnConfiguration pnConfiguration;

    @Before
    public void beforeEach() throws IOException {
        pnConfiguration = new PnConfiguration();
        pnConfiguration.setSecure(true);
        pnConfiguration.setSubscribeKey("demo");
        pnConfiguration.setPublishKey("demo");
    }

    @org.junit.Test
    public void testCreateSuccess() throws IOException, PubnubException {
        pubnub = new Pubnub(pnConfiguration);
        Assert.assertNotNull("pubnub object is null", pubnub);
        Assert.assertNotNull(pubnub.getConfiguration());
        Assert.assertEquals("https://pubsub.pubnub.com", pubnub.getBaseUrl());
    }

}