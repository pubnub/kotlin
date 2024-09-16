package com.pubnub.internal.interceptor

import com.pubnub.api.v2.PNConfiguration
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.PubNubUtil
import okhttp3.Interceptor
import okhttp3.Response

class SignatureInterceptor(private val configuration: PNConfiguration) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = PubNubUtil.signRequest(originalRequest, configuration, PubNubImpl.timestamp())
        return chain.proceed(request)
    }
}
