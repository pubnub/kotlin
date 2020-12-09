package com.pubnub.api.integration.managers.subscription;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.enums.PNLogVerbosity;

import static com.pubnub.api.enums.PNReconnectionPolicy.LINEAR;


public class ReconnectionProblemWithReconnectionPolicy extends AbstractReconnectionProblem {
    @Override
    protected PubNub privilegedClientPubNub() {
        final PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(itPamTestConfig.subscribeKey());
        pnConfiguration.setPublishKey(itPamTestConfig.publishKey());
        pnConfiguration.setSubscribeTimeout(5);
        pnConfiguration.setLogVerbosity(PNLogVerbosity.BODY);
        pnConfiguration.setReconnectionPolicy(LINEAR);
        pnConfiguration.setAuthKey(itPamTestConfig.authKey());
        return new PubNub(pnConfiguration);
    }
}
