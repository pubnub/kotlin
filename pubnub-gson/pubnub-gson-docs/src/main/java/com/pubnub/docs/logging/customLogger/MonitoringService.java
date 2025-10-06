package com.pubnub.docs.logging.customlogger;

import java.util.List;

public class MonitoringService {


    public void reportError(String message, List<String> stack, String type, String instanceId, String timestamp, String location) {
        // no need to implement this is not part of sample
    }

    public void reportNetworkFailure(String url, String method, String instanceId) {
        // no need to implement this is not part of sample
    }

    public void logWarning(String message) {
        // no need to implement this is not part of sample
    }
}
