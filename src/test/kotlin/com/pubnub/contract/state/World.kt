package com.pubnub.contract.state

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.subscribe.eventengine.Bus
import com.pubnub.api.subscribe.eventengine.QueueBus
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.contract.ContractTestConfig
import java.util.concurrent.CopyOnWriteArrayList
class World {
    val configuration: PNConfiguration by lazy {
        PNConfiguration(userId = UserId(PubNub.generateUUID())).apply {
            origin = ContractTestConfig.serverHostPort
            secure = false
            logVerbosity = PNLogVerbosity.BODY
        }
    }

    val events: MutableList<com.pubnub.api.eventengine.Event> = CopyOnWriteArrayList()
    val pubnub: PubNub by lazy {
        PubNub(
            configuration,
            eventBus = object : Bus {
                private val realEventBus = QueueBus()
                override fun add(event: Event) {
                    events.add(event)
                    realEventBus.add(event)
                }

                override fun next(): Event = realEventBus.next()
            }
        )
    }
    var pnException: PubNubException? = null
    var tokenString: String? = null
    var responseStatus: Int? = null
}
