package com.pubnub.api.models.server;

import com.pubnub.api.models.consumer.history.PNFetchMessageItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class FetchMessagesEnvelope {

    private Map<String, List<PNFetchMessageItem>> channels;
}
