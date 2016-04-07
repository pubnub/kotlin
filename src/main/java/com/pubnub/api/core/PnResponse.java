package com.pubnub.api.core;

import com.pubnub.api.events.PnResultStatus;
import lombok.Getter;
import lombok.Setter;
import retrofit2.Response;

@Getter
public class PnResponse<T> {

    @Setter T payload;
    @Setter PnResultStatus resultType;
    int statusCode;
    boolean isSuccessful;
    String errorBody;




    /*
    	private ResultType type;
	private int code;
	private OperationType operation;
	private Config config;
	private String connectionId;
	private String clientRequest;
	private String serverResponse;
     */

    public PnResponse fillFromRetrofit(Response response){
        this.statusCode = response.code();
        this.isSuccessful = response.isSuccessful();

        if (response.errorBody() != null){
            this.errorBody = response.errorBody().toString();
        }

        return this;
    }

}
