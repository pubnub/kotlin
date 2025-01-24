package com.pubnub.internal.managers

import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.interceptor.SignatureInterceptor
import com.pubnub.internal.services.AccessManagerService
import com.pubnub.internal.services.ChannelGroupService
import com.pubnub.internal.services.FilesService
import com.pubnub.internal.services.HistoryService
import com.pubnub.internal.services.MessageActionService
import com.pubnub.internal.services.ObjectsService
import com.pubnub.internal.services.PresenceService
import com.pubnub.internal.services.PublishService
import com.pubnub.internal.services.PushService
import com.pubnub.internal.services.S3Service
import com.pubnub.internal.services.SignalService
import com.pubnub.internal.services.SubscribeService
import com.pubnub.internal.services.TimeService
import com.pubnub.internal.vendor.AppEngineFactory.Factory
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.annotations.TestOnly
import retrofit2.Retrofit
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

class RetrofitManager(
    val pubnub: PubNubImpl,
    private val configuration: PNConfiguration,
    @get:TestOnly internal var transactionClientInstance: OkHttpClient? = null,
    @get:TestOnly internal var subscriptionClientInstance: OkHttpClient? = null,
    @get:TestOnly internal var noSignatureClientInstance: OkHttpClient? = null,
) {
    private var signatureInterceptor: SignatureInterceptor = SignatureInterceptor(configuration)

    internal val timeService: TimeService
    internal val publishService: PublishService
    internal val historyService: HistoryService
    internal val presenceService: PresenceService
    internal val messageActionService: MessageActionService
    internal val signalService: SignalService
    internal val channelGroupService: ChannelGroupService
    internal val pushService: PushService
    internal val accessManagerService: AccessManagerService

    internal val subscribeService: SubscribeService
    internal val objectsService: ObjectsService
    internal val filesService: FilesService
    internal val s3Service: S3Service

    /**
     * Use to get a new RetrofitManager with shared OkHttpClients while overriding configuration values.
     */
    constructor(retrofitManager: RetrofitManager, configuration: PNConfiguration) : this(
        retrofitManager.pubnub,
        configuration,
        retrofitManager.transactionClientInstance,
        retrofitManager.subscriptionClientInstance,
        retrofitManager.noSignatureClientInstance,
    )

    init {
        if (!configuration.googleAppEngineNetworking) {
            transactionClientInstance = createOkHttpClient(configuration.nonSubscribeReadTimeout, parentOkHttpClient = transactionClientInstance)
            subscriptionClientInstance = createOkHttpClient(configuration.subscribeTimeout, parentOkHttpClient = subscriptionClientInstance)
            noSignatureClientInstance =
                createOkHttpClient(configuration.nonSubscribeReadTimeout, withSignature = false, parentOkHttpClient = noSignatureClientInstance)
        }

        val transactionInstance = createRetrofit(transactionClientInstance)
        val subscriptionInstance = createRetrofit(subscriptionClientInstance)
        val noSignatureInstance = createRetrofit(noSignatureClientInstance)

        timeService = transactionInstance.create(TimeService::class.java)
        publishService = transactionInstance.create(PublishService::class.java)
        historyService = transactionInstance.create(HistoryService::class.java)
        presenceService = transactionInstance.create(PresenceService::class.java)
        messageActionService = transactionInstance.create(MessageActionService::class.java)
        signalService = transactionInstance.create(SignalService::class.java)
        channelGroupService = transactionInstance.create(ChannelGroupService::class.java)
        pushService = transactionInstance.create(PushService::class.java)
        accessManagerService = transactionInstance.create(AccessManagerService::class.java)
        objectsService = transactionInstance.create(ObjectsService::class.java)
        filesService = transactionInstance.create(FilesService::class.java)
        s3Service = noSignatureInstance.create(S3Service::class.java)

        subscribeService = subscriptionInstance.create(SubscribeService::class.java)
    }

    fun getTransactionClientExecutorService(): ExecutorService? {
        return transactionClientInstance?.dispatcher?.executorService
    }

    private fun createOkHttpClient(
        readTimeout: Int,
        withSignature: Boolean = true,
        parentOkHttpClient: OkHttpClient? = null,
    ): OkHttpClient {
        val okHttpBuilder = parentOkHttpClient?.newBuilder() ?: OkHttpClient.Builder()

        okHttpBuilder
            .retryOnConnectionFailure(false)
            .readTimeout(readTimeout.toLong(), TimeUnit.SECONDS)
            .connectTimeout(configuration.connectTimeout.toLong(), TimeUnit.SECONDS)

        with(configuration) {
            if (logVerbosity == PNLogVerbosity.BODY) {
                okHttpBuilder.addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    },
                )
            }

            if (httpLoggingInterceptor != null) {
                okHttpBuilder.addInterceptor(httpLoggingInterceptor!!)
            }

            if (sslSocketFactory != null && x509ExtendedTrustManager != null) {
                okHttpBuilder.sslSocketFactory(
                    configuration.sslSocketFactory!!,
                    configuration.x509ExtendedTrustManager!!,
                )
            }
            connectionSpec?.let { okHttpBuilder.connectionSpecs(listOf(it)) }
            hostnameVerifier?.let { okHttpBuilder.hostnameVerifier(it) }
            proxy?.let { okHttpBuilder.proxy(it) }
            proxySelector?.let { okHttpBuilder.proxySelector(it) }
            proxyAuthenticator?.let { okHttpBuilder.proxyAuthenticator(it) }
            certificatePinner?.let { okHttpBuilder.certificatePinner(it) }
        }

        if (withSignature) {
            okHttpBuilder.interceptors().removeAll { it is SignatureInterceptor }
            okHttpBuilder.addInterceptor(signatureInterceptor)
        }

        val okHttpClient = okHttpBuilder.build()

        configuration.maximumConnections?.let { okHttpClient.dispatcher.maxRequestsPerHost = it }

        return okHttpClient
    }

    private fun createRetrofit(callFactory: Call.Factory?): Retrofit {
        val retrofitBuilder =
            Retrofit.Builder()
                .baseUrl(pubnub.baseUrl)
                .addConverterFactory(pubnub.mapper.converterFactory)

        if (configuration.googleAppEngineNetworking) {
            retrofitBuilder.callFactory(Factory(configuration))
        } else if (callFactory != null) {
            retrofitBuilder.callFactory(callFactory)
        } else {
            throw IllegalStateException("Can't instantiate PubNub")
        }
        return retrofitBuilder.build()
    }

    fun destroy(force: Boolean = false) {
        closeExecutor(transactionClientInstance, force)
        closeExecutor(subscriptionClientInstance, force)
        closeExecutor(noSignatureClientInstance, force)
    }

    private fun closeExecutor(
        client: OkHttpClient?,
        force: Boolean,
    ) {
        if (client != null) {
            client.dispatcher.cancelAll()
            client.connectionPool.evictAll()
            val executorService = client.dispatcher.executorService
            executorService.shutdown()
            if (force) {
                try {
                    if (!executorService.awaitTermination(100, TimeUnit.MILLISECONDS)) {
                        executorService.shutdownNow()
                    }
                } catch (e: InterruptedException) {
                    executorService.shutdownNow()
                }
            }
        }
    }
}
