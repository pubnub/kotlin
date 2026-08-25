package com.pubnub.api.java.endpoints.access.builder;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGroupGrant;
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult;

import java.util.List;

/**
 * Shared base for the grant-token builders. Carries the setters common to every grant path — {@code ttl},
 * {@code meta}, and the {@code channels}/{@code channelGroups} PubSub buckets — so a fluent chain can start with any
 * of them and still resolve into either the modern flat-list path (the neutral {@link GrantTokenBuilder}) or the
 * legacy (App Context v2 UUID) path ({@link GrantTokenObjectsBuilder}).
 *
 * <p>Each concrete builder covariantly narrows these setters' return type to itself, keeping the chain fluent within a
 * single path.
 */
public interface AbstractGrantTokenBuilder extends Endpoint<PNGrantTokenResult> {

    AbstractGrantTokenBuilder ttl(Integer ttl);

    AbstractGrantTokenBuilder meta(Object meta);

    AbstractGrantTokenBuilder channels(List<ChannelGrant> channels);

    AbstractGrantTokenBuilder channelGroups(List<ChannelGroupGrant> channelGroups);
}