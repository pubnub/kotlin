package com.pubnub.api.models.server;

import com.google.gson.JsonElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoryForChannelsItem {

    @Getter private JsonElement message;

    @Getter private Long timetoken;

}
