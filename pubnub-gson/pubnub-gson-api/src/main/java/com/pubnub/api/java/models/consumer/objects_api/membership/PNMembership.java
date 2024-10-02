package com.pubnub.api.java.models.consumer.objects_api.membership;

import com.pubnub.api.java.models.consumer.objects_api.channel.PNChannelMetadata;
import com.pubnub.api.utils.PatchValue;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Data
@Accessors(chain = true)
public class PNMembership {
    public PNMembership(@NotNull PNChannelMetadata channel) {
        this.channel = channel;
    }
    @NonNull
    private PNChannelMetadata channel;
    private PatchValue<@Nullable Map<String, Object>> custom;
    private String uuid;
    private String updated;
    private String eTag;
    private PatchValue<@Nullable String> status;
}
