package com.pubnub.internal.java.endpoints.objects_api.channel;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.objects_api.channel.GetAllChannelsMetadata;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.channel.PNGetAllChannelsMetadataResult;
import com.pubnub.api.java.models.consumer.objects_api.channel.PNGetAllChannelsMetadataResultConverter;
import com.pubnub.api.models.consumer.objects.PNKey;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class GetAllChannelsMetadataImpl
        extends DelegatingEndpoint<PNChannelMetadataArrayResult, PNGetAllChannelsMetadataResult> implements GetAllChannelsMetadata {

    private Integer limit = null;
    private PNPage page;
    private String filter;
    private Collection<PNSortKey> sort = Collections.emptyList();
    private boolean includeTotalCount;
    private boolean includeCustom;

    public GetAllChannelsMetadataImpl(final PubNub pubnubInstance) {
        super(pubnubInstance);
    }

    @Override
    @NotNull
    protected Endpoint<PNChannelMetadataArrayResult> createRemoteAction() {
        return pubnub.getAllChannelMetadata(
                limit,
                page,
                filter,
                toInternal(sort),
                includeTotalCount,
                includeCustom
        );
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNGetAllChannelsMetadataResult> mapResult(@NotNull ExtendedRemoteAction<PNChannelMetadataArrayResult> action) {
        return new MappingRemoteAction<>(action, PNGetAllChannelsMetadataResultConverter::from);
    }

    public static Collection<? extends com.pubnub.api.models.consumer.objects.PNSortKey<PNKey>> toInternal(Collection<PNSortKey> sort) {
        List<com.pubnub.api.models.consumer.objects.PNSortKey<PNKey>> list = new ArrayList<>(sort.size());
        for (PNSortKey pnSortKey : sort) {
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
            if (pnSortKey.getDir().equals(PNSortKey.Dir.ASC)) {
                list.add(new com.pubnub.api.models.consumer.objects.PNSortKey.PNAsc<>(key));
            } else {
                list.add(new com.pubnub.api.models.consumer.objects.PNSortKey.PNDesc<>(key));
            }
        }
        return list;
    }
}
