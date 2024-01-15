package com.pubnub.api.models.consumer.history;

import com.pubnub.api.models.consumer.PNBoundedPage;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@ToString
@Data
@Builder
public class PNFetchMessagesResult {
    private final Map<String, List<PNFetchMessageItem>> channels;
    private final PNBoundedPage page;
}
