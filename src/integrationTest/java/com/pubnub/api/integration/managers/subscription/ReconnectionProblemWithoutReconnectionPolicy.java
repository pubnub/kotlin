package com.pubnub.api.integration.managers.subscription;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;

import static com.pubnub.api.enums.PNLogVerbosity.BODY;
import static com.pubnub.api.enums.PNReconnectionPolicy.NONE;


public class ReconnectionProblemWithoutReconnectionPolicy extends AbstractReconnectionProblem {
    @Override
    protected PubNub privilegedClientPubNub() {
        final PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(itPamTestConfig.subscribeKey());
        pnConfiguration.setPublishKey(itPamTestConfig.publishKey());
        pnConfiguration.setSubscribeTimeout(SUBSCRIBE_TIMEOUT);
        pnConfiguration.setLogVerbosity(BODY);
        pnConfiguration.setAuthKey(itPamTestConfig.authKey());
        pnConfiguration.setReconnectionPolicy(NONE);
        return new PubNub(pnConfiguration);
    }
}
