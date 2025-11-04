package com.pubnub.api.legacy.endpoints.files

import com.pubnub.api.PubNubException
import com.pubnub.api.legacy.endpoints.remoteaction.TestRemoteAction
import com.pubnub.api.models.consumer.files.PNBaseFile
import com.pubnub.api.models.consumer.files.PNFileUploadResult
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.files.GenerateUploadUrlEndpoint
import com.pubnub.internal.endpoints.files.PublishFileMessageEndpoint
import com.pubnub.internal.endpoints.files.SendFileEndpoint
import com.pubnub.internal.endpoints.files.UploadFileEndpoint
import com.pubnub.internal.models.server.files.FileUploadRequestDetails
import com.pubnub.internal.models.server.files.FormField
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.jupiter.api.Test
import java.io.InputStream
import java.time.Instant
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

class SendFileTest : TestsWithFiles {
    private val channel = "channel"
    private val generateUploadUrlFactory: GenerateUploadUrlEndpoint.Factory = mockk {}
    private val publishFileMessageFactory: PublishFileMessageEndpoint.Factory = mockk {}
    private val sendFileToS3Factory: UploadFileEndpoint.Factory = mockk {}

    @Test
    fun sync_happyPath() {
        // given
        val fileUploadRequestDetails = generateUploadUrlProperResponse()
        val expectedResponse = pnFileUploadResult()
        val publishFileMessageResult = PNPublishFileMessageResult(expectedResponse.timetoken)
        every { generateUploadUrlFactory.create(any(), any()) } returns
            TestRemoteAction.successful(
                fileUploadRequestDetails,
            )

        every { sendFileToS3Factory.create(any(), any(), any()) } returns AlwaysSuccessfulUploadFile.create(getPubNubMock())
        val publishFileMessage: PublishFileMessageEndpoint =
            AlwaysSuccessfulPublishFileMessage.create(publishFileMessageResult, getPubNubMock())
        every { publishFileMessageFactory.create(any(), any(), any(), any(), any(), any(), any(), any()) } returns publishFileMessage

        // when
        val result: PNFileUploadResult? =
            inputStream().use { inputStream ->
                sendFile(channel, fileName(), inputStream).sync()
            }

        // then
        Assert.assertEquals(expectedResponse, result)
    }

    @Test
    fun async_happyPath() {
        // given
        val countDownLatch = CountDownLatch(1)
        val fileUploadRequestDetails = generateUploadUrlProperResponse()
        val expectedResponse = pnFileUploadResult()
        val publishFileMessageResult = PNPublishFileMessageResult(expectedResponse.timetoken)
        every { generateUploadUrlFactory.create(any(), any()) } returns
            TestRemoteAction.successful(
                fileUploadRequestDetails,
            )

        every { sendFileToS3Factory.create(any(), any(), any()) } returns AlwaysSuccessfulUploadFile.create(getPubNubMock())
        val publishFileMessage: PublishFileMessageEndpoint =
            AlwaysSuccessfulPublishFileMessage.create(publishFileMessageResult, getPubNubMock())
        every { publishFileMessageFactory.create(any(), any(), any(), any(), any(), any(), any(), any()) } returns publishFileMessage
        inputStream().use { inputStream ->
            sendFile(
                channel,
                fileName(),
                inputStream,
            ).async { result ->
                result.onSuccess {
                    Assert.assertEquals(expectedResponse, it)
                    countDownLatch.countDown()
                }
            }
        }
        Assert.assertTrue(countDownLatch.await(1, TimeUnit.SECONDS))
    }

    // NOTE: Retry tests removed because fileMessagePublishRetryLimit parameter was removed
    // All file upload steps (Generate URL, S3 Upload, Publish Message) now use EndpointCore's
    // automatic retry mechanism instead of explicit RetryingRemoteAction wrapper.
    // Retry behavior is tested at the EndpointCore level.

    private fun generateUploadUrlProperResponse(): FileUploadRequestDetails {
        return FileUploadRequestDetails(
            200,
            PNBaseFile("id", "name"),
            "url",
            "GET",
            Instant.now().plusSeconds(50).toString(),
            FormField("key", "value"),
            emptyList(),
        )
    }

    private fun pnFileUploadResult(): PNFileUploadResult {
        return PNFileUploadResult(1337L, 200, PNBaseFile("id", "name"))
    }

    private fun sendFile(
        channel: String,
        fileName: String,
        inputStream: InputStream,
        numberOfRetries: Int = 1,
    ): SendFileEndpoint {
        return SendFileEndpoint(
            channel = channel,
            fileName = fileName,
            customMessageType = "myCustomMessageType01a",
            inputStream = inputStream,
            generateUploadUrlFactory = generateUploadUrlFactory,
            publishFileMessageFactory = publishFileMessageFactory,
            sendFileToS3Factory = sendFileToS3Factory,
            executorService = Executors.newSingleThreadExecutor(),
        )
    }

    private fun getPubNubMock(): PubNubImpl {
        val mockConfig = mockk<PNConfiguration>()
        val mockPubNub = mockk<PubNubImpl>()
        val retryConfiguration = RetryConfiguration.None
        every { mockConfig.retryConfiguration } returns retryConfiguration
        every { mockPubNub.configuration } returns mockConfig
        every { mockPubNub.logConfig } returns mockk()
        return mockPubNub
    }

    internal class AlwaysSuccessfulPublishFileMessage(
        private val result: PNPublishFileMessageResult,
        pubNub: PubNubImpl,
    ) :
        PublishFileMessageEndpoint(
                channel = "channel",
                fileName = "fileName",
                fileId = "fileId",
                customMessageType = "myCustomType",
                pubNub = pubNub,
            ) {
        @Throws(PubNubException::class)
        override fun sync(): PNPublishFileMessageResult {
            return result
        }

        override fun async(callback: Consumer<Result<PNPublishFileMessageResult>>) {
            callback.accept(Result.success(result))
        }

        companion object {
            fun create(
                result: PNPublishFileMessageResult,
                pubNub: PubNubImpl,
            ): PublishFileMessageEndpoint {
                return AlwaysSuccessfulPublishFileMessage(result, pubNub)
            }
        }
    }

    internal class AlwaysSuccessfulUploadFile {
        companion object {
            fun create(pubNub: PubNubImpl): UploadFileEndpoint {
                val mock = mockk<UploadFileEndpoint>(relaxed = true)
                every { mock.sync() } returns Unit
                every { mock.async(any()) } answers {
                    val callback = firstArg<Consumer<Result<Unit>>>()
                    callback.accept(Result.success(Unit))
                }
                return mock
            }
        }
    }
}
