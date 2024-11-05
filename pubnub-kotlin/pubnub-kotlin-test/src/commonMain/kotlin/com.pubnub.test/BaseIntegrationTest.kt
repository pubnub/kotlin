package com.pubnub.test

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.createPNConfiguration
import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.createEventListener
import com.pubnub.kmp.createPubNub
import com.pubnub.kmp.createStatusListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds

abstract class BaseIntegrationTest {
    lateinit var config: PNConfiguration
    lateinit var config02: PNConfiguration
    lateinit var configPamServer: PNConfiguration
    lateinit var configPamClient: PNConfiguration
    lateinit var pubnub: PubNub
    lateinit var pubnub02: PubNub
    lateinit var pubnubPamServer: PubNub
    lateinit var pubnubPamClient: PubNub

    @BeforeTest
    open fun before() {
        config = createPNConfiguration(
            UserId(randomString()),
            Keys.subKey,
            Keys.pubKey,
            logVerbosity = PNLogVerbosity.BODY,
            authToken = null
        )
        config02 = createPNConfiguration(
            UserId(randomString()),
            Keys.subKey,
            Keys.pubKey,
            logVerbosity = PNLogVerbosity.BODY,
            authToken = null
        )
        pubnub = createPubNub(config)
        pubnub02 = createPubNub(config02)
        configPamServer = createPNConfiguration(
            UserId(randomString()),
            Keys.pamSubKey,
            Keys.pamPubKey,
            Keys.pamSecKey,
            PNLogVerbosity.BODY
        )
        configPamClient = createPNConfiguration(
            UserId(randomString()),
            Keys.pamSubKey,
            Keys.pamPubKey,
            logVerbosity = PNLogVerbosity.BODY,
            authToken = null
        )
        pubnubPamServer = createPubNub(configPamServer)
        pubnubPamClient = createPubNub(configPamClient)
    }

    @AfterTest
    open fun after() {
        pubnub.unsubscribeAll()
        pubnub.destroy()
        pubnub02.unsubscribeAll()
        pubnub02.destroy()
        pubnubPamServer.unsubscribeAll()
        pubnubPamServer.destroy()
        pubnubPamClient.unsubscribeAll()
        pubnubPamClient.destroy()
    }
}

suspend fun <T> PNFuture<T>.await(): T {
    val t = suspendCancellableCoroutine { cont ->
        async { result ->
            result.onSuccess {
                cont.resume(it)
            }.onFailure {
                cont.resumeWithException(it)
            }
        }
    }
    withContext(Dispatchers.Default) {
        delay(100)
    }
    return t
}

