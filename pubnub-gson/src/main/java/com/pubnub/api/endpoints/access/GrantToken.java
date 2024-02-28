package com.pubnub.api.endpoints.access;

import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant;
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult;
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant;
import com.pubnub.internal.CorePubNubClient;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Accessors(chain = true, fluent = true)
public class GrantToken extends DelegatingEndpoint<PNGrantTokenResult> {

    @Setter
    private Integer ttl;
    @Setter
    private Object meta;
    @Setter
    private String authorizedUUID;
    @Setter
    private List<ChannelGrant> channels = Collections.emptyList();
    @Setter
    private List<ChannelGroupGrant> channelGroups = Collections.emptyList();
    @Setter
    private List<UUIDGrant> uuids = Collections.emptyList();

    public GrantToken(CorePubNubClient pubnub) {
        super(pubnub);
    }

    @Override
    protected com.pubnub.internal.CoreEndpoint<?, PNGrantTokenResult> createAction() {
        return pubnub.grantToken(
                ttl,
                meta,
                authorizedUUID,
                toInternalChannels(channels),
                toInternalChannelGroups(channelGroups),
                toInternalUuids(uuids)
        );
    }

    private List<? extends com.pubnub.internal.models.consumer.access_manager.v3.ChannelGrant> toInternalChannels(List<ChannelGrant> channels) {
        ArrayList<com.pubnub.internal.models.consumer.access_manager.v3.ChannelGrant> list = new ArrayList<>(channels.size());
        for (ChannelGrant channel : channels) {
            list.add(toInternal(channel));
        }
        return list;
    }

    private List<? extends com.pubnub.internal.models.consumer.access_manager.v3.ChannelGroupGrant> toInternalChannelGroups(List<ChannelGroupGrant> channels) {
        ArrayList<com.pubnub.internal.models.consumer.access_manager.v3.ChannelGroupGrant> list = new ArrayList<>(channels.size());
        for (ChannelGroupGrant channel : channels) {
            list.add(toInternal(channel));
        }
        return list;
    }

    private List<? extends com.pubnub.internal.models.consumer.access_manager.v3.UUIDGrant> toInternalUuids(List<UUIDGrant> uuids) {
        ArrayList<com.pubnub.internal.models.consumer.access_manager.v3.UUIDGrant> list = new ArrayList<>(uuids.size());
        for (UUIDGrant uuid : uuids) {
            list.add(toInternal(uuid));
        }
        return list;
    }

    static com.pubnub.internal.models.consumer.access_manager.v3.ChannelGrant toInternal(ChannelGrant grant) {
        if (grant.isPatternResource()) {
            return com.pubnub.internal.models.consumer.access_manager.v3.ChannelGrant.Companion.pattern(
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
            return com.pubnub.internal.models.consumer.access_manager.v3.ChannelGrant.Companion.name(
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

    static com.pubnub.internal.models.consumer.access_manager.v3.ChannelGroupGrant toInternal(ChannelGroupGrant grant) {
        if (grant.isPatternResource()) {
            return com.pubnub.internal.models.consumer.access_manager.v3.ChannelGroupGrant.Companion.pattern(
                    grant.getId(),
                    grant.isRead(),
                    grant.isManage()
            );
        } else {
            return com.pubnub.internal.models.consumer.access_manager.v3.ChannelGroupGrant.Companion.id(
                    grant.getId(),
                    grant.isRead(),
                    grant.isManage()
            );
        }
    }

    static com.pubnub.internal.models.consumer.access_manager.v3.UUIDGrant toInternal(UUIDGrant grant) {
        if (grant.isPatternResource()) {
            return com.pubnub.internal.models.consumer.access_manager.v3.UUIDGrant.Companion.pattern(
                    grant.getId(),
                    grant.isGet(),
                    grant.isUpdate(),
                    grant.isDelete()
            );
        } else {
            return com.pubnub.internal.models.consumer.access_manager.v3.UUIDGrant.Companion.id(
                    grant.getId(),
                    grant.isGet(),
                    grant.isUpdate(),
                    grant.isDelete()
            );
        }
    }
}
