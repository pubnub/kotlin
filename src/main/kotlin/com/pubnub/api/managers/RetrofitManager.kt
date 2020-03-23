package com.pubnub.api.managers

import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.services.HistoryService
import com.pubnub.api.services.PresenceService
import com.pubnub.api.services.PublishService
import com.pubnub.api.services.SubscribeService
import com.pubnub.api.services.TimeService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitManager(val pubnub: PubNub) {

    private val transactionClientInstance: OkHttpClient by lazy {
        createOkHttpClient(pubnub.configuration.nonSubscribeRequestTimeout)
    }

    private val subscriptionClientInstance: OkHttpClient by lazy {
        createOkHttpClient(pubnub.configuration.subscribeTimeout)
    }

    internal val timeService: TimeService
    internal val publishService: PublishService
    internal val historyService: HistoryService
    internal val presenceService: PresenceService

    internal val subscribeService: SubscribeService


    init {
        val transactionInstance = createRetrofit(transactionClientInstance)
        val subscriptionInstance = createRetrofit(subscriptionClientInstance)

        timeService = transactionInstance.create(TimeService::class.java)
        publishService = transactionInstance.create(PublishService::class.java)
        historyService = transactionInstance.create(HistoryService::class.java)
        presenceService = transactionInstance.create(PresenceService::class.java)

        subscribeService = subscriptionInstance.create(SubscribeService::class.java)
    }

    private fun createOkHttpClient(readTimeout: Int): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()
            .readTimeout(readTimeout.toLong(), TimeUnit.SECONDS)
            .connectTimeout(pubnub.configuration.connectTimeout.toLong(), TimeUnit.SECONDS)

        with(pubnub.configuration) {
            if (logVerbosity == PNLogVerbosity.BODY) {
                okHttpBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
            if (sslSocketFactory != null && x509ExtendedTrustManager != null) {
                okHttpBuilder.sslSocketFactory(
                    pubnub.configuration.sslSocketFactory!!,
                    pubnub.configuration.x509ExtendedTrustManager!!
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

        pubnub.configuration.maximumConnections?.let { okHttpClient.dispatcher().maxRequestsPerHost = it }

        return okHttpClient
    }

    private fun createRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val retrofitBuilder = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(pubnub.baseUrl())
            .addConverterFactory(GsonConverterFactory.create())

        return retrofitBuilder.build()
    }

    fun destroy(force: Boolean = false) {
        closeExecutor(transactionClientInstance, force)
        closeExecutor(subscriptionClientInstance, force)
    }

    private fun closeExecutor(client: OkHttpClient, force: Boolean) {
        client.dispatcher().cancelAll()
        if (force) {
            client.connectionPool().evictAll()
            val executorService = client.dispatcher().executorService()
            executorService.shutdown()
        }
    }
}