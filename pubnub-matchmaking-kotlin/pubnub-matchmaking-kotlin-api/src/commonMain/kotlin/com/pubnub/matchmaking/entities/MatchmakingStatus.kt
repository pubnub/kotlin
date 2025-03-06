package com.pubnub.matchmaking.entities

enum class MatchmakingStatus {
    IN_QUEUE,
    MATCHMAKING_STARTED,
    RE_ADDED_TO_QUEUE,
    ALREADY_IN_QUEUE,
    MATCH_FOUND,
    CANCELLED,
    FAILED,

    INITIALLY_MATCHED, // todo do we need it,
    WAITING_FOR_CONFIRMATION// todo do we need it,
}