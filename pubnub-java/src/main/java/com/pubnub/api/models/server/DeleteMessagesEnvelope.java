package com.pubnub.api.models.server;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteMessagesEnvelope {

    private Integer status;
    private boolean error;
    @SerializedName("error_message")
    private String errorMessage;
}
