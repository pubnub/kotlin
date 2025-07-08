package com.pubnub.internal.logging

import com.pubnub.api.logging.CustomLogger

/**
 * Configuration for logging system with validation.
 *
 * @param pnInstanceId The PubNub instance identifier - must not be blank
 * @param userId The user identifier - must not be blank
 * @param customLoggers Optional list of custom loggers to use
 */
class LogConfig(val pnInstanceId: String, val userId: String, val customLoggers: List<CustomLogger>? = null)
