package com.pubnub.internal.endpoints.objects_api.channel;

import com.pubnub.api.endpoints.objects_api.channel.GetAllChannelsMetadata;
import com.pubnub.api.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.api.models.consumer.objects_api.channel.PNGetAllChannelsMetadataResult;
import com.pubnub.internal.InternalPubNubClient;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import com.pubnub.internal.models.consumer.objects.PNKey;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class GetAllChannelsMetadataImpl
        extends DelegatingEndpoint<PNGetAllChannelsMetadataResult> implements GetAllChannelsMetadata {

    private Integer limit = null;
    private PNPage page;
    private String filter;
    private Collection<PNSortKey> sort = Collections.emptyList();
    private boolean includeTotalCount;
    private boolean includeCustom;

    public GetAllChannelsMetadataImpl(final InternalPubNubClient pubnubInstance) {
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

    public static Collection<? extends com.pubnub.internal.models.consumer.objects.PNSortKey<PNKey>> toInternal(Collection<com.pubnub.api.endpoints.objects_api.utils.PNSortKey> sort) {
        List<com.pubnub.internal.models.consumer.objects.PNSortKey<PNKey>> list = new ArrayList<>(sort.size());
        for (com.pubnub.api.endpoints.objects_api.utils.PNSortKey pnSortKey : sort) {
            PNKey key;
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
                default:
                    throw new IllegalStateException("Should never happen");
            }
            if (pnSortKey.getDir().equals(com.pubnub.api.endpoints.objects_api.utils.PNSortKey.Dir.ASC)) {
                list.add(new com.pubnub.internal.models.consumer.objects.PNSortKey.PNAsc<>(key));
            } else {
                list.add(new com.pubnub.internal.models.consumer.objects.PNSortKey.PNDesc<>(key));
            }
        }
        return list;
    }
}
