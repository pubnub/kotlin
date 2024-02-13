package com.pubnub.api.legacy.managers

import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.managers.PublishSequenceManager
import org.junit.Assert.assertEquals
import org.junit.Test

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
