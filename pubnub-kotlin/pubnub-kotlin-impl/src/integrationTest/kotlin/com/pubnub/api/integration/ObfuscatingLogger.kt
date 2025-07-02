package com.pubnub.api.integration

import com.pubnub.api.logging.CustomLogger
import com.pubnub.api.logging.LogMessage

class ObfuscatingLogger : CustomLogger {
    override fun getName(): String = "ObfuscatingLogger"


//    override fun trace(msg: String?) {
//        // Do nothing for trace level
//        // there is no need to override this ?
//    }

    override fun debug(msg: String?) {
        val obfuscatedMsg = obfuscateSensitiveData(msg)
        println("-=Sending obfuscated message to external system: $obfuscatedMsg")
        // The obfuscated message will be handled by the CompositeLogger
        // which will send it to both InMemoryLoggerAndToPortalSender and SLF4J
    }

    override fun debug(message: LogMessage) {
        println("-= Obfuscating data using  -=LogMessage=- object")
    }

    override fun info(msg: String?) {
        val obfuscatedMsg = obfuscateSensitiveData(msg)
        println("-=Sending obfuscated message to external system: $obfuscatedMsg")
        // The obfuscated message will be handled by the CompositeLogger
    }

    override fun warn(msg: String?) {
        val obfuscatedMsg = obfuscateSensitiveData(msg)
        println("-=Sending obfuscated message to external system: $obfuscatedMsg")
        // The obfuscated message will be handled by the CompositeLogger
    }

    override fun error(msg: String?) {
        val obfuscatedMsg = obfuscateSensitiveData(msg)
        println("-=Sending obfuscated message to external system: $obfuscatedMsg")
        // The obfuscated message will be handled by the CompositeLogger
    }

    private fun obfuscateSensitiveData(message: String?): String? {
        return message?.replace(Regex("(publishKey|subscribeKey|secretKey|authKey)=[^\\s&]+"), "$1=***")
    }

    // Add other Logger method overrides as needed...
}