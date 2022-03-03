package com.pubnub.api.subscribe.internal.data

import com.pubnub.api.Keys
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.models.server.SubscribeMetaData
import com.pubnub.api.subscribe.internal.*
import org.junit.Test
import kotlin.math.log

class InterpreterTest {


    @Test
    fun testDataDrivenStateMachine() {
        val module = Interpreter(
            signature = signature, reducers = mapOf(
                SAction.SetChannels to { ctx, ev ->
                    when (ev) {
                        is Commands.SubscribeIssued -> ctx.copy(channels = ctx.channels + ev.channels)
                        is Commands.UnsubscribeIssued -> ctx.copy(channels = ctx.channels - ev.channels.toSet())
                        else -> ctx
                    }
                },
                SAction.SetCursor to { ctx, ev ->
                    when (ev) {
                        is HandshakeResult.HandshakeSucceeded -> ctx.copy(cursor = ev.cursor)
                        is ReceivingResult.ReceivingSucceeded -> ctx.copy(
                            cursor = Cursor(
                                timetoken = ev.subscribeEnvelope.metadata.timetoken,
                                region = ev.subscribeEnvelope.metadata.region
                            )
                        )
                        else -> ctx
                    }
                }
            ),
            initialState = SState.Unsubscribed
        )

        val status = PNStatus(
            category = PNStatusCategory.PNBadRequestCategory,
            error = true,
            PNOperationType.PNSubscribeOperation
        )

        val inputs = listOf(
            Commands.SubscribeIssued(channels = listOf("ch1")),
            HandshakeResult.HandshakeSucceeded(Cursor(timetoken = 42, region = "12")),
            ReceivingResult.ReceivingSucceeded(
                SubscribeEnvelope(
                    metadata = SubscribeMetaData(75, "14"),
                    messages = listOf()
                )
            )
        )

        val effects = inputs.flatMap { ev ->
            val (ctx, efs) = module.handle(ev)
            efs.map { ctx to it }
        }

        println(effects)
    }

    @Test
    fun p() {
        val pubnub = PubNub(PNConfiguration(uuid = PubNub.generateUUID(), enableSubscribeBeta = true).apply {
            subscribeKey = Keys.subscribeKey
            publishKey = Keys.publishKey
            logVerbosity = PNLogVerbosity.BODY
        })

        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                println(pnStatus)
            }

            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                println(pnMessageResult)
            }
        })

        pubnub.subscribe(channels = listOf("ch1"))

        Thread.sleep(100)
        pubnub.publish("ch1", "hello").sync()

        Thread.sleep(500)
    }
}