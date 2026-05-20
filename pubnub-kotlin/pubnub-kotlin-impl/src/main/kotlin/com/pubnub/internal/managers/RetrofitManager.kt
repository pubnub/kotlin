package com.pubnub.internal.managers

import com.pubnub.api.v2.PNConfiguration
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.interceptor.SignatureInterceptor
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.networkLogging.CustomPnHttpLoggingInterceptor
import com.pubnub.internal.services.AccessManagerService
import com.pubnub.internal.services.ChannelGroupService
import com.pubnub.internal.services.FilesService
import com.pubnub.internal.services.HeartbeatService
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
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import org.jetbrains.annotations.TestOnly
import retrofit2.Retrofit
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

class RetrofitManager(
    val pubnub: PubNubImpl,
    private val configuration: PNConfiguration,
    // todo make private
    @get:TestOnly internal var transactionClientInstance: OkHttpClient? = null,
    @get:TestOnly internal var subscriptionClientInstance: OkHttpClient? = null,
    @get:TestOnly internal var noSignatureClientInstance: OkHttpClient? = null,
    @get:TestOnly internal var heartbeatClientInstance: OkHttpClient? = null,
) {
    private var signatureInterceptor: SignatureInterceptor = SignatureInterceptor(configuration)

    internal val timeService: TimeService
    internal val publishService: PublishService
    internal val historyService: HistoryService
    internal val presenceService: PresenceService
    internal val heartbeatService: HeartbeatService
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
        retrofitManager.heartbeatClientInstance,
    )

    init {
        if (!configuration.googleAppEngineNetworking) {
            transactionClientInstance = createOkHttpClient(configuration.nonSubscribeReadTimeout, parentOkHttpClient = transactionClientInstance)
            subscriptionClientInstance = createOkHttpClient(configuration.subscribeTimeout, parentOkHttpClient = subscriptionClientInstance)
            noSignatureClientInstance =
                createOkHttpClient(configuration.nonSubscribeReadTimeout, withSignature = false, parentOkHttpClient = noSignatureClientInstance)
            heartbeatClientInstance = createOkHttpClient(configuration.nonSubscribeReadTimeout, parentOkHttpClient = heartbeatClientInstance)
        }

        val transactionInstance = createRetrofit(transactionClientInstance)
        val subscriptionInstance = createRetrofit(subscriptionClientInstance)
        val noSignatureInstance = createRetrofit(noSignatureClientInstance)
        val heartbeatInstance = createRetrofit(heartbeatClientInstance)

        timeService = transactionInstance.create(TimeService::class.java)
        publishService = transactionInstance.create(PublishService::class.java)
        historyService = transactionInstance.create(HistoryService::class.java)
        presenceService = transactionInstance.create(PresenceService::class.java)
        heartbeatService = heartbeatInstance.create(HeartbeatService::class.java)
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

        // Only create a new ConnectionPool if there's no parent (initial creation).
        // When cloning from a parent, reuse its ConnectionPool to share resources.
        if (parentOkHttpClient == null) {
            // OkHttp requires keepAliveDuration > 0 (even if maxIdleConnections == 0).
            // Be defensive here because configuration can come from non-Kotlin builders / Java callers.
            val poolMaxIdleConnections = configuration.connectionPoolMaxIdleConnections.coerceAtLeast(0)
            val poolKeepAliveSeconds = configuration.connectionPoolKeepAliveDuration.coerceAtLeast(1).toLong()

            okHttpBuilder.connectionPool(
                ConnectionPool(
                    poolMaxIdleConnections,
                    poolKeepAliveSeconds,
                    TimeUnit.SECONDS
                )
            )
        }

        with(configuration) {
            okHttpBuilder.interceptors().removeAll { interceptor ->
                interceptor is CustomPnHttpLoggingInterceptor
            }

            // todo detect that this is publish to portal and not log this to avoid recursion
            // Replace the standard HttpLoggingInterceptor with our custom one
            val customLogger = LoggerManager.instance.getLogger(pubnub.logConfig, CustomPnHttpLoggingInterceptor::class.java)
            okHttpBuilder.addInterceptor(
                CustomPnHttpLoggingInterceptor(
                    customLogger,
                    pubnub.mapper,
                    logVerbosity,
                    configuration.logContentConfig.loggedHttpResponseMaxBytes.toLong(),
                )
            )

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

        // maxRequestsPerHost lives on the Dispatcher instance, and OkHttpClient.newBuilder().build()
        // returns a client that SHARES the parent's Dispatcher. Mutating it here on a clone would change
        // the cap for the parent's still-in-flight requests too — so only apply it on initial construction,
        // the same gate used above for ConnectionPool. An override config can only change
        // maximumConnections for a net-new RetrofitManager, never as a side-effect on its parent.
        if (parentOkHttpClient == null) {
            configuration.maximumConnections?.let { okHttpClient.dispatcher.maxRequestsPerHost = it }
        }

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
        closeExecutor(heartbeatClientInstance, force)
    }

    private fun closeExecutor(
        client: OkHttpClient?,
        force: Boolean,
    ) {
        if (client != null) {
            client.dispatcher.cancelAll()

            // Proactively close any idle connections so the OS can tear down sockets immediately.
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
            client.connectionPool.evictAll()
        }
    }
}
