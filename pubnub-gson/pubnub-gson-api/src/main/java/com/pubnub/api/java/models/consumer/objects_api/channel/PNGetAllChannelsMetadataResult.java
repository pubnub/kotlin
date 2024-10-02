package com.pubnub.api.java.models.consumer.objects_api.channel;

import com.pubnub.api.java.models.consumer.objects_api.EntityArrayEnvelope;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor
@ToString(callSuper = true)
public class PNGetAllChannelsMetadataResult extends EntityArrayEnvelope<PNChannelMetadata> {
    protected PNGetAllChannelsMetadataResult(Integer status, Integer totalCount, String prev, String next, List<PNChannelMetadata> data) {
        this.status = status;
        this.totalCount = totalCount;
        this.prev = prev;
        this.next = next;
        this.data = data;
    }
}
