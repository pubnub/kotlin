package com.pubnub.matchmaking

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport // todo remove, this is for JS Matchmaking test purposes
class Player(val name: String, val age: Int, val email: String) {
    fun printMy() {
        println("name: $name, age: $age")
    }
}
