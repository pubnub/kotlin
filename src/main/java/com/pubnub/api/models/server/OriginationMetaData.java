package com.pubnub.api.models.server;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class OriginationMetaData {

    @JsonProperty("t")
    private Long timetoken;

    @JsonProperty("r")
    private Integer region;

}
