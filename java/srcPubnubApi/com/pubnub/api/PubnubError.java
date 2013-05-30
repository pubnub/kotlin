package com.pubnub.api;

public class PubnubError {

    public static final PubnubError PNERR_5000_TIMEOUT               =       new PubnubError( 5000 , "Timeout Occurred"            );
    public static final PubnubError PNERR_5001_ENCRYPTION_ERROR      =       new PubnubError( 5001 , "Encryption Error"            );
    public static final PubnubError PNERR_5002_ERR_DECRYPTION_ERROR  =       new PubnubError( 5002 , "Decryption Error"            );
    public static final PubnubError PNERR_5003_INVALID_JSON          =       new PubnubError( 5003 , "Invalid Json"                );
    public static final PubnubError PNERR_5004_JSON_ERROR            =       new PubnubError( 5004 , "JSON Processing Error"       );
    public static final PubnubError PNERR_5005_JSON_ERROR            =       new PubnubError( 5005 , "JSON Processing Error"       );
    public static final PubnubError PNERR_5006_MALFORMED_URL         =       new PubnubError( 5006 , "Malformed URL"               );
    public static final PubnubError PNERR_5007_PUBNUB_ERROR          =       new PubnubError( 5007 , "Pubnub Error"                );
    public static final PubnubError PNERR_5008_URL_OPEN              =       new PubnubError( 5008 , "Error opening url"           );
    public static final PubnubError PNERR_5009_PROTOCOL_EXCEPTION    =       new PubnubError( 5009 , "Protocol Exception"          );
    public static final PubnubError PNERR_5010_CONNECT_EXCEPTION     =       new PubnubError( 5010 , "Connect Exception"           );
    public static final PubnubError PNERR_5011_HTTP_RC_ERROR         =       new PubnubError( 5011 , "Unable to get Response Code" );
    public static final PubnubError PNERR_5012_GETINPUTSTREAM         =       new PubnubError( 5012 , "Unable to get Input Stream"  );
    public static final PubnubError PNERR_5013_GETINPUTSTREAM         =       new PubnubError( 5013 , "Unable to get Input Stream"  );
    public static final PubnubError PNERR_5014_READINPUT             =       new PubnubError( 5014 , "Unable to read Input Stream" );
    public static final PubnubError PNERR_5015_INVALID_JSON             =       new PubnubError( 5015 , "Invalid Json"                );
    public static final PubnubError PNERR_5016_BAD_REQUEST             =       new PubnubError( 5016 , "Bad request"                 );
    public static final PubnubError PNERR_5017_HTTP_ERROR             =       new PubnubError( 5017 , "HTTP Error"                  );
    public static final PubnubError PNERR_5018_HTTP_ERROR             =       new PubnubError( 5018 , "HTTP Error"                  );
    public static final PubnubError PNERR_5019_HTTP_ERROR             =       new PubnubError( 5019 , "HTTP Error"                  );

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
