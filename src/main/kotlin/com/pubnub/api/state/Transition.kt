package com.pubnub.api.state

typealias Transition<S, EV, EF> = (S, EV) -> Pair<S, List<EF>>
