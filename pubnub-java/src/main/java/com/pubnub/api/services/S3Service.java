package com.pubnub.api.services;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface S3Service {

    @POST
    Call<Void> upload(@Url String url,
                      @Body MultipartBody form);
}
