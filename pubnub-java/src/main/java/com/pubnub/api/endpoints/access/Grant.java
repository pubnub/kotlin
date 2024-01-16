package com.pubnub.api.endpoints.access;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerGrantResult;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Accessors(chain = true, fluent = true)
public class Grant extends Endpoint<PNAccessManagerGrantResult> {

    @Setter
    private boolean read;
    @Setter
    private boolean write;
    @Setter
    private boolean manage;
    @Setter
    private boolean delete;
    @Setter
    private boolean get;
    @Setter
    private boolean update;
    @Setter
    private boolean join;
    @Setter
    private int ttl = -1;

    @Setter
    private List<String> authKeys = new ArrayList<>();
    @Setter
    private List<String> channels = new ArrayList<>();
    @Setter
    private List<String> channelGroups = new ArrayList<>();
    @Setter
    private List<String> uuids = Collections.emptyList();

    public Grant(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected ExtendedRemoteAction<PNAccessManagerGrantResult> createAction() {
        return new MappingRemoteAction<>(
                pubnub.grant(
                        read,
                        write,
                        manage,
                        delete,
                        ttl,
                        authKeys,
                        channels,
                        channelGroups
                ), PNAccessManagerGrantResult::from
        );
    }
}
