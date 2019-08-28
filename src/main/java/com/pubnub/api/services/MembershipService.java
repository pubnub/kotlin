package com.pubnub.api.services;


import com.pubnub.api.models.consumer.objects_api.membership.PNMembership;
import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.Map;

public interface MembershipService {

    @GET("v1/objects/{subKey}/users/{userId}/spaces")
    Call<EntityArrayEnvelope<PNMembership>> getMemberships(@Path("subKey") String subKey,
                                                           @Path("userId") String userId,
                                                           @QueryMap(encoded = true) Map<String, String> options);

    @PATCH("v1/objects/{subKey}/users/{userId}/spaces")
    @Headers("Content-Type: application/json; charset=UTF-8")
    Call<EntityArrayEnvelope<PNMembership>> manageMemberships(@Path("subKey") String subKey,
                                                              @Path("userId") String userId,
                                                              @Body Object body,
                                                              @QueryMap(encoded = true) Map<String, String> options);
}
