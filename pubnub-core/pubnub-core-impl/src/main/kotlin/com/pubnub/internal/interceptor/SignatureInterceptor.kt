package com.pubnub.internal.interceptor

import com.pubnub.internal.PubNubCore
import com.pubnub.internal.PubNubUtil
import okhttp3.Interceptor
import okhttp3.Response

class SignatureInterceptor(val pubnub: PubNubCore) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = PubNubUtil.signRequest(originalRequest, pubnub.configuration, pubnub.timestamp())
        return chain.proceed(request)
    }
}
