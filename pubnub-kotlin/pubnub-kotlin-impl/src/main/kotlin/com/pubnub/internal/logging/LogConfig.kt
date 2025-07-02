package com.pubnub.internal.logging

import com.pubnub.api.logging.CustomLogger

class LogConfig(val pnInstanceId: String, val userId: String, val customLoggers: List<CustomLogger>? = null)
