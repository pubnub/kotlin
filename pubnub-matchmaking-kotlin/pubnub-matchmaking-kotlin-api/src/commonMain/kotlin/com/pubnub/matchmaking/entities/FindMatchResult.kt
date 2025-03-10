package com.pubnub.matchmaking.entities

class FindMatchResult(
    val result: String,
    val disconnect: AutoCloseable?
)
