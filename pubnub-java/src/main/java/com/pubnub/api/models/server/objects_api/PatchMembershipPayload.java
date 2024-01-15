package com.pubnub.api.models.server.objects_api;

import com.pubnub.api.models.consumer.objects_api.membership.PNChannelMembership;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Collection;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class PatchMembershipPayload {
    private Collection<PNChannelMembership> set;
    private Collection<PNChannelMembership> delete;
}
