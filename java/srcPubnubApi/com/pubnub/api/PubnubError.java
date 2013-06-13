package com.pubnub.api;

public class PubnubError {
    public static final int         PNERR_5000_TIMEOUT_CODE                  =       5000;
    public static final PubnubError PNERR_5000_TIMEOUT                       =
            new PubnubError( PNERR_5000_TIMEOUT_CODE , "Timeout Occurred" );

    public static final int         PNERR_5001_ENCRYPTION_ERROR_CODE         =       5001;
    public static final PubnubError PNERR_5001_ENCRYPTION_ERROR              =
            new PubnubError( 5001 , "Encryption Error"            );

    public static final int         PNERR_5002_ERR_DECRYPTION_ERROR_CODE     =       5002;
    public static final PubnubError PNERR_5002_ERR_DECRYPTION_ERROR          =
            new PubnubError( 5002 , "Decryption Error"            );

    public static final int         PNERR_5003_INVALID_JSON_CODE             =       5003;
    public static final PubnubError PNERR_5003_INVALID_JSON                  =
            new PubnubError( 5003 , "Invalid Json"                );

    public static final int         PNERR_5004_JSON_ERROR_CODE               =       5004;
    public static final PubnubError PNERR_5004_JSON_ERROR                    =
            new PubnubError( 5004 , "JSON Processing Error"       );

    public static final int         PNERR_5005_JSON_ERROR_CODE               =       5005;
    public static final PubnubError PNERR_5005_JSON_ERROR                    =
            new PubnubError( 5005 , "JSON Processing Error"       );

    public static final int         PNERR_5006_MALFORMED_URL_CODE            =       5006;
    public static final PubnubError PNERR_5006_MALFORMED_URL                 =
            new PubnubError( 5006 , "Malformed URL"               );

    public static final int         PNERR_5007_PUBNUB_ERROR_CODE             =       5007;
    public static final PubnubError PNERR_5007_PUBNUB_ERROR                  =
            new PubnubError( 5007 , "Pubnub Error"                );

    public static final int         PNERR_5008_URL_OPEN_CODE                 =       5008;
    public static final PubnubError PNERR_5008_URL_OPEN                      =
            new PubnubError( 5008 , "Error opening url"           );

    public static final int         PNERR_5009_PROTOCOL_EXCEPTION_CODE       =       5009;
    public static final PubnubError PNERR_5009_PROTOCOL_EXCEPTION            =
            new PubnubError( 5009 , "Protocol Exception"          );

    public static final int         PNERR_5010_CONNECT_EXCEPTION_CODE        =       5010;
    public static final PubnubError PNERR_5010_CONNECT_EXCEPTION             =
            new PubnubError( 5010 , "Connect Exception"           );

    public static final int         PNERR_5011_HTTP_RC_ERROR_CODE            =       5011;
    public static final PubnubError PNERR_5011_HTTP_RC_ERROR                 =
            new PubnubError( 5011 , "Unable to get Response Code" );

    public static final int         PNERR_5012_GETINPUTSTREAM_CODE           =       5012;
    public static final PubnubError PNERR_5012_GETINPUTSTREAM                =
            new PubnubError( 5012 , "Unable to get Input Stream"  );

    public static final int         PNERR_5013_GETINPUTSTREAM_CODE           =       5013;
    public static final PubnubError PNERR_5013_GETINPUTSTREAM                =
            new PubnubError( 5013 , "Unable to get Input Stream"  );

    public static final int         PNERR_5014_READINPUT_CODE                =       5014;
    public static final PubnubError PNERR_5014_READINPUT                     =
            new PubnubError( 5014 , "Unable to read Input Stream" );

    public static final int         PNERR_5015_INVALID_JSON_CODE             =       5015;
    public static final PubnubError PNERR_5015_INVALID_JSON                  =
            new PubnubError( 5015 , "Invalid Json"                );

    public static final int         PNERR_5016_BAD_REQUEST_CODE              =       5016;
    public static final PubnubError PNERR_5016_BAD_REQUEST                   =
            new PubnubError( 5016 , "Bad request"                 );

    public static final int         PNERR_5017_HTTP_ERROR_CODE               =       5017;
    public static final PubnubError PNERR_5017_HTTP_ERROR                    =
            new PubnubError( 5017 , "HTTP Error"                  );

