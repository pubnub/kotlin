package com.pubnub.api.interceptor

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubUtil
import okhttp3.Interceptor
import okhttp3.Response

class SignatureInterceptor(val pubnub: PubNub) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = PubNubUtil.signRequest(originalRequest, pubnub.configuration, pubnub.timestamp())
        return chain.proceed(request)
    }
}
