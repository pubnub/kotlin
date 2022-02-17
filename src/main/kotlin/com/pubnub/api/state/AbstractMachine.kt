package com.pubnub.api.state

typealias Transitions<S, EV, EF> = (S, EV) -> Pair<S, Collection<EF>>
typealias Machine<EV, EF> = (EV) -> Collection<EF>