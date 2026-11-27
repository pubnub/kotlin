package com.pubnub.docs.support

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.api.v2.callbacks.StatusListener
import java.lang.reflect.InvocationHandler
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.lang.reflect.Proxy
import java.util.Collections
import java.util.IdentityHashMap
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.function.Consumer

/**
 * A recording client. [wrap] returns a `java.lang.reflect.Proxy` over the Kotlin `com.pubnub.api.PubNub` or the Java
 * `com.pubnub.api.java.PubNub` interface (any interface works). The sample sees the same types and the same text.
 *
 * - Every interface typed return value is wrapped too, so builder chains (`pubnub.publish(...).async { }`) are covered.
 * - `async(callback)` passes the call through with a wrapping callback that records the `Result` before the sample's
 *   own callback runs. A sample that swallows errors in an empty `onFailure` block can no longer pass a failing call.
 * - `sync()` results and exceptions are recorded, the exception is thrown again.
 * - A `StatusListener` passed to the client is wrapped, so `PNStatus` values are recorded under the name `status`.
 * - Registered placeholder strings (see [placeholder]) are swapped for live values at the call boundary, also inside
 *   a list of strings. The sample text keeps its placeholder.
 *
 * Records are named after the call that started the chain: `pubnub.publish()...async {}` records under `publish`.
 */
class Recorder {
    /** One recorded outcome. [value] is the success value, [error] the failure. */
    class Record(val method: String, val kind: String, val value: Any?, val error: Throwable?) {
        val success: Boolean get() = error == null

        override fun toString(): String {
            val outcome =
                if (success) {
                    "ok"
                } else {
                    "failed: " + Redact.scrub(error?.message)
                }
            return "$method($kind): $outcome"
        }
    }

    private val records = CopyOnWriteArrayList<Record>()
    private val replacements = ConcurrentHashMap<String, String>()
    private val listenerWrappers: MutableMap<Any, Any> = Collections.synchronizedMap(IdentityHashMap())

    /** Registers a placeholder that a sample uses, with the live value to send instead. The live value is redacted. */
    fun placeholder(
        sampleValue: String,
        liveValue: String,
    ): Recorder {
        replacements[sampleValue] = liveValue
        Redact.register(liveValue)
        return this
    }

    /** Wraps [target], which implements the interface [type]. */
    fun <T : Any> wrap(
        target: T,
        type: Class<T>,
    ): T = proxy(target, type, null)

    /** Kotlin shortcut for a Kotlin client. */
    fun wrap(target: PubNub): PubNub = wrap(target, PubNub::class.java)

    /** For a Java client that the Kotlin code can only see as [Any]: [typeName] is `com.pubnub.api.java.PubNub`. */
    fun wrapByName(
        target: Any,
        typeName: String,
    ): Any = proxy(target, Class.forName(typeName), null)

    // ---- reading the records ------------------------------------------------------------------------------------

    fun all(): List<Record> = records.toList()

    fun records(method: String): List<Record> = records.filter { it.method == method }

    fun clear() = records.clear()

    /** Waits for [count] records of [method] and returns the last of them. */
    @JvmOverloads
    fun await(
        method: String,
        count: Int = 1,
        timeoutMs: Long = 15_000,
    ): Record {
        val deadline = System.nanoTime() + timeoutMs * 1_000_000
        while (records(method).size < count) {
            if (System.nanoTime() >= deadline) {
                throw AssertionError(
                    "no $count record(s) of '$method' within ${timeoutMs / 1000.0}s. Recorded so far: " +
                        (all().joinToString("; ") { it.toString() }.ifEmpty { "nothing" }),
                )
            }
            Thread.sleep(50)
        }
        return records(method)[count - 1]
    }

    /** Waits for a record of [method] and fails when it is a failure. Returns the success value. */
    @JvmOverloads
    fun awaitOk(
        method: String,
        count: Int = 1,
        timeoutMs: Long = 15_000,
    ): Any? {
        val record = await(method, count, timeoutMs)
        if (!record.success) {
            throw AssertionError("call '$method' failed: ${Redact.scrub(record.error?.toString())}", record.error)
        }
        return record.value
    }

    /** Waits for a record of [method] and fails when it is a success. Returns the error. */
    @JvmOverloads
    fun awaitFailure(
        method: String,
        count: Int = 1,
        timeoutMs: Long = 15_000,
    ): Throwable {
        val record = await(method, count, timeoutMs)
        return record.error ?: throw AssertionError("call '$method' was expected to fail, but it succeeded")
    }

    /** Fails when any record is a failure. */
    fun assertNoFailures() {
        val failed = records.filter { !it.success }
        if (failed.isNotEmpty()) {
            throw AssertionError("${failed.size} recorded call(s) failed: " + failed.joinToString("; ") { it.toString() })
        }
    }

    // ---- the proxy ----------------------------------------------------------------------------------------------

    private fun <T : Any> proxy(
        target: Any,
        type: Class<*>,
        origin: String?,
    ): T {
        @Suppress("UNCHECKED_CAST")
        return Proxy.newProxyInstance(type.classLoader, arrayOf(type), Handler(target, type, origin)) as T
    }

