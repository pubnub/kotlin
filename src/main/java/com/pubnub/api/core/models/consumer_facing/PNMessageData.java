package com.pubnub.api.core.models.consumer_facing;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PNMessageData extends PNSubscriberData {

    Object message;

}
