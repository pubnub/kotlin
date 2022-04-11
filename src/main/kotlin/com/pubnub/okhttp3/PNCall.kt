package com.pubnub.okhttp3

import okhttp3.Call
import okhttp3.Connection
import org.slf4j.LoggerFactory
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.jvm.isAccessible

internal class PNCall(
    private val realCall: Call
) : Call by realCall {
    private val log = LoggerFactory.getLogger("PNCall")

    override fun cancel() {
        try {
            realCall.getConnection()?.socket()?.shutdownInput()
        } catch (t: Throwable) {
            // log.warn("Caught throwable when canceling call", t)
        }
        realCall.cancel()
    }

    private fun Call.getConnection(): Connection? =
        when {
            isTransmitterAvailable() -> accessField("transmitter")?.accessField("connection")
            isStreamAllocationAvailable() -> callPrivateFunction("streamAllocation")?.accessField("connection")
            else -> {
                log.warn("Unrecognized version of OkHttp client. This may cause unexpected behavior when Security Provider updated on Android")
                null
            }
        } as Connection?

    private fun <T : Call> T.isTransmitterAvailable(): Boolean = this::class.java.hasField("transmitter")

    private fun <T : Call> T.isStreamAllocationAvailable(): Boolean = this::class.java
        .hasMethod("streamAllocation")

    private fun <T : Any> T.accessField(fieldName: String): Any? {
        return javaClass.getDeclaredField(fieldName).let { field ->
            field.isAccessible = true
            return@let field.get(this)
        }
    }

    private fun <T : Any> T.callPrivateFunction(name: String, vararg args: Any?): Any? =
        this::class
            .declaredMemberFunctions
            .firstOrNull { it.name == name }
            ?.apply { isAccessible = true }
            ?.call(this, *args)

    private fun Class<*>.hasField(fieldName: String): Boolean =
        try { getDeclaredField(fieldName) != null } catch (e: Exception) { false }

    private fun Class<*>.hasMethod(methodName: String): Boolean = declaredMethods.any { it.name == methodName }
}
