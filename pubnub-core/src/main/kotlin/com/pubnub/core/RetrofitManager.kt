package com.pubnub.core

import retrofit2.Retrofit

interface RetrofitManager {
    val transactionInstance: Retrofit
}
