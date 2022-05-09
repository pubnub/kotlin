package com.pubnub.entities.objects.endpoint.channel

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.Endpoint
import com.pubnub.entities.objects.endpoint.internal.ReturningCustom
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.managers.RetrofitManager
import com.pubnub.entities.objects.space.PNSpace
import com.pubnub.entities.objects.space.PNSpaceResult
import com.pubnub.api.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

/**
 * @see [PubNub.getSpace]
 */
class GetSpace internal constructor(
    pubnub: PubNub,
    private val channel: String,
    private val withCustom: ReturningCustom
) : Endpoint<EntityEnvelope<PNSpace>, PNSpaceResult>(
    pubnub,
    pubnub.telemetryManager,
    pubnub.retrofitManager,
    pubnub.tokenManager
) {
    override fun getAffectedChannels(): List<String?> = listOf(channel)

    override fun getAffectedChannelGroups(): List<String?> = listOf()

    override fun validateParams() {
    }

    override fun doWork(queryParams: MutableMap<String, String>): Call<EntityEnvelope<PNSpace>> {
        val params = queryParams + withCustom.createIncludeQueryParams()
        return pubnub.retrofitManager.objectsService().getChannelMetadata(
            subKey = pubnub.configuration.subscribeKey,
            channel = channel,
            options = params
        )
    }

    override fun getOperationType(): PNOperationType? = PNOperationType.PNGetChannelMembersOperation

    override fun isAuthRequired(): Boolean = true

    override fun createResponse(input: Response<EntityEnvelope<PNSpace>>): PNSpaceResult? {
        return input.body()?.let {
            PNSpaceResult(
                status = it.status,
                data = it.data
            )
        }
    }
}

interface ObjectsService {
    @GET("v2/objects/{subKey}/channels/{channel}")
    fun getChannelMetadata(
        @Path("subKey") subKey: String,
        @Path("channel") channel: String,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf()
    ): Call<EntityEnvelope<PNSpace>>
}

object Lazy {
    private lateinit var retrofit: Retrofit
    private val objectsService: ObjectsService by lazy {
        retrofit.create()
    }

    fun objectsService(retrofit: Retrofit): ObjectsService {
        this.retrofit = retrofit
        return objectsService
    }
}

fun RetrofitManager.objectsService(): ObjectsService {
    return Lazy.objectsService(this.transactionInstance)
}
