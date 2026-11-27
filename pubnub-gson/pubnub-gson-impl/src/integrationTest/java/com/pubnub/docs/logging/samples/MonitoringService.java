package com.pubnub.docs.logging.samples;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

// The service the custom logger sample talks to. The docs sample leaves it out ("not part of the sample"). Here it records
// every call, so that a test can prove what the logger forwarded.
public class MonitoringService {
    public static final List<String> ERRORS = new CopyOnWriteArrayList<>();
    public static final List<String> NETWORK_FAILURES = new CopyOnWriteArrayList<>();
    public static final List<String> WARNINGS = new CopyOnWriteArrayList<>();

    public static void clear() {
        ERRORS.clear();
        NETWORK_FAILURES.clear();
        WARNINGS.clear();
    }

    public void reportError(String message, List<String> stack, String type, String instanceId, String timestamp, String location) {
        ERRORS.add(message);
    }

    public void reportNetworkFailure(String url, String method, String instanceId) {
        NETWORK_FAILURES.add(method + " " + url);
    }

    public void logWarning(String message) {
        WARNINGS.add(message);
    }
}
