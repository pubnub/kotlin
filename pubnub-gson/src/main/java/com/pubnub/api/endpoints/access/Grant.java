package com.pubnub.api.endpoints.access;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerGrantResult;

public interface Grant extends Endpoint<PNAccessManagerGrantResult> {
    Grant read(boolean read);

    Grant write(boolean write);

    Grant manage(boolean manage);

    Grant delete(boolean delete);

    Grant get(boolean get);

    Grant update(boolean update);

    Grant join(boolean join);

    Grant ttl(int ttl);

    Grant authKeys(java.util.List<String> authKeys);

    Grant channels(java.util.List<String> channels);

    Grant channelGroups(java.util.List<String> channelGroups);

    Grant uuids(java.util.List<String> uuids);
}
