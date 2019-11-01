package com.pubnub.api.managers

import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.services.HistoryService
import com.pubnub.api.services.PublishService
import com.pubnub.api.services.TimeService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitManager(val pubnub: PubNub) {

    private val transactionClientInstance = createOkHttpClient(pubnub.config.nonSubscribeRequestTimeout)
    private val transactionInstance = createRetrofit(transactionClientInstance)

    val timeService: TimeService = transactionInstance.create(TimeService::class.java)
    val publishService: PublishService = transactionInstance.create(PublishService::class.java)
    val historyService: HistoryService = transactionInstance.create(HistoryService::class.java)

    private fun createOkHttpClient(readTimeout: Int): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()
            .readTimeout(readTimeout.toLong(), TimeUnit.SECONDS)
            .connectTimeout(pubnub.config.connectTimeout.toLong(), TimeUnit.SECONDS)

        with(pubnub.config) {
            if (logVerbosity == PNLogVerbosity.BODY) {
                okHttpBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
            if (sslSocketFactory != null && x509ExtendedTrustManager != null) {
                okHttpBuilder.sslSocketFactory(
                    pubnub.config.sslSocketFactory!!,
                    pubnub.config.x509ExtendedTrustManager!!
                )
            }
            connectionSpec?.let { okHttpBuilder.connectionSpecs(listOf(it)) }
            hostnameVerifier?.let { okHttpBuilder.hostnameVerifier(it) }
            proxy?.let { okHttpBuilder.proxy(it) }
            proxySelector?.let { okHttpBuilder.proxySelector(it) }
            proxyAuthenticator?.let { okHttpBuilder.proxyAuthenticator(it) }
            certificatePinner?.let { okHttpBuilder.certificatePinner(it) }
        }

        val okHttpClient = okHttpBuilder.build()

        pubnub.config.maximumConnections?.let { okHttpClient.dispatcher().maxRequestsPerHost = it }

        return okHttpClient
    }

    private fun createRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val retrofitBuilder = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(pubnub.baseUrl())
            .addConverterFactory(GsonConverterFactory.create())

        return retrofitBuilder.build()
    }
}