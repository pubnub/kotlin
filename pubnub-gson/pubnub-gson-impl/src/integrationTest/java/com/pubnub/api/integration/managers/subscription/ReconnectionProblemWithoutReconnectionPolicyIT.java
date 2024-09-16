package com.pubnub.api.integration.managers.subscription;

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.retry.RetryConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static com.pubnub.api.enums.PNLogVerbosity.BODY;


public class ReconnectionProblemWithoutReconnectionPolicyIT extends AbstractReconnectionProblemIT {
    @Override
    protected @NotNull PubNub privilegedClientPubNub() {
        com.pubnub.api.java.v2.PNConfiguration.Builder pnConfiguration;
        try {
            pnConfiguration = com.pubnub.api.java.v2.PNConfiguration.builder(new UserId("pn-" + UUID.randomUUID()), "");
        } catch (PubNubException e) {
            throw new RuntimeException(e);
        }
        pnConfiguration.subscribeKey(itPamTestConfig.pamSubKey());
        pnConfiguration.publishKey(itPamTestConfig.pamPubKey());
        pnConfiguration.subscribeTimeout(SUBSCRIBE_TIMEOUT);
        pnConfiguration.logVerbosity(BODY);
        pnConfiguration.authKey(authKey);
        pnConfiguration.retryConfiguration(RetryConfiguration.None.INSTANCE);
        return PubNub.create(pnConfiguration.build());
    }
}
