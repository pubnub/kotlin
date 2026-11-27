package com.pubnub.docs.logging.samples;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

// The service the custom logger sample talks to. The docs sample leaves it out ("not part of the sample"). Here it records
// every call, so that a test can prove what the logger forwarded.
public class AnalyticsService {
    public static final List<String> CONFIG_CHANGES = new CopyOnWriteArrayList<>();

    public static void clear() {
        CONFIG_CHANGES.clear();
    }

    public void trackConfigChange(String instanceId, Map<String, Object> configDetail) {
        CONFIG_CHANGES.add(instanceId);
    }
}
