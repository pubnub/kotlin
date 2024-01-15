package com.pubnub.api.endpoints.remoteaction;

public interface PNFunction3<INPUT1, INPUT2, INPUT3, OUTPUT> {
    OUTPUT invoke(INPUT1 input1, INPUT2 input2, INPUT3 input3);
}
