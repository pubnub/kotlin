@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.pubnub.api.v2

import com.pubnub.api.UserId

expect interface PNConfiguration {
    val userId: UserId
    val subscribeKey: String
    val publishKey: String
    val secretKey: String
    val authKey: String
    val cryptoModule: CryptoModule?

}

expect interface CryptoModule

expect fun createPNConfiguration(
    userId: UserId,
    subscribeKey: String,
    publishKey: String,
    secretKey: String? = null,
    logVerbosity: Boolean = false,
): PNConfiguration