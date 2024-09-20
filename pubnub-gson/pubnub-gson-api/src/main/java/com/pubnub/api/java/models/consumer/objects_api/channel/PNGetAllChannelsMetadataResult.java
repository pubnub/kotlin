package com.pubnub.api.java.models.consumer.objects_api.channel;

import com.pubnub.api.java.models.consumer.objects_api.EntityArrayEnvelope;
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Getter
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor
@ToString(callSuper = true)
public class PNGetAllChannelsMetadataResult extends EntityArrayEnvelope<PNChannelMetadata> {
    private PNGetAllChannelsMetadataResult(Integer status, Integer totalCount, String prev, String next, List<PNChannelMetadata> data) {
        this.status = status;
        this.totalCount = totalCount;
        this.prev = prev;
        this.next = next;
        this.data = data;
    }

    @Nullable
    public static PNGetAllChannelsMetadataResult from(@Nullable PNChannelMetadataArrayResult result) {
        if (result == null) {
            return null;
        }
        return new PNGetAllChannelsMetadataResult(
                result.getStatus(),
                result.getTotalCount(),
                result.getPrev() != null ? result.getPrev().getPageHash() : null,
                result.getNext() != null ? result.getNext().getPageHash() : null,
                PNChannelMetadata.from(result.getData())
        );
    }
}
