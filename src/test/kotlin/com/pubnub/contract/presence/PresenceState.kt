package com.pubnub.contract.presence

import com.pubnub.api.models.consumer.presence.PNGetStateResult
import com.pubnub.api.models.consumer.presence.PNHereNowResult
import com.pubnub.api.models.consumer.presence.PNSetStateResult

class PresenceState {
    var hereNowResult: PNHereNowResult? = null
    var setStateResult: PNSetStateResult? = null
    var getStateResult: PNGetStateResult? = null
}
