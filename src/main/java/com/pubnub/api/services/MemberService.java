package com.pubnub.api.services;


import com.pubnub.api.models.consumer.objects_api.member.PNMember;
import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.Map;

public interface MemberService {

    @GET("v1/objects/{subKey}/spaces/{spaceId}/users")
    Call<EntityArrayEnvelope<PNMember>> getMembers(@Path("subKey") String subKey,
                                                   @Path("spaceId") String spaceId,
                                                   @QueryMap(encoded = true) Map<String, String> options);

    @PATCH("v1/objects/{subKey}/spaces/{spaceId}/users")
    @Headers("Content-Type: application/json; charset=UTF-8")
    Call<EntityArrayEnvelope<PNMember>> manageMembers(@Path("subKey") String subKey,
                                                      @Path("spaceId") String spaceId,
                                                      @Body Object body,
                                                      @QueryMap(encoded = true) Map<String, String> options);
}
