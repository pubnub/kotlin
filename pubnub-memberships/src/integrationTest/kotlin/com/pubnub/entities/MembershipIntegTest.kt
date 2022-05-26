package com.pubnub.entities

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MembershipIntegTest {
    private lateinit var pubnub: PubNub

    @BeforeEach
    fun setUp() {
        val config = PNConfiguration("kotlin").apply {
            subscribeKey = IntegTestConf.subscribeKey
            publishKey = IntegTestConf.publishKey
        }
        pubnub = PubNub(config)

        //remove membership
    }

    @Test
    internal fun can_addMembershipOfUser() {
        println("toDo")
    }

}