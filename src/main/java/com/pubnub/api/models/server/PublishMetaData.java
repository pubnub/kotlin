package com.pubnub.api.models.server;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class PublishMetaData {

    @JsonProperty("t")
    private Long publishTimetoken;

    @JsonProperty("r")
    private Integer region;

}
