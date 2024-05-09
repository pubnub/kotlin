package com.pubnub.kmp

sealed interface Optional<T> {
    class Value<T>(val value: T) : Optional<T>
    class Absent<T> : Optional<T>
    fun onValue(action: (T)->Unit) : Optional<T> = this.apply { (this as? Value<T>)?.let { action(it.value)} }
    fun onAbsent(action: ()->Unit) : Optional<T> = this.apply { if (this is Absent) { action() }}
}