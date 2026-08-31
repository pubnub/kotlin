package com.pubnub.internal.java.endpoints.datasync.channel;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.channel.GetChannels;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncClassLevel;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncPage;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncSortField;
import com.pubnub.api.java.models.consumer.datasync.channel.PNDataSyncChannelConverter;
import com.pubnub.api.java.models.consumer.datasync.channel.PNDataSyncGetChannelsResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Accessors(chain = true, fluent = true)
public class GetChannelsImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.channel.PNDataSyncGetChannelsResult, PNDataSyncGetChannelsResult>
        implements GetChannels {

    @Setter
    @Nullable
    private String className;

    @Setter
    @Nullable
    private Integer classVersion;

    @Setter
    @Nullable
    private PNDataSyncClassLevel classLevel;

    @Setter
    @Nullable
    private String filter;

    @Setter
    @Nullable
    private String filterAdvanced;

    @Setter
    @Nullable
    private List<PNDataSyncSortField> sort;

    @Setter
    @Nullable
    private Integer limit;

    @Setter
    @Nullable
    private String cursor;

    public GetChannelsImpl(final PubNub pubnubInstance) {
        super(pubnubInstance);
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNDataSyncGetChannelsResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.channel.PNDataSyncGetChannelsResult> action) {
        return new MappingRemoteAction<>(action, result -> {
            com.pubnub.api.models.consumer.datasync.PNDataSyncPage kNext = result.getNext();
            return new PNDataSyncGetChannelsResult(
                    result.getStatus(),
                    result.getData().stream().map(PNDataSyncChannelConverter::from).collect(Collectors.toList()),
                    new PNDataSyncPage(kNext.getCursor(), kNext.getHasNext(), kNext.getLimit())
            );
        });
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.channel.PNDataSyncGetChannelsResult> createRemoteAction() {
        final List<com.pubnub.api.models.consumer.datasync.PNDataSyncSortField> mappedSort =
                sort == null
                        ? Collections.emptyList()
                        : sort.stream()
                                .map(s -> new com.pubnub.api.models.consumer.datasync.PNDataSyncSortField(
                                        s.getProperty(),
                                        s.isAscending()
                                ))
                                .collect(Collectors.toList());
        return pubnub.getDataSync().getChannels(
                className,
                classVersion,
                classLevel == null ? null : com.pubnub.api.models.consumer.datasync.PNDataSyncClassLevel.valueOf(classLevel.name()),
                filter,
                filterAdvanced,
                mappedSort,
                limit,
                cursor
        );
    }
}