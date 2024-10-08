package com.pubnub.internal.v2.callbacks

import com.google.gson.JsonPrimitive
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.files.PNDownloadableFile
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNDeleteUUIDMetadataEventMessage
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.managers.AnnouncementCallback
import com.pubnub.internal.managers.AnnouncementEnvelope
import com.pubnub.internal.v2.PNConfigurationImpl
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EventEmitterImplTest {
    lateinit var emitterImpl: EventEmitterImpl
    val testPubNub = PubNubImpl(PNConfigurationImpl(UserId("aa")))
    val basePubSubResult = BasePubSubResult("a", null, null, null, null)
    val message = JsonPrimitive(1)

    @BeforeEach
    fun setUp() {
        emitterImpl = EventEmitterImpl(AnnouncementCallback.Phase.SUBSCRIPTION)
    }

    @Test
    fun `can add listener`() {
        val listener = object : EventListener { }

        emitterImpl.addListener(listener)

        assertTrue(emitterImpl.listeners.contains(listener))
    }

    @Test
    fun `can remove listener`() {
        val listener = object : EventListener { }
        emitterImpl.addListener(listener)
        assertTrue(emitterImpl.listeners.contains(listener))

        emitterImpl.removeListener(listener)

        assertFalse(emitterImpl.listeners.contains(listener))
    }

    @Test
    fun `can remove all listeners`() {
        emitterImpl.addListener(object : EventListener { })
        emitterImpl.addListener(object : EventListener { })
        emitterImpl.addListener(object : EventListener { })
        assertTrue(emitterImpl.listeners.size == 3)

        emitterImpl.removeAllListeners()

        assertTrue(emitterImpl.listeners.isEmpty())
    }

    @Test
    fun `message is delivered`() {
        var success = false
        emitterImpl.addListener(
            object : EventListener {
                override fun message(
                    pubnub: PubNub,
                    event: PNMessageResult,
                ) {
                    success = true
                }
            },
        )
        emitterImpl.message(testPubNub, PNMessageResult(basePubSubResult, message))
        assertTrue(success)
    }

    @Test
    fun `message announcement is delivered`() {
        var success = false
        emitterImpl.addListener(
            object : EventListener {
                override fun message(
                    pubnub: PubNub,
                    event: PNMessageResult,
                ) {
                    success = true
                }
            },
        )
        emitterImpl.message(testPubNub, AnnouncementEnvelope(PNMessageResult(basePubSubResult, message)))
        assertTrue(success)
    }

    @Test
    fun `presence event is delivered`() {
        var success = false
        emitterImpl.addListener(
            object : EventListener {
                override fun presence(
                    pubnub: PubNub,
                    event: PNPresenceEventResult,
                ) {
                    success = true
                }
            },
        )

        emitterImpl.presence(testPubNub, PNPresenceEventResult(channel = "a"))

        assertTrue(success)
    }

    @Test
    fun `presence announcement is delivered`() {
        var success = false
        emitterImpl.addListener(
            object : EventListener {
                override fun presence(
                    pubnub: PubNub,
                    event: PNPresenceEventResult,
                ) {
                    success = true
                }
            },
        )

        emitterImpl.presence(testPubNub, AnnouncementEnvelope(PNPresenceEventResult(channel = "a")))

        assertTrue(success)
    }

    @Test
    fun `signal is delivered`() {
        var success = false
        emitterImpl.addListener(
            object : EventListener {
                override fun signal(
                    pubnub: PubNub,
                    event: PNSignalResult,
                ) {
                    success = true
                }
            },
        )

        emitterImpl.signal(testPubNub, PNSignalResult(basePubSubResult, message))

        assertTrue(success)
    }

    @Test
    fun `signal announcement is delivered`() {
        var success = false
        emitterImpl.addListener(
            object : EventListener {
                override fun signal(
                    pubnub: PubNub,
                    event: PNSignalResult,
                ) {
                    success = true
                }
            },
        )

        emitterImpl.signal(testPubNub, AnnouncementEnvelope(PNSignalResult(basePubSubResult, message)))

        assertTrue(success)
    }

    @Test
    fun `messageAction is delivered`() {
        var success = false
        emitterImpl.addListener(
            object : EventListener {
                override fun messageAction(
                    pubnub: PubNub,
                    event: PNMessageActionResult,
                ) {
                    success = true
                }
            },
        )

        emitterImpl.messageAction(testPubNub, PNMessageActionResult(basePubSubResult, "a", PNMessageAction()))

        assertTrue(success)
    }

    @Test
    fun `messageAction announcement is delivered`() {
        var success = false
        emitterImpl.addListener(
            object : EventListener {
                override fun messageAction(
                    pubnub: PubNub,
                    event: PNMessageActionResult,
                ) {
                    success = true
                }
            },
        )

        emitterImpl.messageAction(testPubNub, AnnouncementEnvelope(PNMessageActionResult(basePubSubResult, "a", PNMessageAction())))

        assertTrue(success)
    }

    @Test
    fun `object is delivered`() {
        var success = false
        emitterImpl.addListener(
            object : EventListener {
                override fun objects(
                    pubnub: PubNub,
                    event: PNObjectEventResult,
                ) {
                    success = true
                }
            },
        )

        emitterImpl.objects(testPubNub, PNObjectEventResult(basePubSubResult, PNDeleteUUIDMetadataEventMessage("a", "b", "c", "d", "e")))

        assertTrue(success)
    }

    @Test
    fun `objects announcement is delivered`() {
        var success = false
        emitterImpl.addListener(
            object : EventListener {
                override fun objects(
                    pubnub: PubNub,
                    event: PNObjectEventResult,
                ) {
                    success = true
                }
            },
        )

        emitterImpl.objects(
            testPubNub,
            AnnouncementEnvelope(PNObjectEventResult(basePubSubResult, PNDeleteUUIDMetadataEventMessage("a", "b", "c", "d", "e"))),
        )

        assertTrue(success)
    }

    @Test
    fun `file is delivered`() {
        var success = false
        emitterImpl.addListener(
            object : EventListener {
                override fun file(
                    pubnub: PubNub,
                    event: PNFileEventResult,
                ) {
                    success = true
                }
            },
        )

        emitterImpl.file(testPubNub, PNFileEventResult("a", null, null, null, PNDownloadableFile("a", "b", "c"), message))

        assertTrue(success)
    }

    @Test
    fun `file announcement is delivered`() {
        var success = false
        emitterImpl.addListener(
            object : EventListener {
                override fun file(
                    pubnub: PubNub,
                    event: PNFileEventResult,
                ) {
                    success = true
                }
            },
        )

        emitterImpl.file(
            testPubNub,
            AnnouncementEnvelope(PNFileEventResult("a", null, null, null, PNDownloadableFile("a", "b", "c"), message)),
        )

        assertTrue(success)
    }

    @Test
    fun `when accepts is true then announce message`() {
        var success = false
        emitterImpl =
            EventEmitterImpl(AnnouncementCallback.Phase.SUBSCRIPTION) { envelope ->
                envelope.event.channel == "acceptedChannel"
            }
        emitterImpl.addListener(
            object : EventListener {
                override fun message(
                    pubnub: PubNub,
                    event: PNMessageResult,
                ) {
                    success = true
                }
            },
        )
        emitterImpl.message(
            testPubNub,
            AnnouncementEnvelope(PNMessageResult(BasePubSubResult("acceptedChannel", null, null, null, null), message)),
        )
        assertTrue(success)
    }

    @Test
    fun `when accepts is false then don't announce message`() {
        var success = false
        emitterImpl =
            EventEmitterImpl(AnnouncementCallback.Phase.SUBSCRIPTION) { envelope ->
                envelope.event.channel == "acceptedChannel"
            }
        emitterImpl.addListener(
            object : EventListener {
                override fun message(
                    pubnub: PubNub,
                    event: PNMessageResult,
                ) {
                    success = true
                }
            },
        )
        emitterImpl.message(
            testPubNub,
            AnnouncementEnvelope(PNMessageResult(BasePubSubResult("anotherChannel", null, null, null, null), message)),
        )
        assertFalse(success)
    }
}
