package com.pubnub.api.java.endpoints.access.builder;

import com.pubnub.api.UserId;
import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.access_manager.sum.SpacePermissions;
import com.pubnub.api.java.models.consumer.access_manager.sum.UserPermissions;
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult;

import java.util.List;

public interface GrantTokenEntitiesBuilder extends Endpoint<PNGrantTokenResult> {

    GrantTokenEntitiesBuilder meta(Object meta);

    GrantTokenEntitiesBuilder spacesPermissions(List<SpacePermissions> spacesPermissions);

    GrantTokenEntitiesBuilder usersPermissions(List<UserPermissions> usersPermissions);

    GrantTokenEntitiesBuilder authorizedUserId(UserId userId);

}
