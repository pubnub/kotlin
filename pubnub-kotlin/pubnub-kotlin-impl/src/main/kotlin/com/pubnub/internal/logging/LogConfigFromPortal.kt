package com.pubnub.internal.logging

import org.slf4j.event.Level

/**
 * Immutable configuration loaded from portal for logging behavior.
 * Thread-safe value object that represents portal-side logging configuration.
 *
 * @param isLoggingEnabled Whether logging to portal is enabled
 * @param logLevel The minimum log level to send to portal
 * @param userId The user ID for which logging is enabled
 */
class LogConfigFromPortal(val isLoggingEnabled: Boolean, val logLevel: Level, val userId: String)
