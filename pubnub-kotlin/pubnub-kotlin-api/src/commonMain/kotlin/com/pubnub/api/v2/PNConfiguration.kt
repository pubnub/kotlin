@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.pubnub.api.v2

import com.pubnub.api.UserId

expect interface PNConfiguration {
    val userId: UserId
}