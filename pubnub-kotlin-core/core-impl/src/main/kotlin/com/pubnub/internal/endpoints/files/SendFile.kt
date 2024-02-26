package com.pubnub.internal.endpoints.files

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.endpoints.remoteaction.ComposableRemoteAction
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.map
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.files.PNBaseFile
import com.pubnub.api.models.consumer.files.PNFileUploadResult
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.internal.InternalPubNubClient
import com.pubnub.internal.crypto.cryptor.InputStreamSeparator
import com.pubnub.internal.endpoints.remoteaction.RetryingRemoteAction
import com.pubnub.internal.models.server.files.FileUploadRequestDetails
import java.io.InputStream
import java.net.HttpURLConnection
import java.util.concurrent.ExecutorService
import java.util.concurrent.atomic.AtomicReference
import java.util.function.Consumer

/**
 * @see [InternalPubNubClient.sendFile]
 */
class SendFile internal constructor(
    private val channel: String,
    private val fileName: String,
    inputStream: InputStream,
    private val message: Any? = null,
    private val meta: Any? = null,
    private val ttl: Int? = null,
    private val shouldStore: Boolean? = null,
    private val fileMessagePublishRetryLimit: Int,
    private val executorService: ExecutorService,
    generateUploadUrlFactory: GenerateUploadUrl.Factory,
    publishFileMessageFactory: PublishFileMessage.Factory,
    sendFileToS3Factory: UploadFile.Factory,
    cryptoModule: CryptoModule? = null,
) : ISendFile {
    private val sendFileMultistepAction: ExtendedRemoteAction<PNFileUploadResult> =
        sendFileComposedActions(
            generateUploadUrlFactory,
            publishFileMessageFactory,
            sendFileToS3Factory,
            inputStream,
            cryptoModule,
        )

    @Throws(PubNubException::class)
    override fun sync(): PNFileUploadResult {
        validate()
        return sendFileMultistepAction.sync()
    }

    override fun async(callback: Consumer<Result<PNFileUploadResult>>) {
        executorService.execute {
            try {
                validate()
                sendFileMultistepAction.async(callback)
            } catch (ex: PubNubException) {
                callback.accept(
                    Result.failure(ex),
                )
            }
        }
    }

    @Throws(PubNubException::class)
    private fun validate() {
        if (channel.isEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
        if (fileName.isEmpty()) {
            throw PubNubException(PubNubError.INVALID_ARGUMENTS).copy(errorMessage = "File name cannot be null nor empty")
        }
    }

    private fun sendFileComposedActions(
        generateUploadUrlFactory: GenerateUploadUrl.Factory,
        publishFileMessageFactory: PublishFileMessage.Factory,
        sendFileToS3Factory: UploadFile.Factory,
        inputStream: InputStream,
        cryptoModule: CryptoModule?,
    ): ExtendedRemoteAction<PNFileUploadResult> {
        val result = AtomicReference<FileUploadRequestDetails>()

        val content =
            cryptoModule?.encryptStream(InputStreamSeparator(inputStream))?.use {
                it.readBytes()
            } ?: inputStream.readBytes()
        return ComposableRemoteAction.firstDo(generateUploadUrlFactory.create(channel, fileName)) // generateUrl
            .then { res ->
                result.set(res)
                sendFileToS3Factory.create(fileName, content, res) // upload to s3
            }.checkpoint().then {
                val details = result.get()
                RetryingRemoteAction.autoRetry(
                    publishFileMessageFactory.create(
                        channel = channel,
                        fileName = details.data.name,
                        fileId = details.data.id,
                        message = message,
                        meta = meta,
                        ttl = ttl,
                        shouldStore = shouldStore,
                    ),
                    fileMessagePublishRetryLimit,
                    executorService,
                ) // publish file message
            }.map { mapPublishFileMessageToFileUpload(result.get(), it) }
    }

    private fun mapPublishFileMessageToFileUpload(
        requestDetails: FileUploadRequestDetails,
        res: PNPublishFileMessageResult,
    ) = PNFileUploadResult(
        res.timetoken,
        HttpURLConnection.HTTP_OK,
        PNBaseFile(requestDetails.data.id, requestDetails.data.name),
    )

    override fun retry() {
        sendFileMultistepAction.retry()
    }

    override fun silentCancel() {
        sendFileMultistepAction.silentCancel()
    }

    override fun operationType(): PNOperationType {
        return PNOperationType.FileOperation
    }
}
