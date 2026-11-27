package com.pubnub.docs.support;

/**
 * Java side helpers of the messaging pages. The hidden line of a sample that builds its configuration in one chain
 * (`PNConfiguration.builder(...).publishKey("demo").build()`) cannot call {@code LiveKeys.apply}, so it swaps the keys with
 * these two calls. Kotlin's {@code Env} object has no static members, this class is the Java door to it.
 */
public final class MessagingJavaKit {
    private MessagingJavaKit() {}

    /** The subscribe key of the open role. */
    public static String subscribeKey() {
        return Env.INSTANCE.subscribeKey("open");
    }

    /** The publish key of the open role. */
    public static String publishKey() {
        return Env.INSTANCE.publishKey("open");
    }
}
