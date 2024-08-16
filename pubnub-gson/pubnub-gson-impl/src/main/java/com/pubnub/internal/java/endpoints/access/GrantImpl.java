package com.pubnub.internal.java.endpoints.access;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.access.Grant;
import com.pubnub.api.java.models.consumer.access_manager.PNAccessManagerGrantResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Setter
@Accessors(chain = true, fluent = true)
public class GrantImpl extends DelegatingEndpoint<com.pubnub.api.models.consumer.access_manager.PNAccessManagerGrantResult, PNAccessManagerGrantResult> implements Grant {

    private boolean read;
    private boolean write;
    private boolean manage;
    private boolean delete;
    private boolean get;
    private boolean update;
    private boolean join;
    private int ttl = -1;

    private List<String> authKeys = new ArrayList<>();
    private List<String> channels = new ArrayList<>();
    private List<String> channelGroups = new ArrayList<>();
    private List<String> uuids = Collections.emptyList();

    public GrantImpl(PubNub pubnub) {
        super(pubnub);
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.access_manager.PNAccessManagerGrantResult> createAction() {
        return pubnub.grant(
                read,
                write,
                manage,
                delete,
                get,
                update,
                join,
                ttl,
                authKeys,
                channels,
                channelGroups,
                uuids
        );
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNAccessManagerGrantResult> mapResult(@NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.access_manager.PNAccessManagerGrantResult> action) {
        return new MappingRemoteAction<>(action, com.pubnub.api.java.models.consumer.access_manager.PNAccessManagerGrantResult::from);
    }
}
