package com.pubnub.matchmaking.internal.serverREST.entities

data class UserPairWithScore(val firstUserIndex: Int, val secondUserIndex: Int, val score: Double)
