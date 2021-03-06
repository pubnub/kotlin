package com.pubnub.api.legacy.endpoints.files

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.files.GenerateUploadUrl
import com.pubnub.api.endpoints.files.PublishFileMessage
import com.pubnub.api.endpoints.files.SendFile
import com.pubnub.api.endpoints.files.UploadFile
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.legacy.endpoints.files.TestsWithFiles.Companion.folder
import com.pubnub.api.legacy.endpoints.remoteaction.TestRemoteAction
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.files.PNBaseFile
import com.pubnub.api.models.consumer.files.PNFileUploadResult
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult
import com.pubnub.api.models.server.files.FileUploadRequestDetails
import com.pubnub.api.models.server.files.FormField
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.time.Instant
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class SendFileTest : TestsWithFiles {
    private val channel = "channel"
    private val filename = "test.txt"
    private val generateUploadUrlFactory: GenerateUploadUrl.Factory = mockk {}
    private val publishFileMessageFactory: PublishFileMessage.Factory = mockk {}
    private val sendFileToS3Factory: UploadFile.Factory = mockk {}

    @get:Rule
    override val temporaryFolder: TemporaryFolder
        get() = folder

    @Test
    @Throws(PubNubException::class, IOException::class)
    fun sync_happyPath() {
        // given
        val file = getTemporaryFile(filename)
        val fileUploadRequestDetails = generateUploadUrlProperResponse()
        val expectedResponse = pnFileUploadResult()
        val publishFileMessageResult = PNPublishFileMessageResult(expectedResponse.timetoken)
        every { generateUploadUrlFactory.create(any(), any()) } returns TestRemoteAction.successful(
            fileUploadRequestDetails
        )

        every { sendFileToS3Factory.create(any(), any(), any(), any()) } returns TestRemoteAction.successful(Unit)
        val publishFileMessage: PublishFileMessage =
            AlwaysSuccessfulPublishFileMessage.create(
                publishFileMessageResult
            )
        every { publishFileMessageFactory.create(any(), any(), any()) } returns publishFileMessage

        // when
        val result: PNFileUploadResult? = FileInputStream(file).use { fileInputStream ->
            sendFile(channel, file.name, fileInputStream).sync()
        }

        // then
        Assert.assertEquals(expectedResponse, result)
    }

    @Test
    @Throws(InterruptedException::class, IOException::class)
    fun async_happyPath() {
        // given
        val countDownLatch = CountDownLatch(1)
        val file = getTemporaryFile(filename)
        val fileUploadRequestDetails = generateUploadUrlProperResponse()
        val expectedResponse = pnFileUploadResult()
        val publishFileMessageResult = PNPublishFileMessageResult(expectedResponse.timetoken)
        every { generateUploadUrlFactory.create(any(), any()) } returns TestRemoteAction.successful(
            fileUploadRequestDetails
        )

        every { sendFileToS3Factory.create(any(), any(), any(), any()) } returns TestRemoteAction.successful(Unit)
        val publishFileMessage: PublishFileMessage =
            AlwaysSuccessfulPublishFileMessage.create(
                publishFileMessageResult
            )
        every { publishFileMessageFactory.create(any(), any(), any()) } returns publishFileMessage
        FileInputStream(file).use { fileInputStream ->
            sendFile(
                channel,
                file.name,
                fileInputStream
            ).async { result: PNFileUploadResult?, _: PNStatus? ->
                Assert.assertEquals(expectedResponse, result)
                countDownLatch.countDown()
            }
        }
        Assert.assertTrue(countDownLatch.await(1, TimeUnit.SECONDS))
    }

    @Test
    @Throws(InterruptedException::class, IOException::class)
    fun async_publishFileMessageRetry() {
        // given
        val countDownLatch = CountDownLatch(1)
        val file = getTemporaryFile(filename)
        val fileUploadRequestDetails = generateUploadUrlProperResponse()
        val expectedResponse = pnFileUploadResult()
        val publishFileMessageResult = PNPublishFileMessageResult(expectedResponse.timetoken)
        val numberOfRetries = 5
        every { generateUploadUrlFactory.create(any(), any()) } returns TestRemoteAction.successful(
            fileUploadRequestDetails
        )

        every { sendFileToS3Factory.create(any(), any(), any(), any()) } returns TestRemoteAction.successful(Unit)
        val publishFileMessage: PublishFileMessage = spyk(
            FailingPublishFileMessage.create(
                publishFileMessageResult,
                numberOfRetries - 1
            )
        )
        every { publishFileMessageFactory.create(any(), any(), any()) } returns publishFileMessage
        FileInputStream(file).use { fileInputStream ->
            sendFile(
                channel,
                file.name,
                fileInputStream,
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
    @Throws(InterruptedException::class, IOException::class, PubNubException::class)
    fun sync_publishFileMessageRetry() {
        // given
        val file = getTemporaryFile(filename)
        val fileUploadRequestDetails = generateUploadUrlProperResponse()
        val expectedResponse = pnFileUploadResult()
        val publishFileMessageResult = PNPublishFileMessageResult(expectedResponse.timetoken)
        val numberOfRetries = 5
        every { generateUploadUrlFactory.create(any(), any()) } returns
            TestRemoteAction.successful(
                fileUploadRequestDetails
            )

        every { sendFileToS3Factory.create(any(), any(), any(), any()) } returns TestRemoteAction.successful(Unit)
        val publishFileMessage: PublishFileMessage = spyk(
            FailingPublishFileMessage.create(
                publishFileMessageResult,
                numberOfRetries - 1
            )
        )
        every { publishFileMessageFactory.create(any(), any(), any()) } returns publishFileMessage

        // when
        val result: PNFileUploadResult? = FileInputStream(file).use { fileInputStream ->
            sendFile(channel, file.name, fileInputStream, numberOfRetries).sync()
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
        numberOfRetries: Int = 1
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

    internal class FailingPublishFileMessage(
        private val result: PNPublishFileMessageResult,
        private val numberOfFailsBeforeSuccess: Int
    ) :
        PublishFileMessage(
            channel = "channel",
            fileName = "fileName",
            fileId = "fileId",
            pubNub = mockk {}
        ) {
        private val numberOfFails = AtomicInteger(0)
        override fun async(callback: (result: PNPublishFileMessageResult?, status: PNStatus) -> Unit) {
            if (numberOfFails.getAndAdd(1) < numberOfFailsBeforeSuccess) {
                callback(
                    null,
                    PNStatus(
                        category = PNStatusCategory.PNBadRequestCategory,
                        error = true,
                        operation = PNOperationType.FileOperation
                    )
                )
            } else {
                callback(
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
            fun create(result: PNPublishFileMessageResult, numberOfFailsBeforeSuccess: Int): PublishFileMessage {
                return FailingPublishFileMessage(
                    result,
                    numberOfFailsBeforeSuccess
                )
            }
        }
    }

    internal class AlwaysSuccessfulPublishFileMessage(
        private val result: PNPublishFileMessageResult
    ) :
        PublishFileMessage(
            channel = "channel",
            fileName = "fileName",
            fileId = "fileId",
            pubNub = mockk {}
        ) {
        @Throws(PubNubException::class)
        override fun sync(): PNPublishFileMessageResult? {
            return result
        }

        override fun async(callback: (result: PNPublishFileMessageResult?, status: PNStatus) -> Unit) {
            callback(
                result,
                PNStatus(
                    category = PNStatusCategory.PNAcknowledgmentCategory,
                    error = false,
                    operation = PNOperationType.FileOperation
                )
            )
        }

        companion object {
            fun create(result: PNPublishFileMessageResult): PublishFileMessage {
                return AlwaysSuccessfulPublishFileMessage(
                    result
                )
            }
        }
    }
}
