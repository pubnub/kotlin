package com.pubnub.api.core;

import com.pubnub.api.core.models.consumer_facing.PNStatus;
import com.pubnub.api.events.PnResultStatus;
import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;

@Getter
@Setter
public class ErrorStatus<T> extends PNStatus {

    Call<T> executedCall;
    PnResultStatus type = new PnResultStatus();
    Callback<T> callbacks;
    Throwable throwable;

    public StackTraceElement[] getStackTrace() {
        return throwable.getStackTrace();
    }

    public String getMessage() {
        return throwable.getMessage();
    }

    public void retry() {
        executedCall.clone().enqueue(callbacks);
    }

}
