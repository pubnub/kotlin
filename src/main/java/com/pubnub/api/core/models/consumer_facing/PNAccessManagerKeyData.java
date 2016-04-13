package com.pubnub.api.core.models.consumer_facing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class PNAccessManagerKeyData {

    @JsonProperty("r")
    boolean readEnabled;

    @JsonProperty("w")
    boolean writeEnabled;

    @JsonProperty("m")
    boolean manageEnabled;

}