    public static final int         PNERR_5018_HTTP_ERROR_CODE               =       5018;
    public static final PubnubError PNERR_5018_HTTP_ERROR                    =
            new PubnubError( 5018 , "HTTP Error"                  );

    public static final int         PNERR_5019_HTTP_ERROR_CODE               =       5019;
    public static final PubnubError PNERR_5019_HTTP_ERROR                    =
            new PubnubError( 5019 , "HTTP Error"                  );

    public static final int         PNERR_5020_BAD_GATEWAY_CODE              =       5020;
    public static final PubnubError PNERR_5020_BAD_GATEWAY                   =
            new PubnubError( 5020 , "Bad Gateway"                 );

    public static final int         PNERR_5021_CLIENT_TIMEOUT_CODE           =       5021;
    public static final PubnubError PNERR_5021_CLIENT_TIMEOUT                =
            new PubnubError( 5021 , "Client Timeout"              );

    public static final int         PNERR_5022_GATEWAY_TIMEOUT_CODE          =       5022;
    public static final PubnubError PNERR_5022_GATEWAY_TIMEOUT               =
            new PubnubError( 5022 , "Gateway Timeout"             );

    public static final int         PNERR_5023_INTERNAL_ERROR_CODE           =       5023;
    public static final PubnubError PNERR_5023_INTERNAL_ERROR                =
            new PubnubError( 5023 , "Internal Server Error"       );

    public static final int         PNERR_5024_DECRYPTION_ERROR_CODE         =       5024;
    public static final PubnubError PNERR_5024_DECRYPTION_ERROR              =
            new PubnubError( 5024 , "Decryption Error"            );

    public static final int         PNERR_5025_DECRYPTION_ERROR_CODE         =       5025;
    public static final PubnubError PNERR_5025_DECRYPTION_ERROR              =
            new PubnubError( 5025 , "Decryption Error"            );

    public static final int         PNERR_5026_PARSING_ERROR_CODE            =       5026;
    public static final PubnubError PNERR_5026_PARSING_ERROR                 =
            new PubnubError( 5026 , "Parsing Error"               );

    public static final int         PNERR_5027_PUBNUB_EXCEPTION_CODE         =       5027;
    public static final PubnubError PNERR_5027_PUBNUB_EXCEPTION              =
            new PubnubError( 5027 , "Pubnub Exception"            );

    public static final int         PNERR_5028_INVALID_JSON_CODE             =       5028;
    public static final PubnubError PNERR_5028_INVALID_JSON                  =
            new PubnubError( 5028 , "Invalid Json"                );

    public static final int         PNERR_5029_DISCONNECT_CODE               =       5029;
    public static final PubnubError PNERR_5029_DISCONNECT                    =
            new PubnubError( 5029 , "Disconnect"                  );

    public static final int         PNERR_5030_DISCONN_AND_RESUB_CODE        =       5030;
    public static final PubnubError PNERR_5030_DISCONN_AND_RESUB             =
            new PubnubError( PNERR_5030_DISCONN_AND_RESUB_CODE , "Disconnect and Resubscribe"  );

    public static final int         PNERR_5031_FORBIDDEN_CODE                =       5031;
    public static final PubnubError PNERR_5031_FORBIDDEN                     =
            new PubnubError( PNERR_5031_FORBIDDEN_CODE , "Authentication Failure", "Incorrect Authentication Key");

    public static final int         PNERR_5032_UNAUTHORIZED_CODE             =       5032;
    public static final PubnubError PNERR_5032_UNAUTHORIZED                  =
            new PubnubError( PNERR_5032_UNAUTHORIZED_CODE , "Authentication Failure", "Authentication Key Missing");


    public  final int errorCode;
    private final String errorString;
    private String message;

    private PubnubError(int errorCode, String errorString) {
        this.errorCode = errorCode;
        this.errorString = errorString;
    }
    private PubnubError(int errorCode, String errorString, String message) {
        this.errorCode = errorCode;
        this.errorString = errorString;
        this.message = message;
    }
    public PubnubError(PubnubError error, String message) {
        this.errorCode = error.errorCode;
        this.errorString = error.errorString;
        this.message = message;
    }
    public String toString() {
        String value = "[Error: " + errorCode + "] : " + errorString;
        if (message != null && message.length() > 0) {
            value += " : " + message;
        }
        return value;
    }

    public String getErrorString() {
        return errorString;
    }
}
