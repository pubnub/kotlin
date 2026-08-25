package com.pubnub.internal.java.endpoints.access;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.java.builder.PubNubErrorBuilder;
import com.pubnub.api.java.endpoints.access.GrantToken;
import com.pubnub.api.java.endpoints.access.builder.GrantTokenBuilder;
import com.pubnub.api.java.endpoints.access.builder.GrantTokenObjectsBuilder;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGroupGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.DataSyncGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.TokenGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.UUIDGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.UserGrant;
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrantType;
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult;
import com.pubnub.internal.java.endpoints.PassthroughEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class GrantTokenImpl extends PassthroughEndpoint<PNGrantTokenResult> implements GrantToken, GrantTokenBuilder, GrantTokenObjectsBuilder {

    private Integer ttl;
    private Object meta;
    private String authorizedUUID;
    private List<ChannelGrant> channels = Collections.emptyList();
    private List<ChannelGroupGrant> channelGroups = Collections.emptyList();
    private List<UUIDGrant> uuids = Collections.emptyList();
    private List<TokenGrant> grants = Collections.emptyList();

    public GrantTokenImpl(PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (this.ttl == null) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_TTL_MISSING);
        }
        // The legacy `uuids` bucket and the modern flat-list `grants` cannot be combined in a single grant — they
        // enter through mutually-exclusive builder paths. Reject the mix explicitly instead of silently dropping one
        // of them during routing.
        if (!uuids.isEmpty() && !grants.isEmpty()) {
            throw new PubNubException("The legacy `uuids` grants can not be combined with `grants`.");
        }
    }

    @Override
    @NotNull
    protected Endpoint<PNGrantTokenResult> createRemoteAction() {
        // The modern flat-list `grants` and the legacy `uuids` bucket are served by two distinct kotlin overloads.
        // Route through the new overload whenever any `grants` entry is present; otherwise fall back to the legacy
        // `uuids` overload. The authorized principal maps to the same wire field in both. `channels`/`channelGroups`
        // supplied via the shared setters are additive with the flat list (prepended so their order is preserved).
        if (!grants.isEmpty()) {
            final UserId authorizedUserId;
            try {
                authorizedUserId = authorizedUUID == null ? null : new UserId(authorizedUUID);
            } catch (PubNubException e) {
                throw new RuntimeException(e);
            }
            return pubnub.grantToken(
                    ttl,
                    authorizedUserId,
                    meta,
                    toInternalGrants(grants)
            );
        }
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
    public GrantTokenBuilder authorizedUserId(UserId userId) {
        authorizedUUID(userId.getValue());
        return this;
    }

    private List<? extends com.pubnub.api.models.consumer.access_manager.v3.TokenGrant> toInternalGrants(List<TokenGrant> grants) {
        // Prepend the channels/channelGroups supplied via the shared setters so they are additive with the flat list.
        ArrayList<com.pubnub.api.models.consumer.access_manager.v3.TokenGrant> list =
                new ArrayList<>(channels.size() + channelGroups.size() + grants.size());
        for (ChannelGrant channel : channels) {
            list.add(toInternal(channel));
        }
        for (ChannelGroupGrant channelGroup : channelGroups) {
            list.add(toInternal(channelGroup));
        }
        for (TokenGrant grant : grants) {
            if (grant instanceof ChannelGrant) {
                list.add(toInternal((ChannelGrant) grant));
            } else if (grant instanceof ChannelGroupGrant) {
                list.add(toInternal((ChannelGroupGrant) grant));
            } else if (grant instanceof UserGrant) {
                list.add(toInternal((UserGrant) grant));
            } else if (grant instanceof DataSyncGrant) {
                list.add(toInternal((DataSyncGrant) grant));
            } else {
                throw new IllegalArgumentException("Unsupported TokenGrant type: " + grant.getClass().getName());
            }
        }
        return list;
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
                    grant.isUpdate(),
                    grant.getProjection()
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
                    grant.isUpdate(),
                    grant.getProjection()
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

    static DataSyncGrantType toInternal(DataSyncGrant grant) {
        boolean pattern = grant.isPatternResource();
        com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant factory =
                com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant.INSTANCE;
        String projection = grant.getProjection();
        switch (grant.getNamespace()) {
            case DataSyncGrant.DATASYNC_ENTITIES:
                return pattern
                        ? factory.entityPattern(grant.getId(), grant.isGet(), grant.isCreate(), grant.isUpdate(), grant.isDelete(), projection)
                        : factory.entity(grant.getId(), grant.isGet(), grant.isCreate(), grant.isUpdate(), grant.isDelete(), projection);
            case DataSyncGrant.DATASYNC_RELATIONSHIPS:
                return pattern
                        ? factory.relationshipPattern(grant.getId(), grant.isGet(), grant.isCreate(), grant.isUpdate(), grant.isDelete(), projection)
                        : factory.relationship(grant.getId(), grant.isGet(), grant.isCreate(), grant.isUpdate(), grant.isDelete(), projection);
            case DataSyncGrant.DATASYNC_MEMBERSHIPS:
                return pattern
                        ? factory.membershipPattern(grant.getId(), grant.isGet(), grant.isCreate(), grant.isUpdate(), grant.isDelete(), projection)
                        : factory.membership(grant.getId(), grant.isGet(), grant.isCreate(), grant.isUpdate(), grant.isDelete(), projection);
            default:
                throw new IllegalArgumentException("unknown datasync namespace: " + grant.getNamespace());
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

    static com.pubnub.api.models.consumer.access_manager.v3.UserGrant toInternal(UserGrant grant) {
        if (grant.isPatternResource()) {
            return com.pubnub.api.models.consumer.access_manager.v3.UserGrant.Companion.pattern(
                    grant.getId(),
                    grant.isGet(),
                    grant.isUpdate(),
                    grant.isDelete(),
                    grant.isCreate(),
                    grant.getProjection()
            );
        } else {
            return com.pubnub.api.models.consumer.access_manager.v3.UserGrant.Companion.id(
                    grant.getId(),
                    grant.isGet(),
                    grant.isUpdate(),
                    grant.isDelete(),
                    grant.isCreate(),
                    grant.getProjection()
            );
        }
    }
}
