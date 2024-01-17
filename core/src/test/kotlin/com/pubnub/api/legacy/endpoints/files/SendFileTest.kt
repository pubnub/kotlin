package com.pubnub.api.legacy.endpoints.files

import com.pubnub.api.PubNubException
import com.pubnub.api.callbacks.PNCallback
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.legacy.endpoints.remoteaction.TestRemoteAction
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.files.PNBaseFile
import com.pubnub.api.models.consumer.files.PNFileUploadResult
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.internal.PNConfiguration
import com.pubnub.internal.PubNub
import com.pubnub.internal.endpoints.files.GenerateUploadUrl
import com.pubnub.internal.endpoints.files.PublishFileMessage
import com.pubnub.internal.endpoints.files.SendFile
import com.pubnub.internal.endpoints.files.UploadFile
import com.pubnub.internal.models.server.files.FileUploadRequestDetails
import com.pubnub.internal.models.server.files.FormField
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert
import org.junit.jupiter.api.Test
import java.io.InputStream
import java.time.Instant
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class SendFileTest : TestsWithFiles {
    private val channel = "channel"
    private val generateUploadUrlFactory: GenerateUploadUrl.Factory = mockk {}
    private val publishFileMessageFactory: PublishFileMessage.Factory = mockk {}
    private val sendFileToS3Factory: UploadFile.Factory = mockk {}

    @Test
    fun sync_happyPath() {
        // given
        val fileUploadRequestDetails = generateUploadUrlProperResponse()
        val expectedResponse = pnFileUploadResult()
        val publishFileMessageResult = PNPublishFileMessageResult(expectedResponse.timetoken)
        every { generateUploadUrlFactory.create(any(), any()) } returns TestRemoteAction.successful(
            fileUploadRequestDetails
        )

        every { sendFileToS3Factory.create(any(), any(), any()) } returns TestRemoteAction.successful(Unit)
        val publishFileMessage: PublishFileMessage =
            AlwaysSuccessfulPublishFileMessage.create(publishFileMessageResult, getPubNubMock())
        every { publishFileMessageFactory.create(any(), any(), any()) } returns publishFileMessage

        // when
        val result: PNFileUploadResult? = inputStream().use { inputStream ->
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
        every { generateUploadUrlFactory.create(any(), any()) } returns TestRemoteAction.successful(
            fileUploadRequestDetails
        )

        every { sendFileToS3Factory.create(any(), any(), any()) } returns TestRemoteAction.successful(Unit)
        val publishFileMessage: PublishFileMessage =
            AlwaysSuccessfulPublishFileMessage.create(publishFileMessageResult, getPubNubMock())
        every { publishFileMessageFactory.create(any(), any(), any()) } returns publishFileMessage
        inputStream().use { inputStream ->
            sendFile(
                channel,
                fileName(),
                inputStream
            ).async { result: PNFileUploadResult?, _: PNStatus? ->
                Assert.assertEquals(expectedResponse, result)
                countDownLatch.countDown()
            }
        }
        Assert.assertTrue(countDownLatch.await(1, TimeUnit.SECONDS))
    }

    @Test
    fun async_publishFileMessageRetry() {
        // given
        val countDownLatch = CountDownLatch(1)
        val fileUploadRequestDetails = generateUploadUrlProperResponse()
        val expectedResponse = pnFileUploadResult()
        val publishFileMessageResult = PNPublishFileMessageResult(expectedResponse.timetoken)
        val numberOfRetries = 5
        every { generateUploadUrlFactory.create(any(), any()) } returns TestRemoteAction.successful(
            fileUploadRequestDetails
        )

        every { sendFileToS3Factory.create(any(), any(), any()) } returns TestRemoteAction.successful(Unit)
        val publishFileMessage: PublishFileMessage = spyk(
            FailingPublishFileMessage.create(
                publishFileMessageResult,
                numberOfRetries - 1,
                getPubNubMock()
            )
        )
        every { publishFileMessageFactory.create(any(), any(), any()) } returns publishFileMessage
        inputStream().use { inputStream ->
            sendFile(
                channel,
                fileName(),
                inputStream,
                numberOfRetries
            ).async { result: PNFileUploadResult?, _: PNStatus? ->
                Assert.assertEquals(expectedResponse, result)
                countDownLatch.countDown()
            }
        }

        // then
        Assert.assertTrue(countDownLatch.await(1, TimeUnit.SECONDS))
        verify(exactly = numberOfRetries) { publishFileMessage.async(any()) }
    }

    @Test
    fun sync_publishFileMessageRetry() {
        // given
        val fileUploadRequestDetails = generateUploadUrlProperResponse()
        val expectedResponse = pnFileUploadResult()
        val publishFileMessageResult = PNPublishFileMessageResult(expectedResponse.timetoken)
        val numberOfRetries = 5
        every { generateUploadUrlFactory.create(any(), any()) } returns
            TestRemoteAction.successful(
                fileUploadRequestDetails
            )

        every { sendFileToS3Factory.create(any(), any(), any()) } returns TestRemoteAction.successful(Unit)
        val publishFileMessage: PublishFileMessage = spyk(
            FailingPublishFileMessage.create(
                publishFileMessageResult,
                numberOfRetries - 1,
                getPubNubMock()
            )
        )
        every { publishFileMessageFactory.create(any(), any(), any()) } returns publishFileMessage

        // when
        val result: PNFileUploadResult? = inputStream().use { inputStream ->
            sendFile(channel, fileName(), inputStream, numberOfRetries).sync()
        }

        // then
        Assert.assertEquals(expectedResponse, result)
        verify(exactly = numberOfRetries) { publishFileMessage.sync() }
    }

    private fun generateUploadUrlProperResponse(): FileUploadRequestDetails {
        return FileUploadRequestDetails(
            200,
            PNBaseFile("id", "name"),
            "url",
            "GET",
            Instant.now().plusSeconds(50).toString(),
            FormField("key", "value"), emptyList()
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
    ): SendFile {
        return SendFile(
            channel = channel,
            fileName = fileName,
            inputStream = inputStream,
            generateUploadUrlFactory = generateUploadUrlFactory,
            publishFileMessageFactory = publishFileMessageFactory,
            sendFileToS3Factory = sendFileToS3Factory,
            executorService = Executors.newSingleThreadExecutor(),
            fileMessagePublishRetryLimit = numberOfRetries
        )
    }
    private fun getPubNubMock(): PubNub {
        val mockConfig = mockk<PNConfiguration>()
        val mockPubNub = mockk<PubNub>()
        val retryConfiguration = RetryConfiguration.None
        every { mockPubNub.configuration } returns mockConfig
        every { mockConfig.retryConfiguration } returns retryConfiguration

        return mockPubNub
    }

    internal class FailingPublishFileMessage(
        private val result: PNPublishFileMessageResult,
        private val numberOfFailsBeforeSuccess: Int,
        pubNub: PubNub
    ) :
        PublishFileMessage(
            channel = "channel",
            fileName = "fileName",
            fileId = "fileId",
            pubNub = pubNub
        ) {
        private val numberOfFails = AtomicInteger(0)
        override fun async(callback: PNCallback<PNPublishFileMessageResult>) {
            if (numberOfFails.getAndAdd(1) < numberOfFailsBeforeSuccess) {
                callback.onResponse(
                    null,
                    PNStatus(
                        category = PNStatusCategory.PNBadRequestCategory,
                        error = true,
                        operation = PNOperationType.FileOperation
                    )
                )
            } else {
                callback.onResponse(
                    result,
                    PNStatus(
                        category = PNStatusCategory.PNAcknowledgmentCategory,
                        error = false,
                        operation = PNOperationType.FileOperation
                    )
                )
            }
        }

        @Throws(PubNubException::class)
        override fun sync(): PNPublishFileMessageResult? {
            if (numberOfFails.getAndAdd(1) < numberOfFailsBeforeSuccess) {
                throw PubNubException()
            }
            return result
        }

        companion object {
            fun create(result: PNPublishFileMessageResult, numberOfFailsBeforeSuccess: Int, pubNub: PubNub): PublishFileMessage {
                return FailingPublishFileMessage(
                    result,
                    numberOfFailsBeforeSuccess,
                    pubNub
                )
            }
        }
    }

    internal class AlwaysSuccessfulPublishFileMessage(
        private val result: PNPublishFileMessageResult,
        pubNub: PubNub
    ) :
        PublishFileMessage(
            channel = "channel",
            fileName = "fileName",
            fileId = "fileId",
            pubNub = pubNub

        ) {
        @Throws(PubNubException::class)
        override fun sync(): PNPublishFileMessageResult? {
            return result
        }

        override fun async(callback: PNCallback<PNPublishFileMessageResult>) {
            callback.onResponse(
                result,
                PNStatus(
                    category = PNStatusCategory.PNAcknowledgmentCategory,
                    error = false,
                    operation = PNOperationType.FileOperation
                )
            )
        }

        companion object {
            fun create(result: PNPublishFileMessageResult, pubNub: PubNub): PublishFileMessage {
                return AlwaysSuccessfulPublishFileMessage(result, pubNub)
            }
        }
    }
}
