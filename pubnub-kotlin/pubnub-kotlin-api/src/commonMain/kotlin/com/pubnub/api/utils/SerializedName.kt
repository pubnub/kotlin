@file:OptIn(ExperimentalMultiplatform::class)

package com.pubnub.api.utils

@OptionalExpectation
@Target(AnnotationTarget.FIELD)
expect annotation class SerializedName(val value: String, val alternate: Array<String> = [])
