package com.pubnub.matchmaking

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
class Player(val name: String, val age: Int, val email: String) {
    fun printMy() {
        println("name: $name, age: $age")
    }
}
