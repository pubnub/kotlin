package com.pubnub.kmp

external interface JsMap<V>

fun <V> entriesOf(jsObject: JsMap<V>): List<Pair<String, V>> =
    (js("Object.entries") as (dynamic) -> Array<Array<V>>)
        .invoke(jsObject)
        .map { entry -> entry[0] as String to entry[1] }

fun <V> JsMap<V>.toMap(): Map<String, V> =
    entriesOf(this).toMap()

fun <V> Map<String, V>.toJsMap(): JsMap<V> = createJsObject<dynamic> {
    entries.forEach {
        this[it.key] = it.value
    }
}.unsafeCast<JsMap<V>>()

fun <T> createJsObject(configure: T.() -> Unit = {}): T = (js("({})") as T).apply(configure)
