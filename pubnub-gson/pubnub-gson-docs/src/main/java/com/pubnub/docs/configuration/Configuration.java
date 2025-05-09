package com.pubnub.docs.configuration;
// https://www.pubnub.com/docs/sdks/java/api-reference/configuration#basic-usage
// snippet.configurationMain

import com.pubnub.api.PubNubException;
import com.pubnub.api.crypto.CryptoModule;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.UserId;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.retry.RetryConfiguration;

public class Configuration {
    public static void main(String[] args) throws PubNubException {
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("demoUserId"), "demo");

        // Add necessary configuration
        configBuilder.publishKey("demo");
        configBuilder.logVerbosity(PNLogVerbosity.BODY);
        configBuilder.secure(true);
        configBuilder.filterExpression("such=wow");
        configBuilder.retryConfiguration(new RetryConfiguration.Linear());
        configBuilder.cryptoModule(CryptoModule.createAesCbcCryptoModule("enigma", true));

        PNConfiguration pnConfiguration = configBuilder.build();
        PubNub pubnub = PubNub.create(pnConfiguration);

        System.out.println("PubNub client initialized.");
    }
}
// snippet.end
