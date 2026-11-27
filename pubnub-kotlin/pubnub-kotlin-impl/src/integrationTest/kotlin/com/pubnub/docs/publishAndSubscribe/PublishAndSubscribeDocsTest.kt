@file:Suppress("ktlint")

package com.pubnub.docs.publishAndSubscribe

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.callbacks.StatusListener
import com.pubnub.api.v2.subscriptions.SubscriptionCursor
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.docs.support.Cleanup
import com.pubnub.docs.support.Kit.armMessage
import com.pubnub.docs.support.Kit.armPresence
import com.pubnub.docs.support.Kit.armPresenceEvent
import com.pubnub.docs.support.Kit.armSignal
import com.pubnub.docs.support.Kit.assertNotStored
import com.pubnub.docs.support.Kit.assertNothingArrives
import com.pubnub.docs.support.Kit.assertNoStoredText
import com.pubnub.docs.support.Kit.assertNothingHandled
import com.pubnub.docs.support.Kit.assertTimetoken
import com.pubnub.docs.support.Kit.awaitNoOccupants
import com.pubnub.docs.support.Kit.createGroup
import com.pubnub.docs.support.Kit.joinCrowd
import com.pubnub.docs.support.Kit.leaveAs
import com.pubnub.docs.support.Kit.probe
import com.pubnub.docs.support.Kit.publishFrom
import com.pubnub.docs.support.Kit.storedItem
import com.pubnub.docs.support.Kit.subscribeAndConnect
import com.pubnub.docs.support.LiveClients
import com.pubnub.docs.support.LiveExtension
import com.pubnub.docs.support.Recorder
import com.pubnub.docs.support.Retry
import com.pubnub.docs.support.Stdout
import com.pubnub.docs.support.Wait
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.ByteArrayInputStream
import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger

/**
 * Live tests of the Kotlin docs page publish-and-subscribe (docs/sdks/kotlin/api-reference/publish-and-subscribe.md).
 * One test per snippet. The text between a start marker and `snippet.end` is the published sample, the arrangement is
 * above it and the assertions below it. The DataSync listener samples of the page live with the DataSync tests.
 */
@ExtendWith(LiveExtension::class)
class PublishAndSubscribeDocsTest {
    // ---------------------------------------------------------------------------------------------------------------
    // Publish
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun publishMain(clients: LiveClients) {
        val receiver = clients.kotlinClient(OPEN, "docs-receiver")
        val simple = armMessage(receiver, "demo-channel", "Hello from PubNub Kotlin SDK!")
        val json = armMessage(receiver, "demo-channel", "Message from Kotlin")
        subscribeAndConnect(receiver, listOf("demo-channel"))
        Cleanup.record("history", "demo-channel")
        val out = Stdout.captureForTest()

        com.pubnub.docs.publishAndSubscribe.samples.main()

        val first = simple.await() as PNMessageResult
        assertEquals("Hello from PubNub Kotlin SDK!", first.message.asString)
        assertEquals("publish-demo-user", first.publisher)
        assertTimetoken(first.timetoken)
        out.assertPrinted("Timetoken: ${first.timetoken}")
        val second = json.await() as PNMessageResult
        val body = second.message.asJsonObject
        assertEquals("Message from Kotlin", body["title"].asString)
        assertEquals("This is a JSON message", body["content"].asString)
        assertEquals("publish-demo-user", body["sender"].asString)
        assertTrue(body["timestamp"].asLong > 0, "the timestamp of the JSON message is not a number")
        assertTimetoken(second.timetoken)
        out.assertPrinted("Timetoken: ${second.timetoken}")
        storedItem(receiver, "demo-channel", first.timetoken!!)
        storedItem(receiver, "demo-channel", second.timetoken!!)
    }

    @Test
    fun publishRequestExecution(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val receiver = clients.kotlinClient(OPEN, "docs-receiver")
        val arrived = armMessage(receiver, "channelName", "This SDK rules!")
        subscribeAndConnect(receiver, listOf("channelName"))
        Cleanup.record("history", "channelName")

        // snippet.publishRequestExecution
        val channel = pubnub.channel("channelName")

        channel.publish("This SDK rules!").async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end

        val published = recorder.awaitOk("publish") as PNPublishResult
        val message = arrived.await() as PNMessageResult
        assertEquals("This SDK rules!", message.message.asString)
        assertEquals("myUserId", message.publisher)
        assertEquals(published.timetoken, message.timetoken)
        assertTimetoken(message.timetoken)
        storedItem(receiver, "channelName", published.timetoken)
    }

