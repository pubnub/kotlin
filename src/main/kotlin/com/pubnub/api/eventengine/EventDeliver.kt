package com.pubnub.api.eventengine

import com.pubnub.api.subscribe.eventengine.event.Event

interface EventDeliver { // toDo change name from EventHandler. e.g. ToEventEngineEventDeliver This interface doesn't describe event Handling but only how to pass event further e.g to Queue that stores events to be handled
    fun passEventForHandling(event: Event)
}
