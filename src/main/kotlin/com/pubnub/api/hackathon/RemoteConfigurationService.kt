package com.pubnub.api.hackathon

import com.google.gson.annotations.SerializedName
import com.pubnub.api.PNConfiguration
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.enums.PNReconnectionPolicy
import com.pubnub.api.managers.MapperManager
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

fun retrieveConfigurationFromServer(mapperManager: MapperManager, configuration: PNConfiguration): RemoteConfiguration {
    return RemoteConfiguration()
    val httpClient: OkHttpClient = OkHttpClient.Builder().build()

    val remoteConfigClient = Retrofit.Builder()
        .callFactory(httpClient)
        .baseUrl("https://whatever")
        .addConverterFactory(mapperManager.converterFactory)
        .build()
        .create(RemoteConfigurationService::class.java)

    return remoteConfigClient.retrieve(configuration.subscribeKey, mapOf()).execute().body()!!
}

data class RemoteConfiguration(
    @SerializedName("sdk_origin")
    val origin: String? = null,
    val secure: Boolean? = null,
    val logVerbosity: PNLogVerbosity? = null,
    val reconnectionPolicy: PNReconnectionPolicy? = null,
    val presenceTimeout: Int? = null,
    val heartbeatInterval: Int? = null,
    val subscribeTimeout: Int? = null,
    val connectTimeout: Int? = null,
    val nonSubscribeRequestTimeout: Int? = null,
    val maximumReconnectionRetries: Int? = null,
    val maximumConnections: Int? = null,
    val requestMessageCountThreshold: Int? = null,
    val canaryPercentage: Int? = null,
    @SerializedName("sdk_batching_max_window")
    val batchingMaxWindow: Long? = null,
    @SerializedName("sdk_batching_number_messages")
    val batchingNumberMessages: Int? = null
)

interface RemoteConfigurationService {
    @GET("/v2/auth/grant/sub-key/{subKey}")
    fun retrieve(
        @Path("subKey") subKey: String,
        @QueryMap options: Map<String, String>
    ): Call<RemoteConfiguration>
}