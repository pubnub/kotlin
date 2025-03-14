package com.pubnub.matchmaking.entities

enum class MatchmakingStatus {
    IN_QUEUE,
    MATCHMAKING_STARTED,
    RE_ADDED_TO_QUEUE,
    MATCH_FOUND,
    CANCELLED, // todo how to cancel matchmaking?
    FAILED,
    UNKNOWN,

    INITIALLY_MATCHED, // todo do we need it,
    WAITING_FOR_CONFIRMATION; // todo do we need it,

    companion object {
        fun fromString(value: String): MatchmakingStatus {
            return values().find { it.name.equals(value, ignoreCase = true) } ?: UNKNOWN
        }
    }
}
