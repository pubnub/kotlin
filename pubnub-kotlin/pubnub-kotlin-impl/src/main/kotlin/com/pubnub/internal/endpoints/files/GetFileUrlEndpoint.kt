package com.pubnub.internal.endpoints.files

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.files.GetFileUrl
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.files.PNFileUrlResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.PubNubUtil
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.function.Consumer

/**
 * @see [PubNubImpl.getFileUrl]
 */
class GetFileUrlEndpoint(
    private val channel: String,
    private val fileName: String,
    private val fileId: String,
    pubNub: PubNubImpl,
) : EndpointCore<ResponseBody, PNFileUrlResult>(pubNub), GetFileUrl {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)
    private lateinit var cachedCallback: Consumer<Result<PNFileUrlResult>>
    private val executorService: ExecutorService = retrofitManager.getTransactionClientExecutorService() ?: Executors.newSingleThreadExecutor()

    @Throws(PubNubException::class)
    override fun validateParams() {
        super.validateParams()
        if (channel.isEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
    }

    // this code shouldn't call any outside services to return an url, but on the
    // other hand the code that adds signature uses okhttp request. To produce
    // properly constructed url the code creates a request which isn't executed
    @Throws(PubNubException::class)
    override fun sync(): PNFileUrlResult {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "channel" to channel,
                        "fileName" to fileName,
                        "fileId" to fileId
                    ),
                    operation = this::class.simpleName
                ),
                details = "GetFileUrl API call",
            )
        )

        return try {
            val baseParams: Map<String, String> = createBaseParams()
            val call: Call<ResponseBody> =
                retrofitManager.filesService
                    .downloadFile(
                        configuration.subscribeKey,
                        channel,
                        fileId,
                        fileName,
                        baseParams,
                    )
            val signedRequest =
                PubNubUtil.signRequest(
                    call.request(),
                    pubnub.configuration,
                    PubNubImpl.timestamp(),
                )
            PNFileUrlResult(signedRequest.url.toString())
        } catch (e: Exception) {
            throw PubNubException(errorMessage = e.message)
        }
    }

    // Endpoint class uses OkHttp's asynchronous calls to achieve async. Since in this class
    // the code shouldn't call any outside endpoints it's necessary to achieve asynchronous
    // behavior using other means than OkHttp. That's why the code is using executorService
    // to asynchronously call sync()
    override fun async(callback: Consumer<Result<PNFileUrlResult>>) {
        cachedCallback = callback
        executorService.execute {
            try {
                val res: PNFileUrlResult = sync()
                callback.accept(
                    Result.success(res),
                )
            } catch (ex: PubNubException) {
                callback.accept(Result.failure(ex))
            }
        }
    }

    @Throws(PubNubException::class)
    override fun doWork(queryParams: HashMap<String, String>): Call<ResponseBody> {
        throw PubNubException(PubNubError.INTERNAL_ERROR)
    }

    @Throws(PubNubException::class)
    override fun createResponse(input: Response<ResponseBody>): PNFileUrlResult {
        throw PubNubException(PubNubError.INTERNAL_ERROR)
    }

    override fun retry() {
        async(cachedCallback)
    }

    override fun getAffectedChannels() = listOf(channel)

    override fun getAffectedChannelGroups(): List<String> = listOf()

    override fun operationType(): PNOperationType = PNOperationType.FileOperation

    override fun isAuthRequired(): Boolean = true

    override fun isSubKeyRequired(): Boolean = true

    override fun isPubKeyRequired(): Boolean = false

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.FILE_PERSISTENCE
}
