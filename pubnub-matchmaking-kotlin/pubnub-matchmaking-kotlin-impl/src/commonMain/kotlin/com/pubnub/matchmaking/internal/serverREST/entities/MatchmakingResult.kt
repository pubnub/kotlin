package com.pubnub.matchmaking.internal.serverREST.entities

class MatchmakingResult(val matchGroups: Set<MatchGroup>, val unmatchedUserIds: Set<String>)
