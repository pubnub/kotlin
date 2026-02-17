package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.pubsub.PublishEndpoint
import com.pubnub.internal.endpoints.pubsub.PublishEndpoint.Companion.PUBLISH_V1_MAX_GET_PATH_BYTES
import com.pubnub.internal.endpoints.pubsub.PublishEndpoint.Companion.PUBLISH_V1_MAX_POST_BODY_BYTES
import com.pubnub.internal.endpoints.pubsub.PublishEndpoint.Companion.SIGNATURE_QUERY_PARAMS_OVERHEAD_BYTES
import com.pubnub.internal.managers.RetrofitManager
import com.pubnub.internal.services.PublishService
import com.pubnub.internal.v2.PNConfigurationImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Request
import okio.Timeout
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TEST_CHANNEL = "testChannel"
private const val TEST_SUBKEY = "subKey"
private const val TEST_PUBKEY = "pubKey"
private const val TEST_SECRET_KEY = "secretKey"

/**
 * Tests for publish endpoint routing logic.
 * Verifies that messages are routed to V1 or V2 endpoint based on size limits.
 */
class PublishV2RoutingTest {
    private val pnConfigurationNoSecretKey =
        PNConfigurationImpl(
            userId = UserId(PubNubImpl.generateUUID()),
            subscribeKey = TEST_SUBKEY,
            publishKey = TEST_PUBKEY,
        )

    private val pnConfigurationWithSecretKey =
        PNConfigurationImpl(
            userId = UserId(PubNubImpl.generateUUID()),
            subscribeKey = TEST_SUBKEY,
            publishKey = TEST_PUBKEY,
            secretKey = TEST_SECRET_KEY,
        )

    private val pubNubNoSecretKey = spyk(PubNubImpl(pnConfigurationNoSecretKey))
    private val pubNubWithSecretKey = spyk(PubNubImpl(pnConfigurationWithSecretKey))

    private val retrofitManagerMock = mockk<RetrofitManager>()
    private val publishServiceMock = mockk<PublishService>()

    private val v1PostBodySlot = slot<Any>()
    private val v2PostBodySlot = slot<Any>()
    private val v1GetMessageSlot = slot<String>()

    /**
     * Fake Call that builds a realistic Request URL for GET path calculation.
     * The path length calculation depends on having a real URL to measure.
     */
    class FakeCallWithUrl(private val url: String) : Call<List<Any>> {
        override fun clone(): Call<List<Any>> = this

        override fun execute(): Response<List<Any>> = Response.success(listOf("test1", "test2", "10"))

        override fun enqueue(callback: Callback<List<Any>>) {}

        override fun isExecuted(): Boolean = false

        override fun cancel() {}

        override fun isCanceled(): Boolean = false

        override fun request(): Request = Request.Builder().url(url.toHttpUrl()).build()

        override fun timeout(): Timeout = Timeout.NONE
    }

    class FakeCall : Call<List<Any>> {
        override fun clone(): Call<List<Any>> = this

        override fun execute(): Response<List<Any>> = Response.success(listOf("test1", "test2", "10"))

        override fun enqueue(callback: Callback<List<Any>>) {}

        override fun isExecuted(): Boolean = false

        override fun cancel() {}

        override fun isCanceled(): Boolean = false

        override fun request(): Request = Request.Builder().url("https://ps.pndsn.com/publish").build()

        override fun timeout(): Timeout = Timeout.NONE
    }

    init {
        // Mock V1 GET publish
        every {
            publishServiceMock.publish(
                any(),
                any(),
                any(),
                capture(v1GetMessageSlot),
                any(),
            )
        } answers {
            // Build a realistic URL for path length calculation
            val message = v1GetMessageSlot.captured
            FakeCallWithUrl("https://ps.pndsn.com/publish/$TEST_PUBKEY/$TEST_SUBKEY/0/$TEST_CHANNEL/0/$message?seqn=1")
        }

        // Mock V1 POST publish
        every {
            publishServiceMock.publishWithPost(
                any(),
                any(),
                any(),
                capture(v1PostBodySlot),
                any(),
            )
        } answers { FakeCall() }

        // Mock V2 POST publish
        every {
            publishServiceMock.publishWithPostV2(
                any(),
                any(),
                any(),
                capture(v2PostBodySlot),
                any(),
            )
        } answers { FakeCall() }

        every { retrofitManagerMock.publishService } returns publishServiceMock

        every { pubNubNoSecretKey.configuration } returns pnConfigurationNoSecretKey
        every { pubNubWithSecretKey.configuration } returns pnConfigurationWithSecretKey

        every { pubNubNoSecretKey.retrofitManager } returns retrofitManagerMock
        every { pubNubWithSecretKey.retrofitManager } returns retrofitManagerMock
    }