    @Test
    fun publishWithMetadata(clients: LiveClients) {
        val receiver = clients.kotlinClient(OPEN, "docs-receiver")
        val arrived = armMessage(receiver, "myChannel", "Hello, world")
        subscribeAndConnect(receiver, listOf("myChannel"))
        Cleanup.record("history", "myChannel")

        // origin: pubnub-kotlin/pubnub-kotlin-docs/src/main/kotlin/com/pubnub/docs/publishAndSubscribe/publish/PublishOthers.kt#publishWithMetadata
        // snippet.publishWithMetadata
        val configBuilder = com.pubnub.api.v2.PNConfiguration.builder(UserId("myUserId"), "demo").apply {
            publishKey = "demo"
            // snippet.hide
            com.pubnub.docs.support.LiveKeys.apply(this, "open")
            // snippet.show
        }
        val pubnub = PubNub.create(configBuilder.build())

        val channel = pubnub.channel("myChannel")

        val myMessage = JsonObject().apply {
            addProperty("text", "Hello, world")
        }

        channel.publish(
            message = myMessage,
            meta = mapOf("lang" to "en"),
            usePost = true,
            customMessageType = "text-message"
        ).async { result ->
            result.onFailure { exception ->
                println("Error while publishing")
                exception.printStackTrace()
            }.onSuccess { publishResult ->
                println("Message sent, timetoken: ${publishResult.timetoken}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        val message = arrived.await() as PNMessageResult
        assertEquals("Hello, world", message.message.asJsonObject["text"].asString)
        assertEquals("myUserId", message.publisher)
        assertEquals("text-message", message.customMessageType)
        assertEquals("""{"lang":"en"}""", message.userMetadata.toString())
        assertTimetoken(message.timetoken)
        val stored = storedItem(receiver, "myChannel", message.timetoken!!)
        assertEquals("""{"lang":"en"}""", stored.meta.toString())
        assertEquals("text-message", stored.customMessageType)
    }

    @Test
    fun publishJsonArrayGSON(clients: LiveClients) {
        val receiver = clients.kotlinClient(OPEN, "docs-receiver")
        val arrived = armMessage(receiver, "myChannel", "[32,35]")
        subscribeAndConnect(receiver, listOf("myChannel"))
        Cleanup.record("history", "myChannel")

        // snippet.publishJsonArrayGSON
        val configBuilder = com.pubnub.api.v2.PNConfiguration.builder(UserId("myUserId"), "demo").apply {
            publishKey = "demo"
            // snippet.hide
            com.pubnub.docs.support.LiveKeys.apply(this, "open")
            // snippet.show
        }
        val pubnub = PubNub.create(configBuilder.build())

        val channel = pubnub.channel("myChannel")

        val myMessage = JsonArray().apply {
            add(32L)
            add(35L)
        }

        channel.publish(
            message = myMessage,
            customMessageType = "text-message"
        ).async { result ->
            result.onFailure { exception ->
                println("Error while publishing")
                exception.printStackTrace()
            }.onSuccess { value ->
                println("Message sent, timetoken: ${value.timetoken}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        val message = arrived.await() as PNMessageResult
        assertEquals(listOf(32L, 35L), message.message.asJsonArray.map { it.asLong })
        assertEquals("myUserId", message.publisher)
        assertEquals("text-message", message.customMessageType)
        assertTimetoken(message.timetoken)
        storedItem(receiver, "myChannel", message.timetoken!!)
    }

    @Test
    fun publishJsonObjectOrgJson(clients: LiveClients) {
        val receiver = clients.kotlinClient(OPEN, "docs-receiver")
        val arrived = armMessage(receiver, "myChannel", "lat")
        subscribeAndConnect(receiver, listOf("myChannel"))
        Cleanup.record("history", "myChannel")

        // snippet.publishJsonObjectOrgJson
        val configBuilder = com.pubnub.api.v2.PNConfiguration.builder(UserId("myUserId"), "demo").apply {
            publishKey = "demo"
            // snippet.hide
            com.pubnub.docs.support.LiveKeys.apply(this, "open")
            // snippet.show
        }
        val pubnub = PubNub.create(configBuilder.build())

        val channel = pubnub.channel("myChannel")

        val myMessage = JSONObject().apply {
            put("lat", 32L)
            put("lng", 32L)
        }

        channel.publish(
            message = myMessage,
            customMessageType = "text-message"
        ).async { result ->
            result.onFailure { exception ->
                println("Error while publishing")
                exception.printStackTrace()
            }.onSuccess { value ->
                println("Message sent, timetoken: ${value.timetoken}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        val message = arrived.await() as PNMessageResult
        val body = message.message.asJsonObject
        assertEquals(32L, body["lat"].asLong)
        assertEquals(32L, body["lng"].asLong)
        assertEquals("text-message", message.customMessageType)
        assertEquals("myUserId", message.publisher)
        assertTimetoken(message.timetoken)
        storedItem(receiver, "myChannel", message.timetoken!!)
    }

    @Test
    fun publishJsonArrayOrgJson(clients: LiveClients) {
        val receiver = clients.kotlinClient(OPEN, "docs-receiver")
        val arrived = armMessage(receiver, "myChannel", "[32,33]")
        subscribeAndConnect(receiver, listOf("myChannel"))
        Cleanup.record("history", "myChannel")

        // snippet.publishJsonArrayOrgJson
        val configBuilder = com.pubnub.api.v2.PNConfiguration.builder(UserId("myUserId"), "demo").apply {
            publishKey = "demo"
            // snippet.hide
            com.pubnub.docs.support.LiveKeys.apply(this, "open")
            // snippet.show
        }

        val pubnub = PubNub.create(configBuilder.build())

        val channel = pubnub.channel("myChannel")

        val myMessage = JSONArray().apply {
            put(32L)
            put(33L)
        }

        channel.publish(
            message = myMessage,
            customMessageType = "text-message"
        ).async { result ->
            result.onFailure { exception ->
                println("Error while publishing")
                exception.printStackTrace()
            }.onSuccess { value ->
                println("Message sent, timetoken: ${value.timetoken}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        val message = arrived.await() as PNMessageResult
        assertEquals(listOf(32L, 33L), message.message.asJsonArray.map { it.asLong })
        assertEquals("text-message", message.customMessageType)
        assertEquals("myUserId", message.publisher)
        assertTimetoken(message.timetoken)
        storedItem(receiver, "myChannel", message.timetoken!!)
    }

    @Test
    fun publishAndStoreMessage10Hours(clients: LiveClients) {
        val receiver = clients.kotlinClient(OPEN, "docs-receiver")
        val arrived = armMessage(receiver, "myChannel", "Hello, world")
        subscribeAndConnect(receiver, listOf("myChannel"))
        Cleanup.record("history", "myChannel")

        // snippet.publishAndStoreMessage10Hours
        val configBuilder = com.pubnub.api.v2.PNConfiguration.builder(UserId("myUserId"), "demo").apply {
            publishKey = "demo"
            // snippet.hide
            com.pubnub.docs.support.LiveKeys.apply(this, "open")
            // snippet.show
        }

        val pubnub = PubNub.create(configBuilder.build())

        val channel = pubnub.channel("myChannel")

        val myMessage = JsonObject().apply {
            addProperty("text", "Hello, world")
        }

        channel.publish(
            message = myMessage,
            shouldStore = true,
            ttl = 10,
            customMessageType = "text-message"
        ).async { result ->
            result.onFailure { exception ->
                println("Error while publishing")
                exception.printStackTrace()
            }.onSuccess { value ->
                println("Message sent, timetoken: ${value.timetoken}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        val message = arrived.await() as PNMessageResult
        assertEquals("Hello, world", message.message.asJsonObject["text"].asString)
        assertEquals("text-message", message.customMessageType)
        assertTimetoken(message.timetoken)
        val stored = storedItem(receiver, "myChannel", message.timetoken!!)
        assertEquals("Hello, world", stored.message.asJsonObject["text"].asString)
    }

    @Test
    fun publishMessageToChannel(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val receiver = clients.kotlinClient(OPEN, "docs-receiver")
        val arrived = armMessage(receiver, "my_channel", "lat")
        subscribeAndConnect(receiver, listOf("my_channel"))
        Cleanup.record("history", "my_channel")
        val out = Stdout.captureForTest()

        // snippet.publishMessageToChannel
        pubnub.publish(
            message = JsonObject().apply {
                addProperty("lat", 32L)
                addProperty("lng", 32L)
            },
            channel = "my_channel"
        ).async { result ->
            result.onSuccess { value ->
                println("Publish timetoken ${value.timetoken}")
            }
        }
        // snippet.end

        val published = recorder.awaitOk("publish") as PNPublishResult
        val message = arrived.await() as PNMessageResult
        assertEquals(32L, message.message.asJsonObject["lat"].asLong)
        assertEquals(32L, message.message.asJsonObject["lng"].asLong)
        assertEquals(published.timetoken, message.timetoken)
        assertEquals("myUserId", message.publisher)
        assertTimetoken(published.timetoken)
        Wait.eventually("the sample printed the timetoken") { out.assertPrinted("Publish timetoken ${published.timetoken}") }
        storedItem(receiver, "my_channel", published.timetoken)
    }

    @Test
    fun publishWithMetadataOld(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val receiver = clients.kotlinClient(OPEN, "docs-receiver")
        val arrived = armMessage(receiver, "my_channel", "there")
        subscribeAndConnect(receiver, listOf("my_channel"))
        Cleanup.record("history", "my_channel")

        // origin: pubnub-kotlin/pubnub-kotlin-docs/src/main/kotlin/com/pubnub/docs/publishAndSubscribe/publish/PublishOld.kt#publishWithMetadata
        // snippet.publishWithMetadataOld
        pubnub.publish(
            message = mapOf("hello" to "there"),
            channel = "my_channel",
            shouldStore = true,
            meta = mapOf("lang" to "en"),
            usePost = true
        ).async { result ->
            // check result
        }
        // snippet.end

        val published = recorder.awaitOk("publish") as PNPublishResult
        val message = arrived.await() as PNMessageResult
        assertEquals("there", message.message.asJsonObject["hello"].asString)
        assertEquals("""{"lang":"en"}""", message.userMetadata.toString())
        assertEquals(published.timetoken, message.timetoken)
        val stored = storedItem(receiver, "my_channel", published.timetoken)
        assertEquals("""{"lang":"en"}""", stored.meta.toString())
    }

    @Test
    fun publishJsonObjectGson(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val receiver = clients.kotlinClient(OPEN, "docs-receiver")
        val arrived = armMessage(receiver, "my_channel", "lat")
        subscribeAndConnect(receiver, listOf("my_channel"))
        Cleanup.record("history", "my_channel")

        // snippet.publishJsonObjectGson
        pubnub.publish(
            message = JsonObject().apply {
                addProperty("lat", 32L)
                addProperty("lng", 32L)
            },
            channel = "my_channel"
        ).async { result ->
            // check result
        }
        // snippet.end

        val published = recorder.awaitOk("publish") as PNPublishResult
        val message = arrived.await() as PNMessageResult
        assertEquals(32L, message.message.asJsonObject["lat"].asLong)
        assertEquals(32L, message.message.asJsonObject["lng"].asLong)
        assertEquals(published.timetoken, message.timetoken)
        storedItem(receiver, "my_channel", published.timetoken)
    }

    @Test
    fun publishJsonArrayGson(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val receiver = clients.kotlinClient(OPEN, "docs-receiver")
        val arrived = armMessage(receiver, "my_channel", "[32,35]")
        subscribeAndConnect(receiver, listOf("my_channel"))
        Cleanup.record("history", "my_channel")

        // snippet.publishJsonArrayGson
        pubnub.publish(
            message = JsonArray().apply {
                add(32L)
                add(35L)
            },
            channel = "my_channel"
        ).async { result ->
            // check result
        }
        // snippet.end

        val published = recorder.awaitOk("publish") as PNPublishResult
        val message = arrived.await() as PNMessageResult
        assertEquals(listOf(32L, 35L), message.message.asJsonArray.map { it.asLong })
        assertEquals(published.timetoken, message.timetoken)
        storedItem(receiver, "my_channel", published.timetoken)
    }

    @Test
    fun publishJsonObjectJsonOrg(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val receiver = clients.kotlinClient(OPEN, "docs-receiver")
        val arrived = armMessage(receiver, "my_channel", "lat")
        subscribeAndConnect(receiver, listOf("my_channel"))
        Cleanup.record("history", "my_channel")

        // snippet.publishJsonObjectJsonOrg
        pubnub.publish(
            message = JSONObject().apply {
                put("lat", 32L)
                put("lng", 32L)
            },
            channel = "my_channel"
        ).async { result ->
            // check result
        }
        // snippet.end

        val published = recorder.awaitOk("publish") as PNPublishResult
        val message = arrived.await() as PNMessageResult
        assertEquals(32L, message.message.asJsonObject["lat"].asLong)
        assertEquals(32L, message.message.asJsonObject["lng"].asLong)
        assertEquals(published.timetoken, message.timetoken)
        storedItem(receiver, "my_channel", published.timetoken)
    }

    @Test
    fun publishJsonArrayJsonOrg(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val receiver = clients.kotlinClient(OPEN, "docs-receiver")
        val arrived = armMessage(receiver, "my_channel", "[32,33]")
        subscribeAndConnect(receiver, listOf("my_channel"))
        Cleanup.record("history", "my_channel")

        // snippet.publishJsonArrayJsonOrg
        pubnub.publish(
            message = JSONArray().apply {
                put(32L)
                put(33L)
            },
            channel = "my_channel"
        ).async { result ->
            // check result
        }
        // snippet.end

        val published = recorder.awaitOk("publish") as PNPublishResult
        val message = arrived.await() as PNMessageResult
        assertEquals(listOf(32L, 33L), message.message.asJsonArray.map { it.asLong })
        assertEquals(published.timetoken, message.timetoken)
        storedItem(receiver, "my_channel", published.timetoken)
    }

    @Test
    fun publishAndStoreMessagesFor10Hours(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val receiver = clients.kotlinClient(OPEN, "docs-receiver")
        val arrived = armMessage(receiver, "my_channel", "test")
        subscribeAndConnect(receiver, listOf("my_channel"))
        Cleanup.record("history", "my_channel")

        // snippet.publishAndStoreMessagesFor10Hours
        pubnub.publish(
            message = "test",
            channel = "my_channel",
            shouldStore = true,
            ttl = 10
        ).async { result -> // check result }
            // snippet.end
        }

        val published = recorder.awaitOk("publish") as PNPublishResult
        val message = arrived.await() as PNMessageResult
        assertEquals("test", message.message.asString)
        assertEquals(published.timetoken, message.timetoken)
        storedItem(receiver, "my_channel", published.timetoken)
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Fire and signal
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun fireBasic(clients: LiveClients) {
        val receiver = clients.kotlinClient(OPEN, "docs-receiver")
        val arrived = armMessage(receiver, "myChannel", "Hello, world")
        subscribeAndConnect(receiver, listOf("myChannel"))
        Cleanup.record("history", "myChannel")
        val out = Stdout.captureForTest()

        // snippet.fireBasic
        val configBuilder = com.pubnub.api.v2.PNConfiguration.builder(UserId("myUserId"), "demo").apply {
            publishKey = "demo"
            // snippet.hide
            com.pubnub.docs.support.LiveKeys.apply(this, "open")
            // snippet.show
        }

        val pubnub = PubNub.create(configBuilder.build())
        val channel = pubnub.channel("myChannel")
        val myMessage = JsonObject().apply {
            addProperty("text", "Hello, world")
        }

        channel.fire(myMessage).async { result ->
            result.onFailure { exception ->
                println("Error while publishing")
                exception.printStackTrace()
            }.onSuccess { fireResult ->
                println("Message sent, timetoken: ${fireResult.timetoken}")
            }
        }

        // snippet.end
        clients.track(pubnub)

        // A fired message goes to Functions only: no subscriber gets it and history does not keep it. A normal publish
        // that arrives afterwards proves that the channel delivers.
        Wait.eventually("the sample reported the fire") { out.assertPrinted("Message sent, timetoken: ") }
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        val controlText = probe("fire-control")
        val control = armMessage(receiver, "myChannel", controlText)
        publishFrom(friend, "myChannel", controlText, shouldStore = false)
        control.await()
        assertNothingArrives(arrived, "a fired message reached a subscriber")
        assertNoStoredText(receiver, "myChannel", "Hello, world")
    }

    @Test
    fun fireMessageToChannel(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val receiver = clients.kotlinClient(OPEN, "docs-receiver")
        val arrived = armMessage(receiver, "my_channel", "hello")
        subscribeAndConnect(receiver, listOf("my_channel"))
        Cleanup.record("history", "my_channel")

        // snippet.fireMessageToChannel
        pubnub.fire(
            message = listOf("hello", "there"),
            channel = "my_channel"
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end

        val fired = recorder.awaitOk("fire") as PNPublishResult
        assertTimetoken(fired.timetoken)
        // A fired message goes to Functions only: no subscriber gets it and history does not keep it. A normal publish
        // that arrives afterwards proves that the channel delivers.
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        val controlText = probe("fire-control")
        val control = armMessage(receiver, "my_channel", controlText)
        publishFrom(friend, "my_channel", controlText, shouldStore = false)
        control.await()
        assertNothingArrives(arrived, "a fired message reached a subscriber")
        assertNotStored(receiver, "my_channel", fired.timetoken)
    }

    @Test
    fun signalBasic(clients: LiveClients) {
        val receiver = clients.kotlinClient(OPEN, "docs-receiver")
        val arrived = armSignal(receiver, "myChannel", "This is a signal!")
        subscribeAndConnect(receiver, listOf("myChannel"))
        Cleanup.record("history", "myChannel")

        // snippet.signalBasic
        val configBuilder = com.pubnub.api.v2.PNConfiguration.builder(UserId("myUserId"), "demo").apply {
            publishKey = "demo"
            // snippet.hide
            com.pubnub.docs.support.LiveKeys.apply(this, "open")
            // snippet.show
        }
        val pubnub = PubNub.create(configBuilder.build())
        val channel = pubnub.channel("myChannel")

        channel.signal(
            message = "This is a signal!",
            customMessageType = "text-message"
        ).async { result ->
            result.onFailure { exception ->
                println("Error while publishing")
                exception.printStackTrace()
            }.onSuccess { signalResult ->
                println("Signal sent, timetoken: ${signalResult.timetoken}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        val signal = arrived.await() as PNSignalResult
        assertEquals("This is a signal!", signal.message.asString)
        assertEquals("myUserId", signal.publisher)
        assertEquals("text-message", signal.customMessageType)
        assertTimetoken(signal.timetoken)
        assertNotStored(receiver, "myChannel", signal.timetoken!!)
    }

    @Test
    fun signalMessageToChannel(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val receiver = clients.kotlinClient(OPEN, "docs-receiver")
        val arrived = armSignal(receiver, "my_channel", "hello")
        subscribeAndConnect(receiver, listOf("my_channel"))
        Cleanup.record("history", "my_channel")

        // snippet.signalMessageToChannel
        pubnub.signal(
            message = "hello",
            channel = "my_channel"
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end

        val sent = recorder.awaitOk("signal") as PNPublishResult
        val signal = arrived.await() as PNSignalResult
        assertEquals("hello", signal.message.asString)
        assertEquals("myUserId", signal.publisher)
        assertEquals(sent.timetoken, signal.timetoken)
        assertNotStored(receiver, "my_channel", sent.timetoken)
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Subscribe
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun subscribe(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        val text = probe("subscribe")
        val connected = Wait.connected(client, listOf("my_channel"))
        val arrived = armMessage(client, "my_channel", text)

        // snippet.subscribe
        pubnub.subscribe(
            channels = listOf("my_channel")
        )

        // snippet.end

        val status = connected.await() as PNStatus
        assertEquals(PNStatusCategory.PNConnectedCategory, status.category)
        assertEquals(listOf("my_channel"), client.getSubscribedChannels())
        val sent = publishFrom(friend, "my_channel", text, shouldStore = false)
        val message = arrived.await() as PNMessageResult
        assertEquals(text, message.message.asString)
        assertEquals("docs-joiner", message.publisher)
        assertEquals(sent.timetoken, message.timetoken)
        assertTimetoken(message.timetoken)
    }

    @Test
    fun subscribeWithLogging(clients: LiveClients) {
        val watcher = clients.kotlinClient(OPEN, "docs-receiver")
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        val text = probe("subscribe-with-logging")
        awaitNoOccupants(watcher, "my_channel")
        subscribeAndConnect(watcher, listOf("my_channel"), presence = true)
        val joined = armPresence(watcher, "join", "myUserId", "my_channel")

        // snippet.subscribeWithLogging
        val pnConfiguration = com.pubnub.api.v2.PNConfiguration.builder(UserId("myUserId"), "demo").apply {
            publishKey = "my_pubkey"
            // snippet.hide
            com.pubnub.docs.support.LiveKeys.apply(this, "open")
            // snippet.show
        }

        val pubnub = PubNub.create(pnConfiguration.build())

        pubnub.subscribe(
            channels = listOf("my_channel")
        )
        // snippet.end
        clients.track(pubnub)

        val join = joined.await(Wait.PRESENCE_EVENT_TIMEOUT_MS) as PNPresenceEventResult
        assertEquals("myUserId", join.uuid)
        assertEquals(listOf("my_channel"), pubnub.getSubscribedChannels())
        val arrived = armMessage(pubnub, "my_channel", text)
        val sent = publishFrom(friend, "my_channel", text, shouldStore = false)
        val message = arrived.await() as PNMessageResult
        assertEquals(sent.timetoken, message.timetoken)
        assertEquals("docs-joiner", message.publisher)
    }

    @Test
    fun subscribeWithMultipleChannels(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        val textOne = probe("multiple-one")
        val textTwo = probe("multiple-two")
        val connected = Wait.connected(client, listOf("ch1", "ch2"))
        val arrivedOne = armMessage(client, "ch1", textOne)
        val arrivedTwo = armMessage(client, "ch2", textTwo)

        // snippet.subscribeWithMultipleChannels
        pubnub.subscribe(
            channels = listOf("ch1", "ch2") // subscribe to channels information
        )
        // snippet.end

        connected.await()
        assertEquals(setOf("ch1", "ch2"), client.getSubscribedChannels().toSet())
        publishFrom(friend, "ch1", textOne, shouldStore = false)
        publishFrom(friend, "ch2", textTwo, shouldStore = false)
        assertEquals("ch1", (arrivedOne.await() as PNMessageResult).channel)
        assertEquals("ch2", (arrivedTwo.await() as PNMessageResult).channel)
    }

    @Test
    fun subscribeToPresenceChannel(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        awaitNoOccupants(client, "my_channel")
        val connected = Wait.connected(client, listOf("my_channel"))
        val joined = armPresence(client, "join", "docs-joiner", "my_channel")

        // snippet.subscribeToPresenceChannel
        pubnub.subscribe(
            channels = listOf("my_channel"), // subscribe to channels
            withPresence = true // also subscribe to related presence information
        )
        // snippet.end

        connected.await()
        subscribeAndConnect(friend, listOf("my_channel"))
        val join = joined.await(Wait.PRESENCE_EVENT_TIMEOUT_MS) as PNPresenceEventResult
        assertEquals("my_channel", join.channel)
        assertEquals(2, join.occupancy)
        assertNotNull(join.timestamp)
    }

    @Test
    fun joinEvent(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        awaitNoOccupants(client, "my_channel")
        subscribeAndConnect(client, listOf("my_channel"), presence = true)
        val joined = armPresence(client, "join", "docs-joiner", "my_channel")

        // snippet.joinEvent
        pubnub.addListener(object : EventListener {
            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                if (pnPresenceEventResult.event == "join") {
                    pnPresenceEventResult.uuid // 175c2c67-b2a9-470d-8f4b-1db94f90e39e
                    pnPresenceEventResult.timestamp // 1345546797
                    pnPresenceEventResult.occupancy // # users in channel
                }
            }
        })
        // snippet.end

        subscribeAndConnect(friend, listOf("my_channel"))
        val join = joined.await(Wait.PRESENCE_EVENT_TIMEOUT_MS) as PNPresenceEventResult
        assertEquals("docs-joiner", join.uuid)
        assertEquals(2, join.occupancy)
        assertNotNull(join.timestamp)
        assertTimetoken(join.timetoken)
    }

    @Test
    fun leaveEvent(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        awaitNoOccupants(client, "my_channel")
        subscribeAndConnect(client, listOf("my_channel"), presence = true)
        val joined = armPresence(client, "join", "docs-joiner", "my_channel")
        val left = armPresence(client, "leave", "docs-joiner", "my_channel")

        // snippet.leaveEvent
        pubnub.addListener(object : EventListener {
            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                if (pnPresenceEventResult.event == "leave") {
                    pnPresenceEventResult.uuid // 175c2c67-b2a9-470d-8f4b-1db94f90e39e
                    pnPresenceEventResult.timestamp // 1345546797
                    pnPresenceEventResult.occupancy // # users in channel
                }
            }
        })
        // snippet.end

        subscribeAndConnect(friend, listOf("my_channel"))
        joined.await(Wait.PRESENCE_EVENT_TIMEOUT_MS)
        friend.unsubscribe(channels = listOf("my_channel"))
        val leave = left.await(Wait.PRESENCE_EVENT_TIMEOUT_MS) as PNPresenceEventResult
        assertEquals("docs-joiner", leave.uuid)
        assertEquals(1, leave.occupancy)
        assertNotNull(leave.timestamp)
    }

    @Test
    fun timeout(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val lost = clients.kotlinClient(OPEN, "docs-joiner") {
            presenceTimeout = 20
            suppressLeaveEvents = true
        }
        awaitNoOccupants(client, "my_channel")
        subscribeAndConnect(client, listOf("my_channel"), presence = true)
        val joined = armPresence(client, "join", "docs-joiner", "my_channel")
        val timedOut = armPresence(client, "timeout", "docs-joiner", "my_channel")

        // snippet.timeout
        pubnub.addListener(object : EventListener {
            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                if (pnPresenceEventResult.event == "timeout") {
                    pnPresenceEventResult.uuid // 175c2c67-b2a9-470d-8f4b-1db94f90e39e
                    pnPresenceEventResult.timestamp // 1345546797
                    pnPresenceEventResult.occupancy // # users in channel
                }
            }
        })
        // snippet.end

        subscribeAndConnect(lost, listOf("my_channel"))
        joined.await(Wait.PRESENCE_EVENT_TIMEOUT_MS)
        // The joiner stops without a leave, so only the presence timeout of 20 seconds removes it.
        lost.destroy()
        val timeoutEvent = timedOut.await(Wait.PRESENCE_TIMEOUT_MS) as PNPresenceEventResult
        assertEquals("docs-joiner", timeoutEvent.uuid)
        assertEquals(1, timeoutEvent.occupancy)
        assertNotNull(timeoutEvent.timestamp)
    }

    @Test
    fun stateChange(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        awaitNoOccupants(client, "my_channel")
        subscribeAndConnect(client, listOf("my_channel"), presence = true)
        val joined = armPresence(client, "join", "docs-joiner", "my_channel")
        val changed = armPresence(client, "state-change", "docs-joiner", "my_channel")

        // snippet.stateChange
        pubnub.addListener(object : EventListener {
            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                if (pnPresenceEventResult.event == "state-change") {
                    pnPresenceEventResult.uuid // 175c2c67-b2a9-470d-8f4b-1db94f90e39e
                    pnPresenceEventResult.timestamp // 1345546797
                    pnPresenceEventResult.occupancy // # users in channel
                    pnPresenceEventResult.state?.asJsonObject // {"data":{"isTyping":true}}
                }
            }
        })
        // snippet.end

        subscribeAndConnect(friend, listOf("my_channel"))
        joined.await(Wait.PRESENCE_EVENT_TIMEOUT_MS)
        friend.setPresenceState(channels = listOf("my_channel"), state = mapOf("isTyping" to true)).sync()
        val change = changed.await(Wait.PRESENCE_EVENT_TIMEOUT_MS) as PNPresenceEventResult
        assertEquals("docs-joiner", change.uuid)
        assertEquals("""{"isTyping":true}""", change.state.toString())
        assertNotNull(change.timestamp)
    }

    @Test
    fun interval(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        awaitNoOccupants(client, "my_channel")
        subscribeAndConnect(client, listOf("my_channel"), presence = true)
        val interval = armPresenceEvent(client, "interval", "my_channel")

        // snippet.interval
        pubnub.addListener(object : EventListener {
            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                if (pnPresenceEventResult.event == "interval") {
                    pnPresenceEventResult.uuid // 175c2c67-b2a9-470d-8f4b-1db94f90e39e
                    pnPresenceEventResult.timestamp // 1345546797
                    pnPresenceEventResult.occupancy // # users in channel
                }
            }
        })
        // snippet.end

        joinCrowd(clients, "my_channel", ANNOUNCE_MAX)
        val event = interval.await(Wait.PRESENCE_TIMEOUT_MS) as PNPresenceEventResult
        assertNotNull(event.timestamp)
        assertTrue((event.occupancy ?: 0) > ANNOUNCE_MAX, "the interval event does not report the crowd")
    }

    @Test
    fun intervalExampleWithTwoNewUUIDs(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        awaitNoOccupants(client, "my_channel")
        subscribeAndConnect(client, listOf("my_channel"), presence = true)
        // The first interval events can come before the crowd has joined, so wait for one that reports the crowd.
        val interval = Wait.expectEvent(client, "presence") { event ->
            event is PNPresenceEventResult && event.event == "interval" && event.channel == "my_channel" &&
                (event.occupancy ?: 0) > ANNOUNCE_MAX
        }

        // snippet.intervalExampleWithTwoNewUUIDs
        pubnub.addListener(object : EventListener {
            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                if (pnPresenceEventResult.event == "interval") {
                    pnPresenceEventResult.timestamp // 1345546797
                    pnPresenceEventResult.occupancy // # users in channel
                    pnPresenceEventResult.join // ["uuid1", "uuid2"]
                    pnPresenceEventResult.timeout // ["uuid3"]
                }
            }
        })
        // snippet.end

        joinCrowd(clients, "my_channel", ANNOUNCE_MAX)
        val event = interval.await(Wait.PRESENCE_TIMEOUT_MS) as PNPresenceEventResult
        assertNotNull(event.timestamp)
        assertTrue((event.occupancy ?: 0) > ANNOUNCE_MAX, "the interval event does not report the crowd")
        // The open keyset does not list who joined in its interval events (join comes back empty or without the crowd), so only
        // the presence of the field and the names it holds are checked.
        assertNotNull(event.join, "the interval event has no list of joined users")
        assertTrue(event.join!!.all { it.startsWith("docs-crowd-") }, "the interval event lists users that did not join")
    }

    @Test
    fun intervalExampleWithHerNowRefresh(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        awaitNoOccupants(client, "my_channel")
        subscribeAndConnect(client, listOf("my_channel"), presence = true)
        val interval = armPresenceEvent(client, "interval", "my_channel")

        // snippet.intervalExampleWithHerNowRefresh
        pubnub.addListener(object : EventListener {
            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                if (pnPresenceEventResult.event == "interval") {
                    pnPresenceEventResult.timestamp // 1345546797
                    pnPresenceEventResult.occupancy // # users in channel
                    pnPresenceEventResult.hereNowRefresh // true
                }
            }
        })
        // snippet.end

        joinCrowd(clients, "my_channel", ANNOUNCE_MAX)
        val event = interval.await(Wait.PRESENCE_TIMEOUT_MS) as PNPresenceEventResult
        assertNotNull(event.timestamp)
        assertTrue((event.occupancy ?: 0) > ANNOUNCE_MAX, "the interval event does not report the crowd")
        assertNotNull(event.hereNowRefresh, "the interval event has no hereNowRefresh")
    }

    @Test
    fun wildcardSubscribeToChannels(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        val text = probe("wildcard")
        val connected = Wait.connected(client, listOf("foo.*"))
        val arrived = armMessage(client, "foo.bar", text)

        // snippet.wildcardSubscribeToChannels
        pubnub.subscribe(
            channels = listOf("foo.*") // subscribe to channels information
        )
        // snippet.end

        connected.await()
        val sent = publishFrom(friend, "foo.bar", text, shouldStore = false)
        val message = arrived.await() as PNMessageResult
        assertEquals("foo.bar", message.channel)
        assertEquals("foo.*", message.subscription)
        assertEquals(sent.timetoken, message.timetoken)
    }

    @Test
    fun subscribeWithState(clients: LiveClients) {
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        Retry.retryTransient(label = "create awesomeChannelGroup") {
            friend.addChannelsToChannelGroup(listOf("awesomeChannel"), "awesomeChannelGroup").sync()
        }
        Cleanup.record("channel_group", "awesomeChannelGroup")
        awaitNoOccupants(friend, "awesomeChannel")

        // snippet.subscribeWithState
        val pnConfiguration = com.pubnub.api.v2.PNConfiguration.builder(UserId("myUserId"), "demo").apply {
            publishKey = "demo"
            // snippet.hide
            com.pubnub.docs.support.LiveKeys.apply(this, "open")
            // snippet.show
        }

        class ComplexData(
            val fieldA: String,
            val fieldB: Int
        )

        val pubnub = PubNub.create(pnConfiguration.build())

        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, status: PNStatus) {
                if (status.category == PNStatusCategory.PNConnectedCategory) {
                    pubnub.setPresenceState(
                        channels = listOf("awesomeChannel"),
                        channelGroups = listOf("awesomeChannelGroup"),
                        state = ComplexData("Awesome", 10)
                    ).async { result ->
                        // handle set state response
                    }
                }
            }
        })

        pubnub.subscribe(
            channels = listOf("awesomeChannel")
        )

        // snippet.end
        clients.track(pubnub)

        Wait.eventually("the state of myUserId is set on awesomeChannel") {
            val state = friend.getPresenceState(channels = listOf("awesomeChannel"), uuid = "myUserId").sync().stateByUUID
            assertEquals("""{"fieldA":"Awesome","fieldB":10}""", state["awesomeChannel"].toString())
        }
        val here = friend.hereNow(channels = listOf("awesomeChannel"), includeState = true).sync()
        val occupant = here.channels["awesomeChannel"]!!.occupants.single { it.uuid == "myUserId" }
        assertEquals("""{"fieldA":"Awesome","fieldB":10}""", occupant.state.toString())
    }

    @Test
    fun subscribeToChannelGroup(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        createGroup(friend, "cg1", "ch3")
        createGroup(friend, "cg2", "bar")
        val text = probe("group")
        val connected = Wait.connected(client, listOf("ch1", "ch2"), listOf("cg1", "cg2"))
        val arrived = armMessage(client, "ch3", text)

        // snippet.subscribeToChannelGroup
        pubnub.subscribe(
            channels = listOf("ch1", "ch2"), // subscribe to channels
            channelGroups = listOf("cg1", "cg2"), // subscribe to channel groups
            withTimetoken = 1337L, // optional, pass a timetoken
            withPresence = true // also subscribe to related presence information
        )
        // snippet.end

        connected.await()
        assertEquals(setOf("ch1", "ch2"), client.getSubscribedChannels().toSet())
        assertEquals(setOf("cg1", "cg2"), client.getSubscribedChannelGroups().toSet())
        publishFrom(friend, "ch3", text, shouldStore = false)
        val message = arrived.await() as PNMessageResult
        assertEquals("ch3", message.channel)
        assertEquals("cg1", message.subscription)
    }

    @Test
    fun subscribeToPresenceChannelOfChannelGroup(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        createGroup(friend, "cg1", "ch3")
        createGroup(friend, "cg2", "bar")
        awaitNoOccupants(friend, "ch3", "bar")
        val connected = Wait.connected(client, channelGroups = listOf("cg1", "cg2"))
        val joined = armPresence(client, "join", "docs-joiner", "ch3")

        // snippet.subscribeToPresenceChannelOfChannelGroup
        pubnub.subscribe(
            channelGroups = listOf("cg1", "cg2"), // subscribe to channel groups
            withTimetoken = 1337L, // optional, pass a timetoken
            withPresence = true // also subscribe to related presence information
        )
        // snippet.end

        connected.await()
        assertEquals(setOf("cg1", "cg2"), client.getSubscribedChannelGroups().toSet())
        subscribeAndConnect(friend, listOf("ch3"))
        val join = joined.await(Wait.PRESENCE_EVENT_TIMEOUT_MS) as PNPresenceEventResult
        assertEquals("ch3", join.channel)
        assertEquals("cg1", join.subscription)
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Subscriptions and subscription sets
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun createSubscription(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        awaitNoOccupants(friend, "channelName")
        val connected = Wait.connected(client, listOf("channelName"))
        val joined = armPresence(client, "join", "docs-joiner", "channelName")
        val text = probe("create-subscription")
        val arrived = armMessage(client, "channelName", text)

        // snippet.createSubscription
        // Entity-based, local-scoped

        // Specify the channel for subscription
        val myChannel = pubnub.channel("channelName")

        // Create subscription options, if any
        val options = SubscriptionOptions.receivePresenceEvents()

        // Return a Subscription object that is used to establish the subscription
        val subscription = myChannel.subscription(options)

        // Activate the subscription to start receiving events
        subscription.subscribe()
        // snippet.end

        connected.await()
        assertEquals(listOf("channelName"), client.getSubscribedChannels())
        subscribeAndConnect(friend, listOf("channelName"))
        assertEquals("docs-joiner", (joined.await(Wait.PRESENCE_EVENT_TIMEOUT_MS) as PNPresenceEventResult).uuid)
        publishFrom(friend, "channelName", text, shouldStore = false)
        assertEquals(text, (arrived.await() as PNMessageResult).message.asString)
    }

    @Test
    fun subscription(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        val text = probe("subscription")
        val myChannel = pubnub.channel("channelName")
        val subscription = myChannel.subscription()
        val subscriptionSet = pubnub.subscriptionSetOf(channels = setOf("channelName"))
        val connected = Wait.connected(client, listOf("channelName"))
        val arrived = armMessage(client, "channelName", text)

        // snippet.subscription
        // For subscription
        subscription.subscribe()
        // For subscription set
        subscriptionSet.subscribe()

        // snippet.end

        connected.await()
        assertEquals(listOf("channelName"), client.getSubscribedChannels())
        publishFrom(friend, "channelName", text, shouldStore = false)
        assertEquals(text, (arrived.await() as PNMessageResult).message.asString)
    }

    @Test
    fun subscribeBasicUsage(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        val textOne = probe("set-one")
        val textTwo = probe("set-two")
        val connected = Wait.connected(client, listOf("my_channel", "other_channel"))
        val arrivedOne = armMessage(client, "my_channel", textOne)
        val arrivedTwo = armMessage(client, "other_channel", textTwo)

        // snippet.subscribeBasicUsage
        // Step 1: Create a subscription set
        val subscriptionSet = pubnub.subscriptionSetOf(
            // Specify channels with default options
            channels = setOf("my_channel", "other_channel"),
        )

        // Step 2: Subscribe using the subscription set
        subscriptionSet.subscribe()

        // snippet.end

        connected.await()
        assertEquals(setOf("my_channel", "other_channel"), client.getSubscribedChannels().toSet())
        publishFrom(friend, "my_channel", textOne, shouldStore = false)
        publishFrom(friend, "other_channel", textTwo, shouldStore = false)
        assertEquals("my_channel", (arrivedOne.await() as PNMessageResult).channel)
        assertEquals("other_channel", (arrivedTwo.await() as PNMessageResult).channel)
    }

    @Test
    fun createSubscriptionSetFrom2individualSubscriptions(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        createGroup(friend, "channelGroup", "ch3")
        val kept = probe("set-kept")
        val removed = probe("set-removed")
        val viaGroup = probe("set-group")

        // snippet.createSubscriptionSetFrom2individualSubscriptions
        // Create subscriptions
        val subscription1 = pubnub.channel("channelName").subscription()
        val subscription2 = pubnub.channelGroup("channelGroup").subscription()
        val subscription3 = pubnub.channel("channelName03").subscription()

        // Combine into a subscription set
        val subscriptionSet = subscription1 + subscription2

        // Add another subscription to the set
        subscriptionSet += subscription3
        // Or
        subscriptionSet.add(subscription3)

        // Remove a subscription from the set
        subscriptionSet -= subscription3
        // Or
        subscriptionSet.remove(subscription3)

        // snippet.end

        assertEquals(2, subscriptionSet.subscriptions.size)
        val connected = Wait.connected(client, listOf("channelName"), listOf("channelGroup"))
        val arrivedKept = armMessage(client, "channelName", kept)
        val arrivedGroup = armMessage(client, "ch3", viaGroup)
        val arrivedRemoved = armMessage(client, "channelName03", removed)
        subscriptionSet.subscribe()
        connected.await()
        publishFrom(friend, "channelName", kept, shouldStore = false)
        publishFrom(friend, "ch3", viaGroup, shouldStore = false)
        publishFrom(friend, "channelName03", removed, shouldStore = false)
        assertEquals(kept, (arrivedKept.await() as PNMessageResult).message.asString)
        assertEquals("channelGroup", (arrivedGroup.await() as PNMessageResult).subscription)
        assertNothingArrives(arrivedRemoved, "a message of the removed subscription arrived")
    }

    @Test
    fun subscribeWithTimetoken(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        val text = probe("with-timetoken")
        val channels = setOf("my_channel", "other_channel")
        val subscriptionSet = pubnub.subscriptionSetOf(channels = channels)
        val yourTimeToken = 100000000000L // Directly using Long type
        val connected = Wait.connected(client, listOf("my_channel", "other_channel"))
        val arrived = armMessage(client, "other_channel", text)

        // snippet.subscribeWithTimetoken
        subscriptionSet.subscribe(SubscriptionCursor(timetoken = yourTimeToken))
        // snippet.end

        connected.await()
        assertEquals(setOf("my_channel", "other_channel"), client.getSubscribedChannels().toSet())
        publishFrom(friend, "other_channel", text, shouldStore = false)
        assertEquals(text, (arrived.await() as PNMessageResult).message.asString)
    }

    @Test
    fun subscribeToSubscriptionSetUsingTimetoken(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        val text = probe("set-timetoken")
        val connected = Wait.connected(client, listOf("my_channel", "other_channel"))
        val arrived = armMessage(client, "my_channel", text)

        // snippet.subscribeToSubscriptionSetUsingTimetoken
        // Define the channels to subscribe to
        val channels = setOf("my_channel", "other_channel")

        // Create a subscription set with specified channels and subscription options
        val options = SubscriptionOptions.receivePresenceEvents()
        val subscriptionSet = pubnub.subscriptionSetOf(channels = channels, options = options)

        // Define the timetoken for where the subscription should start
        val yourTimeToken = 100000000000L // Directly using Long type

        // Subscribe to the created SubscriptionSet with the desired timetoken
        subscriptionSet.subscribe(SubscriptionCursor(timetoken = yourTimeToken))

        // snippet.end

        connected.await()
        assertEquals(setOf("my_channel", "other_channel"), client.getSubscribedChannels().toSet())
        publishFrom(friend, "my_channel", text, shouldStore = false)
        assertEquals(text, (arrived.await() as PNMessageResult).message.asString)
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Event listeners
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun addListener(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        val text = probe("add-listener")
        Cleanup.record("channel_metadata", "my_channel")
        awaitNoOccupants(client, "my_channel")
        val out = Stdout.captureForTest()

        // snippet.addListener
        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, status: PNStatus) {
                println("Status category: ${status.category}")
                // PNConnectedCategory, PNReconnectedCategory, PNDisconnectedCategory

                println("Status error: ${status.error}")
                // true or false
            }

            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                println("Presence event: ${pnPresenceEventResult.event}")
                println("Presence channel: ${pnPresenceEventResult.channel}")
                println("Presence uuid: ${pnPresenceEventResult.uuid}")
                println("Presence timetoken: ${pnPresenceEventResult.timetoken}")
                println("Presence occupancy: ${pnPresenceEventResult.occupancy}")
            }

            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                println("Message payload: ${pnMessageResult.message}")
                println("Message channel: ${pnMessageResult.channel}")
                println("Message publisher: ${pnMessageResult.publisher}")
                println("Message timetoken: ${pnMessageResult.timetoken}")
            }

            override fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult) {
                println("Signal payload: ${pnSignalResult.message}")
                println("Signal channel: ${pnSignalResult.channel}")
                println("Signal publisher: ${pnSignalResult.publisher}")
                println("Signal timetoken: ${pnSignalResult.timetoken}")
            }

            override fun messageAction(pubnub: PubNub, pnMessageActionResult: PNMessageActionResult) {
                with(pnMessageActionResult.messageAction) {
                    println("Message reaction type: $type")
                    println("Message reaction value: $value")
                    println("Message reaction uuid: $uuid")
                    println("Message reaction actionTimetoken: $actionTimetoken")
                    println("Message reaction messageTimetoken: $messageTimetoken")
                }

                println("Message reaction subscription: ${pnMessageActionResult.subscription}")
                println("Message reaction channel: ${pnMessageActionResult.channel}")
                println("Message reaction timetoken: ${pnMessageActionResult.timetoken}")
            }

            override fun objects(pubnub: PubNub, objectEvent: PNObjectEventResult) {
                println("Object event channel: ${objectEvent.channel}")
                println("Object event publisher: ${objectEvent.publisher}")
                println("Object event subscription: ${objectEvent.subscription}")
                println("Object event timetoken: ${objectEvent.timetoken}")
                println("Object event userMetadata: ${objectEvent.userMetadata}")

                with(objectEvent.extractedMessage) {
                    println("Object event event: $event")
                    println("Object event source: $source")
                    println("Object event type: $type")
                    println("Object event version: $version")
                }
            }
        })
        // snippet.end

        subscribeAndConnect(client, listOf("my_channel"), presence = true)
        Wait.eventually("the status listener printed the connected status") {
            out.assertPrinted("Status category: PNConnectedCategory")
            out.assertPrinted("Status error: false")
        }

        subscribeAndConnect(friend, listOf("my_channel"))
        Wait.eventually("the presence listener printed the join of the friend") {
            out.assertPrinted("Presence event: join")
            out.assertPrinted("Presence uuid: docs-joiner")
            out.assertPrinted("Presence channel: my_channel")
            out.assertPrinted("Presence occupancy: 2")
        }

        val sent = publishFrom(friend, "my_channel", text, shouldStore = true)
        Cleanup.record("history", "my_channel")
        Wait.eventually("the message listener printed the message") {
            out.assertPrinted("Message payload: \"$text\"")
            out.assertPrinted("Message channel: my_channel")
            out.assertPrinted("Message publisher: docs-joiner")
            out.assertPrinted("Message timetoken: ${sent.timetoken}")
        }

        // A signal payload is limited to 64 bytes, so it gets a short text.
        val signalText = "sig-" + UUID.randomUUID().toString().substring(0, 8)
        val signal = Retry.retryTransient(label = "signal") { friend.signal("my_channel", signalText).sync() }
        Wait.eventually("the signal listener printed the signal") {
            out.assertPrinted("Signal payload: \"$signalText\"")
            out.assertPrinted("Signal publisher: docs-joiner")
            out.assertPrinted("Signal timetoken: ${signal.timetoken}")
        }

        val action = Retry.retryTransient(label = "message action") {
            friend.addMessageAction("my_channel", PNMessageAction("reaction", "smiley_face", sent.timetoken)).sync()
        }
        Wait.eventually("the message action listener printed the reaction") {
            out.assertPrinted("Message reaction type: reaction")
            out.assertPrinted("Message reaction value: smiley_face")
            out.assertPrinted("Message reaction uuid: docs-joiner")
            out.assertPrinted("Message reaction actionTimetoken: ${action.actionTimetoken}")
            out.assertPrinted("Message reaction messageTimetoken: ${sent.timetoken}")
        }

        Retry.retryTransient(label = "channel metadata") { friend.setChannelMetadata("my_channel", name = "Docs channel").sync() }
        Wait.eventually("the objects listener printed the channel update") {
            out.assertPrinted("Object event channel: my_channel")
            out.assertPrinted("Object event event: set")
            out.assertPrinted("Object event type: channel")
        }
    }

    @Test
    fun removeListener(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        val text = probe("remove-listener")

        // snippet.removeListener
        val listener = object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {}

            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {}
            // and other callbacks
        }

        pubnub.addListener(listener)

        // some time later
        pubnub.removeListener(listener)
        // snippet.end

        // The callbacks of the sample are empty, so the removal has no output to observe. What the service shows is that
        // the client is healthy after the add and the remove and that a listener added later still gets the events.
        recorder.assertNoFailures()
        val arrived = armMessage(client, "my_channel", text)
        subscribeAndConnect(client, listOf("my_channel"))
        publishFrom(friend, "my_channel", text, shouldStore = false)
        assertEquals(text, (arrived.await() as PNMessageResult).message.asString)
    }

    @Test
    fun addListenersBasic(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        val text = probe("listeners-basic")
        Cleanup.record("channel_metadata", "my_channel")
        val out = Stdout.captureForTest()
        val connected = Wait.connected(client, listOf("my_channel", "other_channel"))

        // snippet.addListenersBasic
        // Create a subscription to a specific channel
        val subscription = pubnub.channel("my_channel").subscription()

        // Add a listener to the subscription for handling various event types
        subscription.addListener(object : EventListener {
            override fun message(pubnub: PubNub, message: PNMessageResult) {
                // Log or process message
                println("Message: ${message.message}")
            }

            override fun signal(pubnub: PubNub, signal: PNSignalResult) {
                // Handle signals
                println("Signal: ${signal.message}")
            }

            override fun messageAction(pubnub: PubNub, messageAction: PNMessageActionResult) {
                // Handle message reactions
                println("Message Reaction: ${messageAction.data}")
            }

            override fun file(pubnub: PubNub, file: PNFileEventResult) {
                // Handle file events
                println("File: ${file.file.name}")
            }

            override fun objects(pubnub: PubNub, obj: PNObjectEventResult) {
                // Handle metadata updates
                println("App Context: ${obj.extractedMessage.event}")
            }

            override fun presence(pubnub: PubNub, presence: PNPresenceEventResult) {
                // Handle presence updates
                // requires a subscription with presence
                println("Presence: ${presence.uuid} - ${presence.event}")
            }
        })

        // Activate the subscription to start receiving events
        subscription.subscribe()

        // Print a status when successfully subscribed
        println("Subscribed to channel 'my_channel'")

        // Create subscription set
        val subscriptionSet = pubnub.subscriptionSetOf(
            // Specify channels with default options
            channels = setOf("my_channel", "other_channel"),
        )

        // Add listener to the subscriptionSet
        subscriptionSet.addListener(object : EventListener {
            override fun message(pubnub: PubNub, message: PNMessageResult) {
                // Log or process message
                println("Message: ${message.message}")
            }
        })

        // Activate the subscriptionSet to start receiving events
        subscriptionSet.subscribe()

        // snippet.end

        connected.await()
        out.assertPrinted("Subscribed to channel 'my_channel'")

        // The subscription and the set both listen to my_channel, so each message prints twice.
        val sent = publishFrom(friend, "my_channel", text, shouldStore = true)
        Cleanup.record("history", "my_channel")
        Wait.eventually("the subscription and the set printed the message") {
            assertEquals(2, out.lines("Message: \"$text\"").size, "the message was not printed once per listener")
        }

        val signalText = "sig-" + UUID.randomUUID().toString().substring(0, 8)
        Retry.retryTransient(label = "signal") { friend.signal("my_channel", signalText).sync() }
        Wait.eventually("the subscription printed the signal") { out.assertPrinted("Signal: \"$signalText\"") }

        Retry.retryTransient(label = "message action") {
            friend.addMessageAction("my_channel", PNMessageAction("reaction", "smiley_face", sent.timetoken)).sync()
        }
        Wait.eventually("the subscription printed the message action") {
            // The sample prints the action object, which has no toString, so only its class name shows.
            out.assertPrinted("Message Reaction: com.pubnub.api.models.consumer.message_actions.PNMessageAction@")
        }

        Retry.retryTransient(label = "channel metadata") { friend.setChannelMetadata("my_channel", name = "Docs channel").sync() }
        Wait.eventually("the subscription printed the App Context event") { out.assertPrinted("App Context: set") }

        val fileName = "docs-listener-${UUID.randomUUID()}.txt"
        val upload = Retry.retryTransient(label = "send file") {
            ByteArrayInputStream("docs listener".toByteArray()).use { content ->
                friend.sendFile("my_channel", fileName, content).sync()
            }
        }
        Cleanup.cleanupAfterTest("delete the uploaded file") { friend.deleteFile("my_channel", fileName, upload.file.id).sync() }
        Wait.eventually("the subscription printed the file event") { out.assertPrinted("File: $fileName") }
    }

    @Test
    fun createListenerForOneTypeEvent(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val subscription = pubnub.channel("my_channel").subscription()

        // snippet.createListenerForOneTypeEvent
        subscription.onMessage = { message ->
            // Handle message
        }

        subscription.onSignal = { signal ->
            // Handle signal
        }

        subscription.onMessageAction = { messageAction ->
            // Handle message reaction
        }

        subscription.onFile = { file ->
            // Handle file event
        }

        subscription.onObjects = { obj ->
            // Handle metadata updates
        }

        subscription.onPresence = { presence ->
            // Handle presence updates
        }

        val onMessage: (PNMessageResult) -> Unit = { /* Handle message */ }
        val onSignal: (PNSignalResult) -> Unit = { /* Handle signal */ }
        val onMessageAction: (PNMessageActionResult) -> Unit = { /* Handle message reaction */ }
        val onFile: (PNFileEventResult) -> Unit = { /* Handle file event */ }
        val onObjects: (PNObjectEventResult) -> Unit = { /* Handle metadata updates */ }
        val onPresence: (PNPresenceEventResult) -> Unit = { /* Handle presence updates */ }

        subscription.onMessage = onMessage
        subscription.onSignal = onSignal
        subscription.onMessageAction = onMessageAction
        subscription.onFile = onFile
        subscription.onObjects = onObjects
        subscription.onPresence = onPresence

        // snippet.end

        assertSame(onMessage, subscription.onMessage)
        assertSame(onSignal, subscription.onSignal)
        assertSame(onMessageAction, subscription.onMessageAction)
        assertSame(onFile, subscription.onFile)
        assertSame(onObjects, subscription.onObjects)
        assertSame(onPresence, subscription.onPresence)
    }

    @Test
    fun removeEventListener(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        val subscription = pubnub.channel("my_channel").subscription()
        val text = probe("remove-event-listener")
        val handled = AtomicInteger()
        subscription.onMessage = { handled.incrementAndGet() }
        val connected = Wait.connected(client, listOf("my_channel"))
        subscription.subscribe()
        connected.await()
        val before = armMessage(client, "my_channel", text)
        publishFrom(friend, "my_channel", text, shouldStore = false)
        before.await()
        Wait.eventually("the handler of the subscription ran") { assertEquals(1, handled.get(), "the handler did not run once") }

        // snippet.removeEventListener
        subscription.onMessage = null
        subscription.onSignal = null
        subscription.onMessageAction = null
        subscription.onFile = null
        subscription.onObjects = null
        subscription.onPresence = null

        // snippet.end

        assertNull(subscription.onMessage)
        assertNull(subscription.onSignal)
        assertNull(subscription.onMessageAction)
        assertNull(subscription.onFile)
        assertNull(subscription.onObjects)
        assertNull(subscription.onPresence)
        val afterText = probe("after-removal")
        val after = armMessage(client, "my_channel", afterText)
        publishFrom(friend, "my_channel", afterText, shouldStore = false)
        after.await()
        assertNothingHandled(handled, 1)
    }

    @Test
    fun addConnectionStatusListenerMethod(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val out = Stdout.captureForTest()

        // snippet.addConnectionStatusListenerMethod
        pubnub.addListener(object : StatusListener {
            override fun status(pubnub: PubNub, status: PNStatus) {
                // Handle connection status updates
                println("Connection Status: ${status.category}")
            }
        })

        // snippet.end

        subscribeAndConnect(client, listOf("my_channel"))
        Wait.eventually("the status listener printed the connected status") {
            out.assertPrinted("Connection Status: PNConnectedCategory")
        }
        client.unsubscribeAll()
        Wait.eventually("the status listener printed the disconnected status") {
            out.assertPrinted("Connection Status: PNDisconnectedCategory")
        }
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Unsubscribe
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun unsubscribeFromChannel(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        val control = clients.kotlinClient(OPEN, "docs-receiver")
        val text = probe("unsubscribe")
        subscribeAndConnect(client, listOf("my_channel"))
        subscribeAndConnect(control, listOf("my_channel"))
        assertEquals(listOf("my_channel"), client.getSubscribedChannels())

        // snippet.unsubscribeFromChannel
        pubnub.unsubscribe(
            channels = listOf("my_channel") // subscribe to channel groups
        )
        // snippet.end

        assertTrue(client.getSubscribedChannels().isEmpty(), "the client is still subscribed to my_channel")
        val controlArrived = armMessage(control, "my_channel", text)
        val afterArrived = armMessage(client, "my_channel", text)
        publishFrom(friend, "my_channel", text, shouldStore = false)
        controlArrived.await()
        assertNothingArrives(afterArrived, "a message arrived after the unsubscribe")
    }

    @Test
    fun unsubscribeFromMultiple(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        val control = clients.kotlinClient(OPEN, "docs-receiver")
        createGroup(friend, "cg1", "ch3")
        createGroup(friend, "cg2", "bar")
        createGroup(friend, "cg3", "coolChannel")
        val textChannel = probe("unsubscribe-channel")
        val textGroup = probe("unsubscribe-group")
        subscribeAndConnect(client, listOf("ch1", "ch2", "ch3"), listOf("cg1", "cg2", "cg3"))
        subscribeAndConnect(control, listOf("ch1"), listOf("cg1"))

        // snippet.unsubscribeFromMultiple
        pubnub.unsubscribe(
            channels = listOf("ch1", "ch2", "ch3"),
            channelGroups = listOf("cg1", "cg2", "cg3")
        )
        // snippet.end

        assertTrue(client.getSubscribedChannels().isEmpty(), "the client is still subscribed to channels")
        assertTrue(client.getSubscribedChannelGroups().isEmpty(), "the client is still subscribed to channel groups")
        val controlChannel = armMessage(control, "ch1", textChannel)
        val controlGroup = armMessage(control, "ch3", textGroup)
        val afterChannel = armMessage(client, "ch1", textChannel)
        val afterGroup = armMessage(client, "ch3", textGroup)
        publishFrom(friend, "ch1", textChannel, shouldStore = false)
        publishFrom(friend, "ch3", textGroup, shouldStore = false)
        controlChannel.await()
        controlGroup.await()
        assertNothingArrives(afterChannel, "a channel message arrived after the unsubscribe")
        assertNothingArrives(afterGroup, "a channel group message arrived after the unsubscribe")
    }

    @Test
    fun unsubscribeFromChannelGroup(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        val control = clients.kotlinClient(OPEN, "docs-receiver")
        createGroup(friend, "cg1", "ch3")
        createGroup(friend, "cg2", "bar")
        createGroup(friend, "cg3", "coolChannel")
        val text = probe("unsubscribe-group")
        subscribeAndConnect(client, groups = listOf("cg1", "cg2", "cg3"))
        subscribeAndConnect(control, groups = listOf("cg1"))

        // snippet.unsubscribeFromChannelGroup
        pubnub.unsubscribe(
            channelGroups = listOf("cg1", "cg2", "cg3")
        )
        // snippet.end

        assertTrue(client.getSubscribedChannelGroups().isEmpty(), "the client is still subscribed to channel groups")
        val controlArrived = armMessage(control, "ch3", text)
        val afterArrived = armMessage(client, "ch3", text)
        publishFrom(friend, "ch3", text, shouldStore = false)
        controlArrived.await()
        assertNothingArrives(afterArrived, "a channel group message arrived after the unsubscribe")
    }

    @Test
    fun unsubscribeAndOnPresenceLeave(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        awaitNoOccupants(client, "my_channel")
        subscribeAndConnect(client, listOf("my_channel"), presence = true)
        val joined = armPresence(client, "join", "docs-joiner", "my_channel")
        val left = armPresence(client, "leave", "docs-joiner", "my_channel")

        // snippet.unsubscribeAndOnPresenceLeave
        pubnub.addListener(object : EventListener {
            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                if (pnPresenceEventResult.event == "leave") {
                    pnPresenceEventResult.timestamp // 1345546797
                    pnPresenceEventResult.occupancy // 2
                    pnPresenceEventResult.uuid // left_uuid
                }
            }
        })
        // snippet.end

        subscribeAndConnect(friend, listOf("my_channel"))
        joined.await(Wait.PRESENCE_EVENT_TIMEOUT_MS)
        friend.unsubscribe(channels = listOf("my_channel"))
        val leave = left.await(Wait.PRESENCE_EVENT_TIMEOUT_MS) as PNPresenceEventResult
        assertEquals("docs-joiner", leave.uuid)
        assertEquals(1, leave.occupancy)
        assertNotNull(leave.timestamp)
    }

    @Test
    fun unsubscribeFromMultipleAndOnPresenceLeave(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        awaitNoOccupants(client, "ch1", "ch2")
        subscribeAndConnect(client, listOf("ch1", "ch2"), presence = true)
        val joined = armPresence(client, "join", "docs-joiner", "ch2")
        val left = armPresence(client, "leave", "docs-joiner", "ch2")

        // snippet.unsubscribeFromMultipleAndOnPresenceLeave
        pubnub.addListener(object : EventListener {
            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                if (pnPresenceEventResult.event == "leave") {
                    pnPresenceEventResult.timestamp // 1345546797
                    pnPresenceEventResult.occupancy // 2
                    pnPresenceEventResult.uuid // left_uuid
                }
            }
        })
        // snippet.end

        subscribeAndConnect(friend, listOf("ch1", "ch2"))
        joined.await(Wait.PRESENCE_EVENT_TIMEOUT_MS)
        friend.unsubscribe(channels = listOf("ch1", "ch2"))
        val leave = left.await(Wait.PRESENCE_EVENT_TIMEOUT_MS) as PNPresenceEventResult
        assertEquals("docs-joiner", leave.uuid)
        assertEquals(1, leave.occupancy)
        assertNotNull(leave.timestamp)
    }

    @Test
    fun unsubscribeFromChannelGroupAndOnPresenceLeave(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        createGroup(friend, "cg1", "ch3")
        awaitNoOccupants(friend, "ch3")
        subscribeAndConnect(client, groups = listOf("cg1"), presence = true)
        val joined = armPresence(client, "join", "docs-joiner", "ch3")
        val left = armPresence(client, "leave", "docs-joiner", "ch3")

        // snippet.unsubscribeFromChannelGroupAndOnPresenceLeave
        pubnub.addListener(object : EventListener {
            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                if (pnPresenceEventResult.event == "leave") {
                    pnPresenceEventResult.timestamp // 1345546797
                    pnPresenceEventResult.occupancy // 2
                    pnPresenceEventResult.uuid // left_uuid
                }
            }
        })
        // snippet.end

        subscribeAndConnect(friend, listOf("ch3"))
        joined.await(Wait.PRESENCE_EVENT_TIMEOUT_MS)
        friend.unsubscribe(channels = listOf("ch3"))
        val leave = left.await(Wait.PRESENCE_EVENT_TIMEOUT_MS) as PNPresenceEventResult
        assertEquals("docs-joiner", leave.uuid)
        assertEquals("cg1", leave.subscription)
        assertNotNull(leave.timestamp)
    }

    @Test
    fun unsubscribeMethod(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        val control = clients.kotlinClient(OPEN, "docs-receiver")
        val text = probe("unsubscribe-method")
        val subscription = pubnub.channel("my_channel").subscription()
        val subscriptionSet = pubnub.subscriptionSetOf(channels = setOf("my_channel", "other_channel"))
        val connected = Wait.connected(client, listOf("my_channel", "other_channel"))
        subscription.subscribe()
        subscriptionSet.subscribe()
        connected.await()
        subscribeAndConnect(control, listOf("my_channel", "other_channel"))

        // snippet.unsubscribeMethod
        // For subscription
        subscription.unsubscribe()
        // For subscription set
        subscriptionSet.unsubscribe()
        // snippet.end

        Wait.eventually("the client dropped both channels") {
            assertTrue(client.getSubscribedChannels().isEmpty(), "the client is still subscribed to channels")
        }
        val controlArrived = armMessage(control, "my_channel", text)
        val afterArrived = armMessage(client, "my_channel", text)
        publishFrom(friend, "my_channel", text, shouldStore = false)
        controlArrived.await()
        assertNothingArrives(afterArrived, "a message arrived after the unsubscribe")
    }

    @Test
    fun unsubscribeBasic(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        val control = clients.kotlinClient(OPEN, "docs-receiver")
        val text = probe("unsubscribe-basic")
        val subscription = pubnub.channel("my_channel").subscription()
        subscribeAndConnect(control, listOf("my_channel"))

        // snippet.unsubscribeBasic
        // Subscribe to a channel
        subscription.subscribe()

        // Unsubscribe from that channel
        subscription.unsubscribe()
        // snippet.end

        Wait.eventually("the client dropped the channel") {
            assertTrue(client.getSubscribedChannels().isEmpty(), "the client is still subscribed to my_channel")
        }
        val controlArrived = armMessage(control, "my_channel", text)
        val afterArrived = armMessage(client, "my_channel", text)
        publishFrom(friend, "my_channel", text, shouldStore = false)
        controlArrived.await()
        assertNothingArrives(afterArrived, "a message arrived after the unsubscribe")
        // The sample subscribes and unsubscribes at once, so a join of the handshake can outlive the leave.
        leaveAs(clients, "myUserId", listOf("my_channel"))
    }

    @Test
    fun unsubscribeAllBasic(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        val control = clients.kotlinClient(OPEN, "docs-receiver")
        createGroup(friend, "my_channel_group", "ch3")
        val textChannel = probe("unsubscribe-all-channel")
        val textGroup = probe("unsubscribe-all-group")
        subscribeAndConnect(control, listOf("my_channel"), listOf("my_channel_group"))

        // snippet.unsubscribeAllBasic
        // Subscribe to channels
        pubnub.subscribe(channels = listOf("my_channel", "other_channel"))

        // Subscribe to a channel group
        pubnub.subscribe(channelGroups = listOf("my_channel_group"))

        // Later, when you want to unsubscribe from all subscriptions
        pubnub.unsubscribeAll()
        // snippet.end

        Wait.eventually("the client dropped every subscription") {
            assertTrue(client.getSubscribedChannels().isEmpty(), "the client is still subscribed to channels")
            assertTrue(client.getSubscribedChannelGroups().isEmpty(), "the client is still subscribed to channel groups")
        }
        val controlChannel = armMessage(control, "my_channel", textChannel)
        val controlGroup = armMessage(control, "ch3", textGroup)
        val afterChannel = armMessage(client, "my_channel", textChannel)
        val afterGroup = armMessage(client, "ch3", textGroup)
        publishFrom(friend, "my_channel", textChannel, shouldStore = false)
        publishFrom(friend, "ch3", textGroup, shouldStore = false)
        controlChannel.await()
        controlGroup.await()
        assertNothingArrives(afterChannel, "a channel message arrived after unsubscribeAll")
        assertNothingArrives(afterGroup, "a channel group message arrived after unsubscribeAll")
        // The sample subscribes and unsubscribes at once, so a join of the handshake can outlive the leave.
        leaveAs(clients, "myUserId", listOf("my_channel", "other_channel"), listOf("my_channel_group"))
    }

    @Test
    fun unsubscribeAllMethod(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val friend = clients.kotlinClient(OPEN, "docs-joiner")
        val control = clients.kotlinClient(OPEN, "docs-receiver")
        val text = probe("unsubscribe-all-method")
        subscribeAndConnect(client, listOf("my_channel", "other_channel"))
        subscribeAndConnect(control, listOf("my_channel"))

        // snippet.unsubscribeAllMethod
        pubnub.unsubscribeAll()
        // snippet.end

        assertTrue(client.getSubscribedChannels().isEmpty(), "the client is still subscribed to channels")
        val controlArrived = armMessage(control, "my_channel", text)
        val afterArrived = armMessage(client, "my_channel", text)
        publishFrom(friend, "my_channel", text, shouldStore = false)
        controlArrived.await()
        assertNothingArrives(afterArrived, "a message arrived after unsubscribeAll")
    }

    private companion object {
        const val OPEN = "open"

        /** The announce max of the presence rule of the open keyset: above it presence switches to interval events. */
        const val ANNOUNCE_MAX = 20
    }
}
