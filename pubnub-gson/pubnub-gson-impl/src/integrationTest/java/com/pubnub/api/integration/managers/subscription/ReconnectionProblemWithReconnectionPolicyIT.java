package com.pubnub.api.integration.managers.subscription;

import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.retry.RetryConfiguration;
import org.jetbrains.annotations.NotNull;


public class ReconnectionProblemWithReconnectionPolicyIT extends AbstractReconnectionProblemIT {
    @Override
    protected @NotNull PubNub privilegedClientPubNub() {
        PNConfiguration.Builder pnConfiguration = getPNConfiguration();
        pnConfiguration.subscribeKey(itPamTestConfig.pamSubKey());
        pnConfiguration.publishKey(itPamTestConfig.pamPubKey());
        pnConfiguration.subscribeTimeout(5);
        pnConfiguration.logVerbosity(PNLogVerbosity.BODY);
        pnConfiguration.retryConfiguration(new RetryConfiguration.Linear(2, 1));
        pnConfiguration.authKey(authKey);
        return PubNub.create(pnConfiguration.build());
    }
}
