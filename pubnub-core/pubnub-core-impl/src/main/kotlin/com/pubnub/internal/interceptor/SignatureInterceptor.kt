package com.pubnub.internal.interceptor

import com.pubnub.api.v2.BasePNConfiguration
import com.pubnub.internal.PubNubCore
import com.pubnub.internal.PubNubUtil
import okhttp3.Interceptor
import okhttp3.Response

class SignatureInterceptor(private val basePNConfiguration: BasePNConfiguration) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = PubNubUtil.signRequest(originalRequest, basePNConfiguration, PubNubCore.timestamp())
        return chain.proceed(request)
    }
}
