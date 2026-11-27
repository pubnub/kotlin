package com.pubnub.docs.support

import com.pubnub.api.v2.PNConfiguration

/**
 * The hidden line of a "Main" sample calls this inside the config builder, so the visible text keeps its "demo" keys:
 *
 *     // snippet.hide
 *     com.pubnub.docs.support.LiveKeys.apply(this, "open")
 *     // snippet.show
 *
 * The Kotlin builder is typed. The Java builder (`com.pubnub.api.java.v2.PNConfiguration.Builder`) is not on the
 * classpath of the Kotlin module, so the [Any] overload reaches it by reflection.
 */
object LiveKeys {
    /** Sets the subscribe, publish and (when the role has one) secret key of [role] on a Kotlin builder. */
    @JvmStatic
    fun apply(
        builder: PNConfiguration.Builder,
        role: String,
    ) {
        builder.subscribeKey = Env.subscribeKey(role)
        builder.publishKey = Env.publishKey(role)
        Env.secretKey(role)?.let { builder.secretKey = it }
    }

    /** Same for the Java builder. */
    @JvmStatic
    fun apply(
        builder: Any,
        role: String,
    ) {
        if (builder is PNConfiguration.Builder) {
            apply(builder, role)
            return
        }
        call(builder, "subscribeKey", Env.subscribeKey(role))
        call(builder, "publishKey", Env.publishKey(role))
        Env.secretKey(role)?.let { call(builder, "secretKey", it) }
    }

    /**
     * The shortest presence timeout the SDK accepts (20 seconds, the default is 300), for every client of the support layer.
     * It is not part of [apply], because the hidden line of a "Main" sample runs after the sample's own builder lines and must
     * not override them.
     */
    const val SHORT_PRESENCE_TIMEOUT = 20

    @JvmStatic
    fun shortPresence(builder: PNConfiguration.Builder) {
        builder.presenceTimeout = SHORT_PRESENCE_TIMEOUT
    }

    /** Same for the Java builder. */
    @JvmStatic
    fun shortPresence(builder: Any) {
        if (builder is PNConfiguration.Builder) {
            shortPresence(builder)
            return
        }
        val builderType = Class.forName("com.pubnub.api.java.v2.PNConfiguration\$Builder")
        builderType.getMethod("presenceTimeout", Int::class.javaPrimitiveType).invoke(builder, SHORT_PRESENCE_TIMEOUT)
    }

    private fun call(
        builder: Any,
        name: String,
        value: String,
    ) {
        // The public interface is used, the implementation class of the builder is not public.
        val builderType = Class.forName("com.pubnub.api.java.v2.PNConfiguration\$Builder")
        require(builderType.isInstance(builder)) { "${builder.javaClass.name} is not a PNConfiguration builder" }
        builderType.getMethod(name, String::class.java).invoke(builder, value)
    }
}
