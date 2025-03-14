package com.pubnub.matchmaking.internal.serverREST.entities

import com.pubnub.matchmaking.User

data class MatchGroup(val users: Set<User>)
