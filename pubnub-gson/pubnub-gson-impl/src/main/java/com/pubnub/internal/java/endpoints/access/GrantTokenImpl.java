package com.pubnub.internal.java.endpoints.access;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.java.builder.PubNubErrorBuilder;
import com.pubnub.api.java.endpoints.access.GrantToken;
import com.pubnub.api.java.endpoints.access.builder.GrantTokenBuilder;
import com.pubnub.api.java.endpoints.access.builder.GrantTokenEntitiesBuilder;
import com.pubnub.api.java.endpoints.access.builder.GrantTokenObjectsBuilder;
import com.pubnub.api.java.models.consumer.access_manager.sum.SpacePermissions;
import com.pubnub.api.java.models.consumer.access_manager.sum.UserPermissions;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGroupGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.UUIDGrant;
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult;
import com.pubnub.internal.java.endpoints.IdentityMappingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class GrantTokenImpl extends IdentityMappingEndpoint<PNGrantTokenResult> implements GrantToken, GrantTokenBuilder, GrantTokenEntitiesBuilder, GrantTokenObjectsBuilder {

    private Integer ttl;
    private Object meta;
    private String authorizedUUID;
    private List<ChannelGrant> channels = Collections.emptyList();
    private List<ChannelGroupGrant> channelGroups = Collections.emptyList();
    private List<UUIDGrant> uuids = Collections.emptyList();

    public GrantTokenImpl(PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (this.ttl == null) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_TTL_MISSING);
        }
    }

    @Override
    @NotNull
    protected Endpoint<PNGrantTokenResult> createAction() {
        return pubnub.grantToken(
                ttl,
                meta,
                authorizedUUID,
                toInternalChannels(channels),
                toInternalChannelGroups(channelGroups),
                toInternalUuids(uuids)
        );
    }

    @Override
    public GrantTokenEntitiesBuilder spacesPermissions(List<SpacePermissions> spacesPermissions) {
        List<ChannelGrant> channelGrants = new ArrayList<>();
        for (SpacePermissions spacePermission : spacesPermissions) {
            final ChannelGrant channelGrant;
            if (spacePermission.isPatternResource()) {
                channelGrant = ChannelGrant.pattern(spacePermission.getId());
            } else {
                channelGrant = ChannelGrant.name(spacePermission.getId());
            }
            if (spacePermission.isRead()) {
                channelGrant.read();
            }
            if (spacePermission.isWrite()) {
                channelGrant.write();
            }
            if (spacePermission.isManage()) {
                channelGrant.manage();
            }
            if (spacePermission.isDelete()) {
                channelGrant.delete();
            }
            if (spacePermission.isUpdate()) {
                channelGrant.update();
            }
            if (spacePermission.isJoin()) {
                channelGrant.join();
            }
            if (spacePermission.isGet()) {
                channelGrant.get();
            }
            channelGrants.add(channelGrant);
        }

        channels(channelGrants);
        return this;
    }

    @Override
    public GrantTokenEntitiesBuilder usersPermissions(List<UserPermissions> usersPermissions) {
        List<UUIDGrant> uuidsGrants = new ArrayList<>();
        for (UserPermissions userPermissions : usersPermissions) {
            final UUIDGrant channelGrant;
            if (userPermissions.isPatternResource()) {
                channelGrant = UUIDGrant.pattern(userPermissions.getId());
            } else {
                channelGrant = UUIDGrant.id(userPermissions.getId());
            }
            if (userPermissions.isDelete()) {
                channelGrant.delete();
            }
            if (userPermissions.isUpdate()) {
                channelGrant.update();
            }
            if (userPermissions.isGet()) {
                channelGrant.get();
            }
            uuidsGrants.add(channelGrant);
        }

        uuids(uuidsGrants);
        return this;
    }

    @Override
    public GrantTokenEntitiesBuilder authorizedUserId(UserId userId) {
        authorizedUUID(userId.getValue());
        return this;
    }

    private List<? extends com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant> toInternalChannels(List<ChannelGrant> channels) {
        ArrayList<com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant> list = new ArrayList<>(channels.size());
        for (ChannelGrant channel : channels) {
            list.add(toInternal(channel));
        }
        return list;
    }

    private List<? extends com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant> toInternalChannelGroups(List<ChannelGroupGrant> channels) {
        ArrayList<com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant> list = new ArrayList<>(channels.size());
        for (ChannelGroupGrant channel : channels) {
            list.add(toInternal(channel));
        }
        return list;
    }

    private List<? extends com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant> toInternalUuids(List<UUIDGrant> uuids) {
        ArrayList<com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant> list = new ArrayList<>(uuids.size());
        for (UUIDGrant uuid : uuids) {
            list.add(toInternal(uuid));
        }
        return list;
    }

    static com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant toInternal(ChannelGrant grant) {
        if (grant.isPatternResource()) {
            return com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant.Companion.pattern(
                    grant.getId(),
                    grant.isRead(),
                    grant.isWrite(),
                    grant.isManage(),
                    grant.isDelete(),
                    grant.isCreate(),
                    grant.isGet(),
                    grant.isJoin(),
                    grant.isUpdate()
            );
        } else {
            return com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant.Companion.name(
                    grant.getId(),
                    grant.isRead(),
                    grant.isWrite(),
                    grant.isManage(),
                    grant.isDelete(),
                    grant.isCreate(),
                    grant.isGet(),
                    grant.isJoin(),
                    grant.isUpdate()
            );
        }
    }

    static com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant toInternal(ChannelGroupGrant grant) {
        if (grant.isPatternResource()) {
            return com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant.Companion.pattern(
                    grant.getId(),
                    grant.isRead(),
                    grant.isManage()
            );
        } else {
            return com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant.Companion.id(
                    grant.getId(),
                    grant.isRead(),
                    grant.isManage()
            );
        }
    }

    static com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant toInternal(UUIDGrant grant) {
        if (grant.isPatternResource()) {
            return com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant.Companion.pattern(
                    grant.getId(),
                    grant.isGet(),
                    grant.isUpdate(),
                    grant.isDelete()
            );
        } else {
            return com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant.Companion.id(
                    grant.getId(),
                    grant.isGet(),
                    grant.isUpdate(),
                    grant.isDelete()
            );
        }
    }
}
