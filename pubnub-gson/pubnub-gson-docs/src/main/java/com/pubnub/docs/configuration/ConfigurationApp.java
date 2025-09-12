package com.pubnub.docs.configuration;
// https://www.pubnub.com/docs/sdks/java/api-reference/configuration#basic-usage
// snippet.configurationMain

import com.pubnub.api.PubNubException;
import com.pubnub.api.crypto.CryptoModule;
import com.pubnub.api.UserId;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.logging.CustomLogger;
import com.pubnub.api.logging.LogMessage;
import com.pubnub.api.retry.RetryConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigurationApp {
    public static void main(String[] args) throws PubNubException {
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("demoUserId"), "demo");

        // Add necessary configuration
        configBuilder.publishKey("demo");
        configBuilder.customLoggers(Arrays.asList(new MyCustomLoggerImpl()));
        configBuilder.secure(true);
        configBuilder.filterExpression("such=wow");
        configBuilder.retryConfiguration(new RetryConfiguration.Linear());
        configBuilder.cryptoModule(CryptoModule.createAesCbcCryptoModule("enigma", true));

        PNConfiguration pnConfiguration = configBuilder.build();
        PubNub pubnub = PubNub.create(pnConfiguration);

        System.out.println("PubNub client initialized.");
    }
}

class MyCustomLoggerImpl implements CustomLogger {
    public static final List<String> stringMessages = new ArrayList<>();
    public static final List<LogMessage> logMessages = new ArrayList<>();

    @Override
    public String getName() {
        return "MyCustomLoggerImpl";
    }

    @Override
    public void info(String message) {
        if (message != null) {
            stringMessages.add(message);
        }
    }

    @Override
    public void info(LogMessage logMessage) {
        logMessages.add(logMessage);
    }

    @Override
    public void debug(String message) {
        if (message != null) {
            stringMessages.add(message);
        }
    }

    @Override
    public void debug(LogMessage logMessage) {
        logMessages.add(logMessage);
    }

    public static void clear() {
        stringMessages.clear();
        logMessages.clear();
    }
}
// snippet.end
