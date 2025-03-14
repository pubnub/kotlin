package com.pubnub.matchmaking.entities

interface MatchmakingCallback {
    fun onMatchFound(match: MatchmakingGroup?)

    fun onMatchmakingFailed(reason: String?)

    fun onStatusChange(status: MatchmakingStatus?)
}
