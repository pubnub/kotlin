package com.pubnub.kmp

external interface JsMap<V>

fun <V> entriesOf(jsObject: JsMap<V>): List<Pair<String, V>> =
    (js("Object.entries") as (dynamic) -> Array<Array<V>>)
        .invoke(jsObject)
        .map { entry -> entry[0] as String to entry[1] }

fun <V> JsMap<V>.toMap(): Map<String, V> =
    entriesOf(this).toMap()
