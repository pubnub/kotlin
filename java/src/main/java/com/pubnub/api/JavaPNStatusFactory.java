package com.pubnub.api;

import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNErrorData;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.core.CoreException;
import com.pubnub.core.PNStatusFactory;
import com.pubnub.core.Status;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JavaPNStatusFactory implements PNStatusFactory {
    @NotNull
    @Override
    public Status createPNStatus(@NotNull PNStatusCategory category, boolean error, @NotNull PNOperationType operation, @NotNull List<String> affectedChannels, @NotNull List<String> affectedChannelGroups, @Nullable CoreException exception, @Nullable Integer statusCode, @Nullable Boolean tlsEnabled, @Nullable String origin, @Nullable String uuid, @Nullable String authKey, @Nullable PNErrorData errorData) {
        return PNStatus.builder().category(category).error(error).operation(operation).affectedChannels(affectedChannels).affectedChannelGroups(affectedChannelGroups).statusCode(statusCode == null ? 0 : statusCode).tlsEnabled(tlsEnabled != null && tlsEnabled).origin(origin).uuid(uuid).authKey(authKey).errorData(errorData).build();
    }
}
