package com.pubnub.contract.state

import com.pubnub.api.eventengine.EventSink
import com.pubnub.api.subscribe.eventengine.event.Event
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.LinkedBlockingQueue

private const val HAPPENING_TYPE_EVENT = "event"
private const val HAPPENING_TYPE = "type"
private const val HAPPENING_NAME = "name"
private const val PATH_FROM_CLASS_NAME_SEPARATOR = "$"

class EventSinkTestImpl(
    private val queue: LinkedBlockingQueue<Event>,
    private val happenings: CopyOnWriteArrayList<HashMap<String, String>>
) : EventSink {

    override fun add(event: Event) {
        val eventName = getEventName(event)
        val happeningTypeAndName = HashMap<String, String>().also {
            it[HAPPENING_TYPE] = HAPPENING_TYPE_EVENT
            it[HAPPENING_NAME] = eventName
        }

        happenings.add(happeningTypeAndName)
        queue.add(event)
    }

    private fun getEventName(event: Event): String {
        val eventClassPath = event.javaClass.name
        return eventClassPath.subSequence(eventClassPath.indexOf(PATH_FROM_CLASS_NAME_SEPARATOR) + 1, eventClassPath.length).toString()
    }
}