    /**
     * A staged builder (`pubNub.deleteFile().channel(..).fileName(..)`) returns a type variable at each step, which the
     * compiler erases to Object. The step is wrapped over every public interface of the real object, so the cast that the
     * caller's code performs works, and the chain keeps the label of the call that started it.
     */
    private fun stepProxy(
        target: Any,
        origin: String,
    ): Any? {
        val interfaces = LinkedHashSet<Class<*>>()
        var current: Class<*>? = target.javaClass
        while (current != null) {
            current.interfaces.filter { shouldWrap(it) }.forEach { interfaces.add(it) }
            current = current.superclass
        }
        if (interfaces.isEmpty()) {
            return null
        }
        val type = interfaces.firstOrNull { i -> i.methods.any { it.name == "async" || it.name == "sync" } } ?: interfaces.first()
        return Proxy.newProxyInstance(type.classLoader, interfaces.toTypedArray(), Handler(target, type, origin))
    }

    private fun shouldWrap(type: Class<*>): Boolean =
        type.isInterface &&
            Modifier.isPublic(type.modifiers) &&
            !type.name.startsWith("java.") &&
            !type.name.startsWith("javax.") &&
            !type.name.startsWith("kotlin.")

    /** The recording [Handler] of [arg] when it is a proxy of this or another recorder, otherwise null. */
    private fun handlerOf(arg: Any?): Handler? =
        if (arg != null && Proxy.isProxyClass(arg.javaClass)) {
            Proxy.getInvocationHandler(arg) as? Handler
        } else {
            null
        }

    private fun needsSwap(item: Any?): Boolean = (item is String && replacements.containsKey(item)) || handlerOf(item) != null

    // The real client checks the class of what it gets (a subscription set only accepts its own subscriptions), so an
    // argument that is one of our proxies goes back to the client as the real object.
    private fun swap(arg: Any?): Any? {
        if (arg is String) {
            return replacements[arg] ?: arg
        }
        if (arg is List<*> && arg.any { needsSwap(it) }) {
            return arg.map { swap(it) }
        }
        if (arg is Set<*> && arg.any { needsSwap(it) }) {
            return arg.map { swap(it) }.toSet()
        }
        return handlerOf(arg)?.target ?: arg
    }

    private fun record(
        method: String,
        kind: String,
        value: Any?,
        error: Throwable?,
    ) {
        records.add(Record(method, kind, value, error))
    }

    private fun statusRecorder(
        listener: StatusListener,
        origin: String,
    ): StatusListener =
        Proxy.newProxyInstance(
            StatusListener::class.java.classLoader,
            arrayOf(StatusListener::class.java),
        ) { _, method, args ->
            if (method.name == "status" && args != null && args.size == 2 && args[1] is PNStatus) {
                val status = args[1] as PNStatus
                record(origin, "status", status, status.exception)
            }
            try {
                method.invoke(listener, *(args ?: emptyArray()))
            } catch (e: InvocationTargetException) {
                throw e.targetException
            }
        } as StatusListener

    private inner class Handler(
        val target: Any,
        private val type: Class<*>,
        private val origin: String?,
    ) : InvocationHandler {
        // A builder chain keeps the name of the call that started it. An entity (a client, a channel) starts a new one.
        private val futureLike = type.methods.any { it.name == "async" || it.name == "sync" }

        override fun invoke(
            proxy: Any,
            method: Method,
            args: Array<Any?>?,
        ): Any? {
            val given = args ?: emptyArray()
            if (method.declaringClass == Any::class.java) {
                return when (method.name) {
                    "equals" -> proxy === given[0] || target == given[0]
                    "hashCode" -> target.hashCode()
                    else -> target.toString()
                }
            }
            val label = origin ?: method.name
            var swapped = given.map { swap(it) }.toTypedArray()

            if (method.name == "async" && swapped.size == 1 && swapped[0] is Consumer<*>) {
                val original = swapped[0] as Consumer<*>
                swapped[0] =
                    Consumer<Any?> { result ->
                        if (result is Result<*>) {
                            record(label, "async", result.getOrNull(), result.exceptionOrNull())
                        }
                        @Suppress("UNCHECKED_CAST")
                        (original as Consumer<Any?>).accept(result)
                    }
                return call(method, swapped)
            }
            if (method.name == "sync" && swapped.isEmpty()) {
                try {
                    val value = method.invoke(target)
                    record(label, "sync", value, null)
                    return value
                } catch (e: InvocationTargetException) {
                    record(label, "sync", null, e.targetException)
                    throw e.targetException
                }
            }
            // Only the overload that takes exactly a StatusListener can take our wrapper, the others (a SubscribeCallback)
            // need the object itself.
            if (swapped.size == 1 && swapped[0] is StatusListener && method.name.startsWith("addListener") &&
                method.parameterTypes[0] == StatusListener::class.java
            ) {
                val wrapper = statusRecorder(swapped[0] as StatusListener, "status")
                listenerWrappers[swapped[0]!!] = wrapper
                swapped = arrayOf(wrapper)
            } else if (swapped.size == 1 && method.name == "removeListener" && swapped[0] != null) {
                swapped = arrayOf(listenerWrappers[swapped[0]!!] ?: swapped[0])
            }
            val result = call(method, swapped)
            if (result != null && origin != null && method.genericReturnType is java.lang.reflect.TypeVariable<*>) {
                return stepProxy(result, label) ?: result
            }
            if (result != null && shouldWrap(method.returnType) && method.returnType.isInstance(result)) {
                val childOrigin =
                    if (futureLike) {
                        label
                    } else {
                        method.name
                    }
                return proxy<Any>(result, method.returnType, childOrigin)
            }
            return result
        }

        private fun call(
            method: Method,
            args: Array<Any?>,
        ): Any? =
            try {
                method.invoke(target, *args)
            } catch (e: InvocationTargetException) {
                throw e.targetException
            }
    }
}
