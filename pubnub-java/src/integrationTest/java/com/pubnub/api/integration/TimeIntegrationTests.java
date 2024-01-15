package com.pubnub.api.integration;

import com.pubnub.api.integration.util.BaseIntegrationTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;


public class TimeIntegrationTests extends BaseIntegrationTest {

    @Test
    public void testGetPubNubTime() {
        final AtomicBoolean success = new AtomicBoolean();

        pubNub.time()
                .async((result, status) -> {
                    assert result != null;
                    Assert.assertNotNull(result.getTimetoken());
                    Assert.assertFalse(status.isError());
                    success.set(true);
                });

        listen(success);
    }
}
