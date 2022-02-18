package com.pubnub.api.state

typealias Transition<S, EV, EF> = (S, EV) -> Pair<S, Collection<EF>>

typealias StateMachine<EV, EF> = (EV) -> Collection<EF>