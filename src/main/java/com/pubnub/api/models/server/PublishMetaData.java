package com.pubnub.api.models.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class PublishMetaData {

    @JsonProperty("t")
    private Long publishTimetoken;

    @JsonProperty("r")
    private Integer region;

}
