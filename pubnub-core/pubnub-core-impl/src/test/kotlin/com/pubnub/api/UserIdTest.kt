package com.pubnub.api

import org.junit.Test

class UserIdTest {
    @Test(expected = PubNubException::class)
    fun setUserIdToEmptyString() {
        UserId("")
    }
}
