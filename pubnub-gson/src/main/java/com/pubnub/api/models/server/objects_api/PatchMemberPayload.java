package com.pubnub.api.models.server.objects_api;

import com.pubnub.api.models.consumer.objects_api.member.PNUUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Collection;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public
class PatchMemberPayload {
    private Collection<PNUUID> set;
    private Collection<PNUUID> delete;
}
