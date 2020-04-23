package com.pubnub.api.managers

import com.pubnub.api.BaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PublishSequenceManagerTest : BaseTest() {

    @Test
    fun testSequenceManager() {
        val publishSequenceManager = PublishSequenceManager(2)

        assertEquals(1, publishSequenceManager.nextSequence())
        assertEquals(2, publishSequenceManager.nextSequence())
        assertEquals(1, publishSequenceManager.nextSequence())
        assertEquals(2, publishSequenceManager.nextSequence())
    }

}