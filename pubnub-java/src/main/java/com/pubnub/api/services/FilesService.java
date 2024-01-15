package com.pubnub.api.services;

import com.pubnub.api.models.server.files.GenerateUploadUrlPayload;
import com.pubnub.api.models.server.files.GeneratedUploadUrlResponse;
import com.pubnub.api.models.server.files.ListFilesResult;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.List;
import java.util.Map;

public interface FilesService {
    String GET_FILE_URL = "/v1/files/{subKey}/channels/{channel}/files/{fileId}/{fileName}";

    @POST("/v1/files/{subKey}/channels/{channel}/generate-upload-url")
    Call<GeneratedUploadUrlResponse> generateUploadUrl(@Path("subKey") String subKey,
                                                       @Path("channel") String channel,
                                                       @Body GenerateUploadUrlPayload body,
                                                       @QueryMap(encoded = true) Map<String, String> options);

    @GET("/v1/files/publish-file/{pubKey}/{subKey}/0/{channel}/0/{message}")
    Call<List<Object>> notifyAboutFileUpload(@Path("pubKey") String pubKey,
                                             @Path("subKey") String subKey,
                                             @Path("channel") String channel,
                                             @Path(value = "message", encoded = true) String message,
                                             @QueryMap(encoded = true) Map<String, String> options);

    @GET("/v1/files/{subKey}/channels/{channel}/files")
    Call<ListFilesResult> listFiles(@Path("subKey") String subKey,
                                    @Path("channel") String channel,
                                    @QueryMap(encoded = true) Map<String, String> options);

    @GET(GET_FILE_URL)
    Call<ResponseBody> downloadFile(@Path("subKey") String subKey,
                                    @Path("channel") String channel,
                                    @Path("fileId") String fileId,
                                    @Path("fileName") String fileName,
                                    @QueryMap(encoded = true) Map<String, String> options);

    @DELETE("/v1/files/{subKey}/channels/{channel}/files/{fileId}/{fileName}")
    Call<Void> deleteFile(@Path("subKey") String subKey,
                          @Path("channel") String channel,
                          @Path("fileId") String fileId,
                          @Path("fileName") String fileName,
                          @QueryMap(encoded = true) Map<String, String> options);
}
