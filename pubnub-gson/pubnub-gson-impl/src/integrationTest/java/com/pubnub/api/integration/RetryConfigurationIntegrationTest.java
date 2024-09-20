package com.pubnub.api.integration;

import com.pubnub.api.PubNubException;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.callbacks.StatusListener;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.retry.RetryConfiguration;
import com.pubnub.api.retry.RetryableEndpointGroup;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.pubnub.api.integration.util.Utils.randomChannel;
import static org.junit.Assert.assertSame;

public class RetryConfigurationIntegrationTest extends BaseIntegrationTest {
    @Test
    public void whenRetryConfigurationIsDefinedShouldGetProperStatus() throws InterruptedException, PubNubException {
        AtomicBoolean success = new AtomicBoolean();

        pubNub = getPubNub(builder -> {
            builder.origin("ps.pndsn_notExisting_URI.com"); // we want to trigger UnknownHostException to initiate retry
            builder.retryConfiguration(new RetryConfiguration.Linear(1,2 ));
            builder.heartbeatInterval(1);
        });

        pubNub.addListener(new StatusListener() {
            @Override
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus pnStatus) {
                assertSame(pnStatus.getCategory(), PNStatusCategory.PNConnectionError);
                success.set(true);
            }
        });

        pubNub.subscribe().channels(Collections.singletonList(randomChannel())).execute();
        Thread.sleep(10000);
    }

    @Test
    public void canCreateRetryConfigurationsWithDifferentParameters() {
        int delayInSec = 2;
        int minDelay = 3;
        int maxDelay = 5;
        int maxRetryNumber = 2;
        List<RetryableEndpointGroup> excludedOperations = new ArrayList<>();

        RetryConfiguration noneRetryConfiguration = RetryConfiguration.None.INSTANCE;

        RetryConfiguration linearRetryConfigurationWithNoParamsa = new RetryConfiguration.Linear();
        RetryConfiguration linearRetryConfigurationWithDelay = new RetryConfiguration.Linear(delayInSec);
        RetryConfiguration linearRetryConfigurationWithDelayAndMaxRetry = new RetryConfiguration.Linear(delayInSec, maxRetryNumber);
        RetryConfiguration linearRetryConfigurationWithAllParams = new RetryConfiguration.Linear(delayInSec, maxRetryNumber, excludedOperations);

        RetryConfiguration exponentialRetryConfigurationWithNoParams = new RetryConfiguration.Exponential();
        RetryConfiguration exponentialRetryConfigurationWithMinMax = new RetryConfiguration.Exponential(minDelay, maxDelay);
        RetryConfiguration exponentialRetryConfigurationWithMinMaxAndRetry = new RetryConfiguration.Exponential(minDelay, maxDelay, maxRetryNumber);
        RetryConfiguration exponentialRetryConfigurationWithAllParams = new RetryConfiguration.Exponential(minDelay, maxDelay, maxRetryNumber, excludedOperations);
    }
}
