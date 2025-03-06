package com.pubnub.matchmaking.entities

import com.pubnub.matchmaking.User

class MatchmakingGroup (val users: Set<User>)   //todo currently we returns Set<User>. What about Set<userid> ?
