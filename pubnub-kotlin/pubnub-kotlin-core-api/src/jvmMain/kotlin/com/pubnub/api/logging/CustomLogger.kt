package com.pubnub.api.logging

interface CustomLogger {
    fun getName(): String {
        return "CustomLogger"
    }

    fun trace(message: String?) {
        // by default do nothing
    }

    fun trace(message: LogMessage) {
        // by default do nothing
    }

    fun debug(message: String?) {
        // by default do nothing
    }

    fun debug(message: LogMessage) {
        // by default do nothing
    }

    fun info(message: String?) {
        // by default do nothing
    }

    fun info(message: LogMessage) {
        // by default do nothing
    }

    fun warn(message: String?) {
        // by default do nothing
    }

    fun warn(message: LogMessage) {
        // by default do nothing
    }

    fun error(message: String?) {
        // by default do nothing
    }

    fun error(message: LogMessage) {
        // by default do nothing
    }
}
