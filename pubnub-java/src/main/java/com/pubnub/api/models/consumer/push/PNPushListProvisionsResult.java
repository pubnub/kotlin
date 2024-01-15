package com.pubnub.api.models.consumer.push;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
public class PNPushListProvisionsResult {

    private List<String> channels;

}
