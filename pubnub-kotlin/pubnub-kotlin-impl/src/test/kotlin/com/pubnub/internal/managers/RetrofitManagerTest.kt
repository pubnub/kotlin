package com.pubnub.internal.managers

import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.internal.interceptor.SignatureInterceptor
import org.junit.Assert
import org.junit.Test

class RetrofitManagerTest : BaseTest() {
    @Test
    fun `retrofit manager created from another has shared OkHttpClients data`() {
        val retrofitManager = RetrofitManager(pubnub, pubnub.configuration)

        val clonedRetrofitManager = RetrofitManager(retrofitManager, pubnub.configuration)

        Assert.assertEquals(
            retrofitManager.subscriptionClientInstance!!.dispatcher,
            clonedRetrofitManager.subscriptionClientInstance!!.dispatcher,
        )
        Assert.assertEquals(
            retrofitManager.transactionClientInstance!!.dispatcher,
            clonedRetrofitManager.transactionClientInstance!!.dispatcher,
        )
        Assert.assertEquals(
            retrofitManager.noSignatureClientInstance!!.dispatcher,
            clonedRetrofitManager.noSignatureClientInstance!!.dispatcher,
        )
        Assert.assertEquals(
            retrofitManager.heartbeatClientInstance!!.dispatcher,
            clonedRetrofitManager.heartbeatClientInstance!!.dispatcher,
        )

        Assert.assertEquals(
            retrofitManager.subscriptionClientInstance!!.connectionPool,
            clonedRetrofitManager.subscriptionClientInstance!!.connectionPool,
        )
        Assert.assertEquals(
            retrofitManager.transactionClientInstance!!.connectionPool,
            clonedRetrofitManager.transactionClientInstance!!.connectionPool,
        )
        Assert.assertEquals(
            retrofitManager.noSignatureClientInstance!!.connectionPool,
            clonedRetrofitManager.noSignatureClientInstance!!.connectionPool,
        )
        Assert.assertEquals(
            retrofitManager.heartbeatClientInstance!!.connectionPool,
            clonedRetrofitManager.heartbeatClientInstance!!.connectionPool,
        )
    }

    @Test
    fun `retrofit manager created from another has separate SignatureInterceptors`() {
        val retrofitManager = RetrofitManager(pubnub, pubnub.configuration)
        val clonedRetrofitManager = RetrofitManager(retrofitManager, pubnub.configuration)

        Assert.assertNotEquals(
            retrofitManager.transactionClientInstance!!.interceptors.single { it is SignatureInterceptor },
            clonedRetrofitManager.subscriptionClientInstance!!.interceptors.single { it is SignatureInterceptor },
        )
    }

    @Test
    fun `heartbeat client has its own OkHttpClient - dispatcher and connection pool isolation`() {
        val retrofitManager = RetrofitManager(pubnub, pubnub.configuration)

        // Heartbeat/leave run on a dedicated OkHttpClient — with both its own Dispatcher AND its own
        // ConnectionPool — separate from the transaction client that carries publish/history/objects/etc.
        //
        // Dispatcher isolation (H/1-relevant): a busy async publish burst cannot queue heartbeats behind
        //   itself in Dispatcher.readyAsyncCalls.
        //
        // Connection-pool isolation (H/2-relevant, load-bearing): under H/2, shared-pool traffic would
        //   coalesce onto one multiplexed TCP connection. That connection shares a TCP send buffer and
        //   receive window, so a large publish write causes TCP-layer head-of-line blocking even though
        //   H/2 streams are logically independent, and a single dead connection would synchronize failure
        //   across all streams. Giving heartbeat its own pool means it has an independent H/2 connection
        //   (and an independent failure domain) from user-RPC traffic.
        Assert.assertNotSame(
            retrofitManager.transactionClientInstance!!,
            retrofitManager.heartbeatClientInstance!!,
        )
        Assert.assertNotSame(
            retrofitManager.transactionClientInstance!!.dispatcher,
            retrofitManager.heartbeatClientInstance!!.dispatcher,
        )
        Assert.assertNotSame(
            retrofitManager.transactionClientInstance!!.connectionPool,
            retrofitManager.heartbeatClientInstance!!.connectionPool,
        )
    }

    @Test
    fun `all internal clients apply OkHttp's default maxRequestsPerHost when maximumConnections is unset`() {
        val retrofitManager = RetrofitManager(pubnub, pubnub.configuration)

        // With default config (`maximumConnections = null`), the SDK does not override
        // OkHttp's built-in `Dispatcher.maxRequestsPerHost` (5). Assert that built-in value
        // rather than reading from the config, because null-as-config means "use OkHttp's default".
        val okHttpDefault = 5
        Assert.assertNull(pubnub.configuration.maximumConnections)
        Assert.assertEquals(okHttpDefault, retrofitManager.transactionClientInstance!!.dispatcher.maxRequestsPerHost)
        Assert.assertEquals(okHttpDefault, retrofitManager.subscriptionClientInstance!!.dispatcher.maxRequestsPerHost)
        Assert.assertEquals(okHttpDefault, retrofitManager.noSignatureClientInstance!!.dispatcher.maxRequestsPerHost)
        Assert.assertEquals(okHttpDefault, retrofitManager.heartbeatClientInstance!!.dispatcher.maxRequestsPerHost)
    }

    @Test
    fun `clone constructor must NOT mutate parent dispatcher maxRequestsPerHost`() {
        // Regression test. OkHttpClient.newBuilder().build() returns a client that SHARES the parent's
        // Dispatcher instance. Before the fix in createOkHttpClient, applying
        // configuration.maximumConnections to the clone's dispatcher stomped the parent's cap at runtime —
        // any still-in-flight async calls on the parent would suddenly see a different maxRequestsPerHost.
        val parentManager = RetrofitManager(pubnub, pubnub.configuration)
        val parentCapBefore = parentManager.transactionClientInstance!!.dispatcher.maxRequestsPerHost

        // Build a derived configuration with a deliberately different maximumConnections and use it to
        // clone the RetrofitManager (the same flow used for per-endpoint overrides).
        val derivedConfig = PNConfiguration.builder(pubnub.configuration).apply {
            // Deliberately different from whatever the SDK default is, so the test doesn't accidentally
            // match the parent's cap by coincidence if the default changes.
            maximumConnections = parentCapBefore + 1
        }.build()
        RetrofitManager(parentManager, derivedConfig)

        Assert.assertEquals(
            "clone with derived config must not mutate parent dispatcher maxRequestsPerHost",
            parentCapBefore,
            parentManager.transactionClientInstance!!.dispatcher.maxRequestsPerHost,
        )
    }
}
