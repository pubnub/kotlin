package com.pubnub.docs.logging.customLogger;

// snippet.customLoggerImplementation

import com.pubnub.api.logging.CustomLogger;
import com.pubnub.api.logging.LogMessage;
import com.pubnub.api.logging.LogMessageContent;
import com.pubnub.api.logging.LogMessageType;

public class MonitoringLogger implements CustomLogger {

    private final MonitoringService monitoringService = new MonitoringService();
    private final AnalyticsService analyticsService =  new AnalyticsService();

    @Override
    public String getName() {
        return "MonitoringLogger";
    }

    @Override
    public void error(LogMessage logMessage) {
        // Access structured data
        String instanceId = logMessage.getPubNubId();
        String timestamp = logMessage.getTimestamp();
        String location = logMessage.getLocation();

        // Handle different message types
        if (logMessage.getMessage() instanceof LogMessageContent.Error) {
            LogMessageContent.Error errorContent = (LogMessageContent.Error) logMessage.getMessage();

            monitoringService.reportError(
                    errorContent.getMessage(),
                    errorContent.getStack(),
                    errorContent.getType(),
                    instanceId,
                    timestamp,
                    location
            );
        } else if (logMessage.getMessage() instanceof LogMessageContent.NetworkRequest) {
            LogMessageContent.NetworkRequest requestContent =
                    (LogMessageContent.NetworkRequest) logMessage.getMessage();

            if (requestContent.getCanceled() || requestContent.getFailed()) {
                monitoringService.reportNetworkFailure(
                        requestContent.getOrigin() + requestContent.getPath(),
                        requestContent.getMethod().getValue(),
                        instanceId
                );
            }
        }
    }

    @Override
    public void warn(String message) {
        // Simple string-based warning for deprecations
        if (message != null) {
            monitoringService.logWarning(message);
        }
    }

    @Override
    public void debug(LogMessage logMessage) {
        // Log configuration changes
        if (logMessage.getType() == LogMessageType.OBJECT) {
            LogMessageContent.Object content = (LogMessageContent.Object) logMessage.getMessage();
            String operation = content.getOperation();

            if (operation != null && operation.contains("Configuration")) {
                analyticsService.trackConfigChange(
                        logMessage.getPubNubId(),
                        content.getArguments()
                );
            }
        }
    }

    // Other methods use default no-op implementation
}
// snippet.end
