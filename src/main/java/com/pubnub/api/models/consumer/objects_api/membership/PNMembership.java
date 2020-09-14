package com.pubnub.api.models.consumer.objects_api.membership;

import com.google.gson.annotations.JsonAdapter;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadata;
import com.pubnub.api.models.consumer.objects_api.util.CustomPayloadJsonInterceptor;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class PNMembership {
    @NonNull
    private PNChannelMetadata channel;

    @JsonAdapter(CustomPayloadJsonInterceptor.class)
    protected Object custom;

    protected String updated;
    protected String eTag;
}
