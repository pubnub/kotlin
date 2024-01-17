package com.pubnub.api.endpoints.access;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.MappingEndpoint;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerGrantResult;
import com.pubnub.internal.DelegatingEndpoint;
import com.pubnub.internal.PubNub;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Setter
@Accessors(chain = true, fluent = true)
public class Grant extends ValidatingEndpoint<PNAccessManagerGrantResult> {

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

    public Grant(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected @NotNull Endpoint<PNAccessManagerGrantResult> createAction() {
        return new MappingEndpoint<>(
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
