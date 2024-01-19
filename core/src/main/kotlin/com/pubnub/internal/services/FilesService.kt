package com.pubnub.internal.services

import com.pubnub.internal.models.server.files.GenerateUploadUrlPayload
import com.pubnub.internal.models.server.files.GeneratedUploadUrlResponse
import com.pubnub.internal.models.server.files.ListFilesResult
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface FilesService {
    @POST("/v1/files/{subKey}/channels/{channel}/generate-upload-url")
    fun generateUploadUrl(
        @Path("subKey") subKey: String,
        @Path("channel") channel: String,
        @Body body: GenerateUploadUrlPayload,
        @QueryMap options: Map<String, String>
    ): Call<GeneratedUploadUrlResponse>

    @GET("/v1/files/publish-file/{pubKey}/{subKey}/0/{channel}/0/{message}")
    fun notifyAboutFileUpload(
        @Path("pubKey") pubKey: String,
        @Path("subKey") subKey: String,
        @Path("channel") channel: String,
        @Path(value = "message") message: String,
        @QueryMap options: Map<String, String>
    ): Call<List<Any>>

    @GET("/v1/files/{subKey}/channels/{channel}/files")
    fun listFiles(
        @Path("subKey") subKey: String,
        @Path("channel") channel: String,
        @QueryMap options: Map<String, String>
    ): Call<ListFilesResult>

    @GET(GET_FILE_URL)
    fun downloadFile(
        @Path("subKey") subKey: String,
        @Path("channel") channel: String,
        @Path("fileId") fileId: String,
        @Path("fileName") fileName: String,
        @QueryMap options: Map<String, String>
    ): Call<ResponseBody>

    @DELETE("/v1/files/{subKey}/channels/{channel}/files/{fileId}/{fileName}")
    fun deleteFile(
        @Path("subKey") subKey: String,
        @Path("channel") channel: String,
        @Path("fileId") fileId: String,
        @Path("fileName") fileName: String,
        @QueryMap options: Map<String, String>
    ): Call<Unit>

    companion object {
        const val GET_FILE_URL = "/v1/files/{subKey}/channels/{channel}/files/{fileId}/{fileName}"
    }
}
