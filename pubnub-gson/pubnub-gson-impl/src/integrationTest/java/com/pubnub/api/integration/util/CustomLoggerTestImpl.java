package com.pubnub.api.integration.util;

import com.pubnub.api.logging.CustomLogger;
import com.pubnub.api.logging.LogMessage;

import java.util.ArrayList;
import java.util.List;

public class CustomLoggerTestImpl implements CustomLogger {
    public static final List<String> stringMessages = new ArrayList<>();
    public static final List<LogMessage> logMessages = new ArrayList<>();

    @Override
    public String getName() {
        return "CustomLoggerTestImpl";
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
