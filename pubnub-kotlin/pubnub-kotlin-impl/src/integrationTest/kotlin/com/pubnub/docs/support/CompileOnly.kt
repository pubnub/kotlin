package com.pubnub.docs.support

/**
 * Marks a method whose snippet regions are compiled but not run (for example an Android only sample). The manifest lists
 * them as `compile-only` with this [value] as the reason. Use it rarely, every use is reviewed.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class CompileOnly(val value: String)