    // ================================
    // POST Path Tests
    // ================================

    @Test
    fun `POST with body at exactly limit uses V1`() {
        // JSON serialization adds 2 bytes for quotes around string
        // So we need message length = LIMIT - 2 to get exactly LIMIT bytes after serialization
        val messageLength = PUBLISH_V1_MAX_POST_BODY_BYTES - 2
        val message = "x".repeat(messageLength)

        val publish = PublishEndpoint(
            pubnub = pubNubNoSecretKey,
            message = message,
            channel = TEST_CHANNEL,
            usePost = true,
        )
        publish.sync()

        verify(exactly = 1) { publishServiceMock.publishWithPost(any(), any(), any(), any(), any()) }
        verify(exactly = 0) { publishServiceMock.publishWithPostV2(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `POST with body exceeding limit by 1 byte uses V2`() {
        // JSON serialization adds 2 bytes for quotes around string
        // So we need message length = LIMIT - 1 to exceed LIMIT by 1 byte after serialization
        val messageLength = PUBLISH_V1_MAX_POST_BODY_BYTES - 1
        val message = "x".repeat(messageLength)

        val publish = PublishEndpoint(
            pubnub = pubNubNoSecretKey,
            message = message,
            channel = TEST_CHANNEL,
            usePost = true,
        )
        publish.sync()

        verify(exactly = 0) { publishServiceMock.publishWithPost(any(), any(), any(), any(), any()) }
        verify(exactly = 1) { publishServiceMock.publishWithPostV2(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `POST with very large message (100KB) uses V2`() {
        val message = "x".repeat(100_000)

        val publish = PublishEndpoint(
            pubnub = pubNubNoSecretKey,
            message = message,
            channel = TEST_CHANNEL,
            usePost = true,
        )
        publish.sync()

        verify(exactly = 0) { publishServiceMock.publishWithPost(any(), any(), any(), any(), any()) }
        verify(exactly = 1) { publishServiceMock.publishWithPostV2(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `POST with small message uses V1`() {
        val message = "Hello, World!"

        val publish = PublishEndpoint(
            pubnub = pubNubNoSecretKey,
            message = message,
            channel = TEST_CHANNEL,
            usePost = true,
        )
        publish.sync()

        verify(exactly = 1) { publishServiceMock.publishWithPost(any(), any(), any(), any(), any()) }
        verify(exactly = 0) { publishServiceMock.publishWithPostV2(any(), any(), any(), any(), any()) }
    }

    // ================================
    // GET Path Tests
    // ================================

    @Test
    fun `GET with small message uses V1 GET`() {
        val message = "Hello"

        val publish = PublishEndpoint(
            pubnub = pubNubNoSecretKey,
            message = message,
            channel = TEST_CHANNEL,
            usePost = false,
        )
        publish.sync()

        verify(exactly = 1) { publishServiceMock.publish(any(), any(), any(), any(), any()) }
        verify(exactly = 0) { publishServiceMock.publishWithPostV2(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `GET with large message falls back to V2 POST`() {
        // Create a message large enough to exceed GET path limit
        val message = "x".repeat(40_000)

        val publish = PublishEndpoint(
            pubnub = pubNubNoSecretKey,
            message = message,
            channel = TEST_CHANNEL,
            usePost = false,
        )
        publish.sync()

        // GET is called first to measure path length, then V2 POST is used
        verify(exactly = 1) { publishServiceMock.publish(any(), any(), any(), any(), any()) }
        verify(exactly = 1) { publishServiceMock.publishWithPostV2(any(), any(), any(), any(), any()) }
    }

    // ================================
    // Secret Key Signature Overhead Tests
    // ================================

    @Test
    fun `GET without secret key stays on V1 when path is exactly at limit`() {
        // GET path layout: /publish/pubKey/subKey/0/testChannel/0/%22<N x's>%22?seqn=1
        // Fixed overhead: 39 (path prefix) + 6 (two %22 for JSON quotes) + 7 (?seqn=1) = 52 bytes
        // Path+query length = 52 + N
        //
        // N = 32,700 → 52 + 32,700 = 32,752 = PUBLISH_V1_MAX_GET_PATH_BYTES → fits V1
        val message = "x".repeat(32_700)

        val publish = PublishEndpoint(
            pubnub = pubNubNoSecretKey,
            message = message,
            channel = TEST_CHANNEL,
            usePost = false,
        )
        publish.sync()

        verify(exactly = 1) { publishServiceMock.publish(any(), any(), any(), any(), any()) }
        verify(exactly = 0) { publishServiceMock.publishWithPostV2(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `GET with secret key falls back to V2 when signature overhead pushes path over limit`() {
        // Same message as above: path+query = 52 + 32,700 = 32,752 bytes (at the V1 limit).
        // With secret key, SignatureInterceptor adds 78 bytes (timestamp + signature),
        // so effective length = 32,752 + 78 = 32,830 > 32,752 → must fall back to V2 POST.
        val message = "x".repeat(32_700)

        val publish = PublishEndpoint(
            pubnub = pubNubWithSecretKey,
            message = message,
            channel = TEST_CHANNEL,
            usePost = false,
        )
        publish.sync()

        // V1 GET is called to build the request and measure path length, then V2 POST is used
        verify(exactly = 1) { publishServiceMock.publish(any(), any(), any(), any(), any()) }
        verify(exactly = 1) { publishServiceMock.publishWithPostV2(any(), any(), any(), any(), any()) }
    }

    // ================================
    // Oversize Rejection Tests
    // ================================

    @Test
    fun `POST with body exceeding 2MB rejects client-side before any network call`() {
        // PUBLISH_V2_MAX_POST_BODY_BYTES = 2 * 1024 * 1024 = 2,097,152 bytes.
        // JSON serialization adds 2 bytes for quotes around a string,
        // so a string of 2,097,151 chars produces 2,097,153 bytes → exceeds the limit.
        val message = "x".repeat(2_097_151)

        val publish = PublishEndpoint(
            pubnub = pubNubNoSecretKey,
            message = message,
            channel = TEST_CHANNEL,
            usePost = true,
        )

        try {
            publish.sync()
            fail("Expected PubNubException to be thrown")
        } catch (e: PubNubException) {
            assertEquals(413, e.statusCode)
            assertTrue(e.errorMessage!!.contains("Request Entity Too Large"))
        }

        // No network calls should have been made — rejection is purely client-side
        verify(exactly = 0) { publishServiceMock.publishWithPost(any(), any(), any(), any(), any()) }
        verify(exactly = 0) { publishServiceMock.publishWithPostV2(any(), any(), any(), any(), any()) }
        verify(exactly = 0) { publishServiceMock.publish(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `GET with body exceeding 2MB rejects client-side before any network call`() {
        // Same validation applies when usePost=false and the message is large enough
        // to trigger V2 fallback — the 2MB limit check still fires before the V2 call.
        val message = "x".repeat(2_097_151)

        val publish = PublishEndpoint(
            pubnub = pubNubNoSecretKey,
            message = message,
            channel = TEST_CHANNEL,
            usePost = false,
        )

        try {
            publish.sync()
            fail("Expected PubNubException to be thrown")
        } catch (e: PubNubException) {
            assertEquals(413, e.statusCode)
            assertTrue(e.errorMessage!!.contains("Request Entity Too Large"))
        }

        // V1 GET is called to measure path (which exceeds V1 limit), then oversize check fires
        // before V2 POST is invoked
        verify(exactly = 1) { publishServiceMock.publish(any(), any(), any(), any(), any()) }
        verify(exactly = 0) { publishServiceMock.publishWithPostV2(any(), any(), any(), any(), any()) }
        verify(exactly = 0) { publishServiceMock.publishWithPost(any(), any(), any(), any(), any()) }
    }

    // ================================
    // Constants Verification Tests
    // ================================

    @Test
    fun `POST body limit constant is 32768`() {
        assertThat(PUBLISH_V1_MAX_POST_BODY_BYTES, `is`(32_768))
    }

    @Test
    fun `GET path limit constant is 32752`() {
        assertThat(PUBLISH_V1_MAX_GET_PATH_BYTES, `is`(32_752))
    }

    @Test
    fun `Signature overhead constant is 78`() {
        assertThat(SIGNATURE_QUERY_PARAMS_OVERHEAD_BYTES, `is`(78))
    }
}
