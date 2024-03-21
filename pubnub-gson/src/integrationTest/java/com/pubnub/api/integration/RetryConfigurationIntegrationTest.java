package com.pubnub.api.integration;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.retry.RetryConfiguration;
import com.pubnub.api.retry.RetryableEndpointGroup;
import com.pubnub.api.v2.callbacks.StatusListener;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.pubnub.api.integration.util.Utils.randomChannel;
import static org.junit.Assert.assertTrue;

public class RetryConfigurationIntegrationTest extends BaseIntegrationTest {
    @Test
    public void whenRetryConfigurationIsDefinedShouldGetProperStatus() throws InterruptedException, PubNubException {
        AtomicBoolean success = new AtomicBoolean();

        pubNub = getPubNub(builder -> {
            builder.setOrigin("ps.pndsn_notExisting_URI.com"); // we want to trigger UnknownHostException to initiate retry
            builder.setRetryConfiguration(new RetryConfiguration.Linear(1,2 ));
            builder.setHeartbeatInterval(1);
        });

        pubNub.addListener(new StatusListener() {
            @Override
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus pnStatus) {
                assertTrue(pnStatus.getCategory() == PNStatusCategory.PNConnectionError);
                success.set(true);
            }
        });

        pubNub.subscribe().channels(Arrays.asList(randomChannel())).execute();
        Thread.sleep(10000);
    }

    @Test
    public void canCreateRetryConfigurationsWithDifferentParameters() {
        int delayInSec = 2;
        int minDelay = 3;
        int maxDelay = 5;
        int maxRetryNumber = 2;
        List<RetryableEndpointGroup> excludedOperations = new ArrayList<>();

        RetryConfiguration linearRetryConfigurationWithNoParams = new RetryConfiguration.Linear();
        RetryConfiguration linearRetryConfigurationWithDelay = new RetryConfiguration.Linear(delayInSec);
        RetryConfiguration linearRetryConfigurationWithDelayAndMaxRetry = new RetryConfiguration.Linear(delayInSec, maxRetryNumber);
        RetryConfiguration linearRetryConfigurationWithAllParams = new RetryConfiguration.Linear(delayInSec, maxRetryNumber, excludedOperations);

        RetryConfiguration exponentialRetryConfigurationWithNoParams = new RetryConfiguration.Exponential();
        RetryConfiguration exponentialRetryConfigurationWithMinMax = new RetryConfiguration.Exponential(minDelay, maxDelay);
        RetryConfiguration exponentialRetryConfigurationWithMinMaxAndRetry = new RetryConfiguration.Exponential(minDelay, maxDelay, maxRetryNumber);
        RetryConfiguration exponentialRetryConfigurationWithAllParams = new RetryConfiguration.Exponential(minDelay, maxDelay, maxRetryNumber, excludedOperations);
    }
}
