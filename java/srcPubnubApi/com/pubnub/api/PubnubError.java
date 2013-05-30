package com.pubnub.api;

public class PubnubError {

    public static final PubnubError PUBNUB_ERR_TIMEOUT               =       new PubnubError( 5000 , "Timeout Occurred");
    public static final PubnubError PUBNUB_ERR_ENCRYPTION_ERROR      =       new PubnubError( 5001 , "Encryption Error");
    public static final PubnubError PUBNUB_ERR_DECRYPTION_ERR        =       new PubnubError( 5002 , "Decryption Error");
    public static final PubnubError PUBNUB_ERR_INVALID_JSON          =       new PubnubError( 5003 , "Invalid Json"    );

    private final int errorCode;
    private final String errorString;

    private PubnubError(int errorCode, String errorString) {
        this.errorCode = errorCode;
        this.errorString = errorString;
    }
    public String toString() {
        return "[" + errorCode + "] : " + errorString;
    }
}
