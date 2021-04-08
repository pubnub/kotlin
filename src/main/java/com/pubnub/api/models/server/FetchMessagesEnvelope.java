package com.pubnub.api.models.server;

import com.pubnub.api.models.consumer.history.PNFetchMessageItem;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class FetchMessagesEnvelope {
    private Map<String, List<PNFetchMessageItem>> channels;
    private FetchMessagesPage more;

    @Data
    public static class FetchMessagesPage {
        private Long start;
        private Long end;
        private Integer max;
    }
}
