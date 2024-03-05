package com.pubnub.internal.v2.callbacks

import com.google.gson.JsonPrimitive
import com.pubnub.api.BasePubNub
import com.pubnub.api.models.consumer.files.PNDownloadableFile
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.internal.PNConfigurationCore
import com.pubnub.internal.TestPubNub
import com.pubnub.internal.managers.AnnouncementCallback
import com.pubnub.internal.managers.AnnouncementEnvelope
import com.pubnub.internal.models.consumer.pubsub.objects.PNDeleteUUIDMetadataEventMessage
import com.pubnub.internal.models.consumer.pubsub.objects.PNObjectEventResult
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EventEmitterImplTest {

    lateinit var emitterImpl: EventEmitterImpl
    val testPubNub = TestPubNub(PNConfigurationCore("aa"))
    val basePubSubResult = BasePubSubResult("a", null, null, null, null)
    val message = JsonPrimitive(1)

    @BeforeEach
    fun setUp() {
        emitterImpl = EventEmitterImpl(AnnouncementCallback.Phase.SUBSCRIPTION)
    }

    @Test
    fun addListener() {
        val listener = object : InternalEventListener { }

        emitterImpl.addListener(listener)

        assertTrue(emitterImpl.listeners.contains(listener))
    }

    @Test
    fun removeListener() {
        val listener = object : InternalEventListener { }
        emitterImpl.addListener(listener)
        assertTrue(emitterImpl.listeners.contains(listener))

        emitterImpl.removeListener(listener)

        assertFalse(emitterImpl.listeners.contains(listener))
    }

    @Test
    fun removeAllListeners() {
        emitterImpl.addListener(object : InternalEventListener { })
        emitterImpl.addListener(object : InternalEventListener { })
        emitterImpl.addListener(object : InternalEventListener { })
        assertTrue(emitterImpl.listeners.size == 3)

        emitterImpl.removeAllListeners()

        assertTrue(emitterImpl.listeners.isEmpty())
    }

    @Test
    fun message() {
        var success = false
        emitterImpl.addListener(object : InternalEventListener {
            override fun message(pubnub: BasePubNub<*, *, *, *, *, *, *, *>, event: PNMessageResult) {
                success = true
            }
        })
        emitterImpl.message(testPubNub, PNMessageResult(basePubSubResult, message))
        assertTrue(success)
    }

    @Test
    fun messageAnnounce() {
        var success = false
        emitterImpl.addListener(object : InternalEventListener {
            override fun message(pubnub: BasePubNub<*, *, *, *, *, *, *, *>, event: PNMessageResult) {
                success = true
            }
        })
        emitterImpl.message(testPubNub, AnnouncementEnvelope(PNMessageResult(basePubSubResult, message)))
        assertTrue(success)
    }

    @Test
    fun presence() {
        var success = false
        emitterImpl.addListener(object : InternalEventListener {
            override fun presence(pubnub: BasePubNub<*, *, *, *, *, *, *, *>, event: PNPresenceEventResult) {
                success = true
            }
        })

        emitterImpl.presence(testPubNub, PNPresenceEventResult(channel = "a"))

        assertTrue(success)
    }

    @Test
    fun presenceAnnounce() {
        var success = false
        emitterImpl.addListener(object : InternalEventListener {
            override fun presence(pubnub: BasePubNub<*, *, *, *, *, *, *, *>, event: PNPresenceEventResult) {
                success = true
            }
        })

        emitterImpl.presence(testPubNub, AnnouncementEnvelope(PNPresenceEventResult(channel = "a")))

        assertTrue(success)
    }

    @Test
    fun signal() {
        var success = false
        emitterImpl.addListener(object : InternalEventListener {
            override fun signal(pubnub: BasePubNub<*, *, *, *, *, *, *, *>, event: PNSignalResult) {
                success = true
            }
        })

        emitterImpl.signal(testPubNub, PNSignalResult(basePubSubResult, message))

        assertTrue(success)
    }

    @Test
    fun signalAnnounce() {
        var success = false
        emitterImpl.addListener(object : InternalEventListener {
            override fun signal(pubnub: BasePubNub<*, *, *, *, *, *, *, *>, event: PNSignalResult) {
                success = true
            }
        })

        emitterImpl.signal(testPubNub, AnnouncementEnvelope(PNSignalResult(basePubSubResult, message)))

        assertTrue(success)
    }

    @Test
    fun messageAction() {
        var success = false
        emitterImpl.addListener(object : InternalEventListener {
            override fun messageAction(pubnub: BasePubNub<*, *, *, *, *, *, *, *>, event: PNMessageActionResult) {
                success = true
            }
        })

        emitterImpl.messageAction(testPubNub, PNMessageActionResult(basePubSubResult, "a", PNMessageAction()))

        assertTrue(success)
    }

    @Test
    fun messageActionAnnounce() {
        var success = false
        emitterImpl.addListener(object : InternalEventListener {
            override fun messageAction(pubnub: BasePubNub<*, *, *, *, *, *, *, *>, event: PNMessageActionResult) {
                success = true
            }
        })

        emitterImpl.messageAction(testPubNub, AnnouncementEnvelope(PNMessageActionResult(basePubSubResult, "a", PNMessageAction())))

        assertTrue(success)
    }

    @Test
    fun objects() {
        var success = false
        emitterImpl.addListener(object : InternalEventListener {
            override fun objects(pubnub: BasePubNub<*, *, *, *, *, *, *, *>, event: PNObjectEventResult) {
                success = true
            }
        })

        emitterImpl.objects(testPubNub, PNObjectEventResult(basePubSubResult, PNDeleteUUIDMetadataEventMessage("a", "b", "c", "d", "e")))

        assertTrue(success)
    }
    @Test
    fun objectsAnnounce() {
        var success = false
        emitterImpl.addListener(object : InternalEventListener {
            override fun objects(pubnub: BasePubNub<*, *, *, *, *, *, *, *>, event: PNObjectEventResult) {
                success = true
            }
        })

        emitterImpl.objects(testPubNub, AnnouncementEnvelope(PNObjectEventResult(basePubSubResult, PNDeleteUUIDMetadataEventMessage("a", "b", "c", "d", "e"))))

        assertTrue(success)
    }

    @Test
    fun file() {
        var success = false
        emitterImpl.addListener(object : InternalEventListener {
            override fun file(pubnub: BasePubNub<*, *, *, *, *, *, *, *>, event: PNFileEventResult) {
                success = true
            }
        })

        emitterImpl.file(testPubNub, PNFileEventResult("a", null, null, null, PNDownloadableFile("a", "b", "c"), message))

        assertTrue(success)
    }

    @Test
    fun fileAnnounce() {
        var success = false
        emitterImpl.addListener(object : InternalEventListener {
            override fun file(pubnub: BasePubNub<*, *, *, *, *, *, *, *>, event: PNFileEventResult) {
                success = true
            }
        })

        emitterImpl.file(testPubNub, AnnouncementEnvelope(PNFileEventResult("a", null, null, null, PNDownloadableFile("a", "b", "c"), message)))

        assertTrue(success)
    }


    @Test
    fun `when accepts is true then announce message`() {
        var success = false
        emitterImpl = EventEmitterImpl(AnnouncementCallback.Phase.SUBSCRIPTION) { envelope ->
            envelope.event.channel == "acceptedChannel"
        }
        emitterImpl.addListener(object : InternalEventListener {
            override fun message(pubnub: BasePubNub<*, *, *, *, *, *, *, *>, event: PNMessageResult) {
                success = true
            }
        })
        emitterImpl.message(testPubNub, AnnouncementEnvelope(PNMessageResult(BasePubSubResult("acceptedChannel", null, null, null, null), message)))
        assertTrue(success)
    }

    @Test
    fun `when accepts is false then don't announce message`() {
        var success = false
        emitterImpl = EventEmitterImpl(AnnouncementCallback.Phase.SUBSCRIPTION) { envelope ->
            envelope.event.channel == "acceptedChannel"
        }
        emitterImpl.addListener(object : InternalEventListener {
            override fun message(pubnub: BasePubNub<*, *, *, *, *, *, *, *>, event: PNMessageResult) {
                success = true
            }
        })
        emitterImpl.message(testPubNub, AnnouncementEnvelope(PNMessageResult(BasePubSubResult("anotherChannel", null, null, null, null), message)))
        assertFalse(success)
    }


}