package com.pubnub.okhttp3

import okhttp3.Call
import okhttp3.Request

internal class PNCallFactory(private val callFactory: Call.Factory) : Call.Factory {
    override fun newCall(request: Request): Call {
        return PNCall(callFactory.newCall(request))
    }
}
