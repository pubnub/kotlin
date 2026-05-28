package com.pubnub.api

import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.PubNubUtil
import com.pubnub.internal.endpoints.pubsub.PublishEndpoint.Companion.SIGNATURE_QUERY_PARAMS_OVERHEAD_BYTES
import com.pubnub.internal.v2.PNConfigurationImpl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Request
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.lessThanOrEqualTo
import org.junit.Test

class PubNubUtilTest {
    @Test
    fun signedSHA256ReturnPrecomputedSingleLine() {
        assertThat(PubNubUtil.signSHA256("key", "data"), Matchers.`is`("UDH-PZicbRU3oBP6bnOdojRj_a7DtwE32Cjjas4iG9A="))
    }

    /**
     * Pins SIGNATURE_QUERY_PARAMS_OVERHEAD_BYTES (used by PublishEndpoint to budget the V1 GET
     * path limit) against the actual bytes added by PubNubUtil.signRequest. If the signer's
     * wire format ever changes (timestamp width, signature prefix, base64 padding, extra query
     * params), this test fails so the constant is updated before oversized GET paths slip past
     * client-side validation.
     *
     * Asserts <= so a future shrink still passes; only growth — the dangerous case — trips it.
     */
    @Test
    fun `signRequest query overhead does not exceed SIGNATURE_QUERY_PARAMS_OVERHEAD_BYTES`() {
        val configuration = PNConfigurationImpl(
            userId = UserId(PubNubImpl.generateUUID()),
            subscribeKey = "subKey",
            publishKey = "pubKey",
            secretKey = "secretKey",
        )
        val baseRequest = Request.Builder()
            .url("https://ps.pndsn.com/publish/pubKey/subKey/0/testChannel/0/%22hi%22?seqn=1".toHttpUrl())
            .build()
        val baseQueryLength = baseRequest.url.encodedQuery!!.length

        // Use a 10-digit Unix timestamp — the upper bound until 2038.
        val signedRequest = PubNubUtil.signRequest(baseRequest, configuration, timestamp = 1_999_999_999)
        val signedQueryLength = signedRequest.url.encodedQuery!!.length

        val actualOverhead = signedQueryLength - baseQueryLength
        assertThat(actualOverhead, lessThanOrEqualTo(SIGNATURE_QUERY_PARAMS_OVERHEAD_BYTES))
    }
}
