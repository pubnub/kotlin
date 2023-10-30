package com.pubnub.api.endpoints.objects_api.channel;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.api.models.consumer.objects_api.channel.PNGetAllChannelsMetadataResult;
import com.pubnub.internal.BasePubNub.PubNubImpl;
import com.pubnub.internal.models.consumer.objects.PNKey;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Accessors(chain = true, fluent = true)
public class GetAllChannelsMetadata
        extends Endpoint<PNGetAllChannelsMetadataResult> {

    @Setter
    private Integer limit = null;
    @Setter
    private PNPage page;
    @Setter
    private String filter;
    @Setter
    private Collection<PNSortKey> sort = Collections.emptyList();
    @Setter
    private boolean includeTotalCount;
    @Setter
    private boolean includeCustom;

    public GetAllChannelsMetadata(final PubNubImpl pubnubInstance) {
        super(pubnubInstance);
    }

    @Override
    protected ExtendedRemoteAction<PNGetAllChannelsMetadataResult> createAction() {
        return new MappingRemoteAction<>(pubnub.getAllChannelMetadata(
                limit,
                page,
                filter,
                toInternal(sort),
                includeTotalCount,
                includeCustom
        ), PNGetAllChannelsMetadataResult::from);
    }

    public static Collection<? extends com.pubnub.internal.models.consumer.objects.PNSortKey<PNKey>> toInternal(Collection<PNSortKey> sort) {
        List<com.pubnub.internal.models.consumer.objects.PNSortKey<PNKey>> list = new ArrayList<>(sort.size());
        for (PNSortKey pnSortKey : sort) {
            PNKey key = null;
            switch (pnSortKey.getKey()) {
                case ID:
                    key = PNKey.ID;
                    break;
                case NAME:
                    key = PNKey.NAME;
                    break;
                case UPDATED:
                    key = PNKey.UPDATED;
                    break;
            }
            if (pnSortKey.getDir().equals(PNSortKey.Dir.ASC)) {
                list.add(new com.pubnub.internal.models.consumer.objects.PNSortKey.PNAsc<>(key));
            } else {
                list.add(new com.pubnub.internal.models.consumer.objects.PNSortKey.PNDesc<>(key));
            }
        }
        return list;
    }
}
