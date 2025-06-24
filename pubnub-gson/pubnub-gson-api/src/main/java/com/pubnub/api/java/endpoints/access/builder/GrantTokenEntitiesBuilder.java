package com.pubnub.api.java.endpoints.access.builder;

import com.pubnub.api.UserId;
import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.access_manager.sum.SpacePermissions;
import com.pubnub.api.java.models.consumer.access_manager.sum.UserPermissions;
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult;

import java.util.List;

public interface GrantTokenEntitiesBuilder extends Endpoint<PNGrantTokenResult> {

    @Deprecated
    GrantTokenEntitiesBuilder meta(Object meta);

    @Deprecated
    GrantTokenEntitiesBuilder spacesPermissions(List<SpacePermissions> spacesPermissions);

    @Deprecated
    GrantTokenEntitiesBuilder usersPermissions(List<UserPermissions> usersPermissions);

    @Deprecated
    GrantTokenEntitiesBuilder authorizedUserId(UserId userId);
}