class PubNubTest(
    private val pubNub: PubNub,
    private val withPresenceOverride: Boolean,
    backgroundScope: CoroutineScope,
    private val checkAllEvents: Boolean = true,
) {
    private val messageQueue = Channel<PNEvent>(10)
    private val statusQueue = Channel<PNStatus>(10)

    private val statusVerificationListener = createStatusListener(pubNub) { _, status ->
        backgroundScope.launch {
            statusQueue.send(status)
        }
    }
    private val eventVerificationListener = createEventListener(
        pubNub,
        onMessage = { _, event ->
            backgroundScope.launch {
                messageQueue.send(event)
            }
        },
        onSignal = { _, event ->
            backgroundScope.launch {
                messageQueue.send(event)
            }
        },
        onFile = { _, event ->
            backgroundScope.launch {
                messageQueue.send(event)
            }
        },
        onPresence = { _, event ->
            backgroundScope.launch {
                messageQueue.send(event)
            }
        },
        onObjects = { _, event ->
            backgroundScope.launch {
                messageQueue.send(event)
            }
        },
        onMessageAction = { _, event ->
            backgroundScope.launch {
                messageQueue.send(event)
            }
        },
    )

    init {
        pubNub.addListener(eventVerificationListener)
        pubNub.addListener(statusVerificationListener)
    }

    suspend fun com.pubnub.api.v2.entities.Channel.awaitSubscribe(options: SubscriptionOptions = EmptyOptions) = suspendCancellableCoroutine { cont ->
        val subscription = subscription(options)
        val statusListener = createStatusListener(pubNub) { _, pnStatus ->
            if ((pnStatus.category == PNStatusCategory.PNConnectedCategory || pnStatus.category == PNStatusCategory.PNSubscriptionChanged) &&
                pnStatus.affectedChannels.contains(name)
            ) {
                cont.resume(subscription)
            }
            if (pnStatus.category == PNStatusCategory.PNUnexpectedDisconnectCategory || pnStatus.category == PNStatusCategory.PNConnectionError) {
                cont.resumeWithException(pnStatus.exception ?: RuntimeException(pnStatus.category.toString()))
            }
        }
        pubNub.addListener(statusListener)
        cont.invokeOnCancellation {
            pubNub.removeListener(statusListener)
        }
        subscription.subscribe()
    }

    suspend fun PubNub.awaitSubscribe(
        channels: Collection<String> = setOf(),
        channelGroups: Collection<String> = setOf(),
        withPresence: Boolean = false,
        customSubscriptionBlock: () -> Unit = {
            subscribe(channels.toList(), channelGroups.toList(), withPresence)
        }
    ) = suspendCancellableCoroutine { cont ->
        val statusListener = createStatusListener(pubNub) { _, pnStatus ->
            if ((pnStatus.category == PNStatusCategory.PNConnectedCategory || pnStatus.category == PNStatusCategory.PNSubscriptionChanged) &&
                pnStatus.affectedChannels.containsAll(channels) && pnStatus.affectedChannelGroups.containsAll(
                    channelGroups
                ) || (
                    getSubscribedChannels().containsAll(channels) && getSubscribedChannelGroups().containsAll(
                        channelGroups
                    )
                )
            ) {
                if (!cont.isCompleted) {
                    cont.resume(Unit)
                }
            } else if (pnStatus.category == PNStatusCategory.PNUnexpectedDisconnectCategory || pnStatus.category == PNStatusCategory.PNConnectionError) {
                cont.resumeWithException(pnStatus.exception ?: RuntimeException(pnStatus.category.toString()))
            }
        }
        pubNub.addListener(statusListener)
        cont.invokeOnCancellation {
            pubNub.removeListener(statusListener)
        }
        customSubscriptionBlock()
    }

    suspend fun PubNub.awaitUnsubscribe(
        channels: Collection<String> = setOf(),
        channelGroups: Collection<String> = setOf(),
        customUnsubscribeBlock: () -> Unit = {
            unsubscribe(channels.toList(), channelGroups.toList())
        }
    ) {
        coroutineScope {
            val job = launch {
                suspendCancellableCoroutine { cont ->
                    val statusListener = createStatusListener(pubNub) { _, pnStatus ->
                        if (cont.isCompleted) {
                            return@createStatusListener
                        }
                        if ((pnStatus.category == PNStatusCategory.PNDisconnectedCategory || pnStatus.category == PNStatusCategory.PNSubscriptionChanged) &&
                            (
                                pnStatus.affectedChannels.containsAll(channels) && pnStatus.affectedChannelGroups.containsAll(
                                    channelGroups
                                )
                            )
                        ) {
                            cont.resume(Unit)
                        }
                        if (pnStatus.category == PNStatusCategory.PNUnexpectedDisconnectCategory || pnStatus.category == PNStatusCategory.PNConnectionError) {
                            cont.resumeWithException(pnStatus.exception ?: RuntimeException(pnStatus.category.toString()))
                        }
                    }
                    pubNub.addListener(statusListener)
                    cont.invokeOnCancellation {
                        pubNub.removeListener(statusListener)
                    }
                    customUnsubscribeBlock()
                }
            }
            withContext(Dispatchers.Default) {
                delay(500.milliseconds)
            }
            job.cancelAndJoin()
            if (getSubscribedChannels().any { it in channels } || getSubscribedChannelGroups().any { it in channelGroups }) {
                error("Didn't unsubscribe on time")
            }
        }
    }

//    fun unsubscribeAll() {
//        pubNub.unsubscribeAll()
//        val status = statusQueue.take()
//        Assert.assertTrue(status.category == PNStatusCategory.PNDisconnectedCategory)
//    }

    suspend fun nextStatus(): PNStatus = statusQueue.receive()

//    suspend fun nextStatus(timeout: Duration): PNStatus? {
//        return try {
//            FutureTask {
//                statusQueue.take()
//            }.get(timeout.inWholeMilliseconds, TimeUnit.MILLISECONDS)
//        } catch (e: TimeoutException) {
//            null
//        }
//    }

    suspend fun <T : PNEvent> nextEvent(): T {
        return messageQueue.receive() as T
    }

    suspend fun nextMessage() = nextEvent<PNMessageResult>()

    suspend fun skip(n: Int = 1) {
        for (i in 0 until n) {
            nextEvent<PNEvent>()
        }
    }

    fun close() {
        pubNub.unsubscribeAll()
        pubNub.destroy()

        val remainingMessages = buildList {
            messageQueue.tryReceive().getOrNull()?.apply { add(this) } ?: return@buildList
        }
        if (checkAllEvents) {
            assertTrue(
                "There were ${remainingMessages.size} unverified events in the test: ${remainingMessages.joinToString(", ")}"
            ) { remainingMessages.isEmpty() }
        }
    }
}

suspend fun PubNub.test(
    backgroundScope: CoroutineScope,
    withPresence: Boolean = false,
    checkAllEvents: Boolean = true,
    action: suspend PubNubTest.() -> Unit,
) {
    val pubNubTest = PubNubTest(this, withPresence, backgroundScope, checkAllEvents)
    try {
        with(pubNubTest) {
            action()
        }
    } finally {
        pubNubTest.close()
    }
}

fun randomString() = (0..6).map { "abcdefghijklmnopqrstuvw".random() }.joinToString("")
