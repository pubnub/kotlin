package com.pubnub.api.legacy.endpoints.files

import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.internal.PubNubImpl
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Test

class GetFileUrlTest {
    private val channel = "channel"
    private val fileName = "fileName"
    private val fileId = "fileId"
    private val defaultQueryParams: Set<String> = HashSet(listOf("pnsdk", "requestid", "uuid"))

    @Test
    @Throws(PubNubException::class)
    fun noAdditionalQueryParamsWhenNotSecretNorAuth() {
        // given
        val pubnub = PubNubImpl(config().build())

        // when
        val url =
            pubnub.getFileUrl(
                channel = channel,
                fileName = fileName,
                fileId = fileId,
            ).sync().url

        // then
        val queryParamNames = queryParameterNames(url)
        queryParamNames.removeAll(defaultQueryParams)
        Assert.assertEquals(emptySet<Any>(), queryParamNames)
    }

    @Test
    @Throws(PubNubException::class)
    fun signatureAndTimestampQueryParamsAreSetWhenSecret() {
        // given
        val pubnub = PubNubImpl(withSecret(config()).build())

        // when
        val url =
            pubnub.getFileUrl(
                channel = channel,
                fileName = fileName,
                fileId = fileId,
            ).sync().url

        // then
        val queryParamNames = queryParameterNames(url)
        queryParamNames.removeAll(defaultQueryParams)
        assertThat<Collection<String>>(queryParamNames, Matchers.containsInAnyOrder("signature", "timestamp"))
    }

    @Test
    @Throws(PubNubException::class)
    fun authQueryParamIsSetWhenAuth() {
        // given
        val pubnub = PubNubImpl(withAuth(config()).build())

        // when
        val url =
            pubnub.getFileUrl(
                channel = channel,
                fileName = fileName,
                fileId = fileId,
            ).sync().url

        // then
        val queryParamNames = queryParameterNames(url)
        queryParamNames.removeAll(defaultQueryParams)
        assertThat<Collection<String>>(queryParamNames, Matchers.containsInAnyOrder("auth"))
    }

    @Test
    @Throws(PubNubException::class)
    fun signatureAndTimestampAndAuthQueryParamsAreSetWhenSecretAndAuth() {
        // given
        val pubnub = PubNubImpl(withSecret(withAuth(config())).build())

        // when
        val url =
            pubnub.getFileUrl(
                channel = channel,
                fileName = fileName,
                fileId = fileId,
            ).sync().url

        // then
        println(url)
        val queryParamNames = queryParameterNames(url)
        queryParamNames.removeAll(defaultQueryParams)
        assertThat<Collection<String>>(
            queryParamNames,
            Matchers.containsInAnyOrder("auth", "signature", "timestamp"),
        )
    }

    private fun config(): PNConfiguration.Builder {
        val config = PNConfiguration.builder(userId = UserId(PubNubImpl.generateUUID()), "")
        config.publishKey = "pk"
        config.subscribeKey = "sk"
        config.retryConfiguration = RetryConfiguration.Linear(delayInSec = 4, maxRetryNumber = 3)
        return config
    }

    private fun withSecret(config: PNConfiguration.Builder): PNConfiguration.Builder {
        config.secretKey = "secK"
        return config
    }

    private fun withAuth(config: PNConfiguration.Builder): PNConfiguration.Builder {
        config.authKey = "ak"
        return config
    }

    private fun queryParameterNames(url: String): MutableCollection<String> {
        return HashSet(url.toHttpUrl().queryParameterNames)
    }
}
