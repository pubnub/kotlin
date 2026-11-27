package com.pubnub.docs.configuration.samples;

import com.pubnub.api.logging.LogMessage;

import java.util.ArrayList;
import java.util.List;

// Test-only access to MyCustomLoggerImpl of ConfigurationApp.java. Same package as the sample, so no reflection is needed.
// Not part of the docs text: this file has no snippet markers.
public final class ConfigurationLoggerProbe {
    private ConfigurationLoggerProbe() {
    }

    public static void clear() {
        MyCustomLoggerImpl.clear();
    }

    public static List<LogMessage> logMessages() {
        return new ArrayList<>(MyCustomLoggerImpl.logMessages);
    }
}
