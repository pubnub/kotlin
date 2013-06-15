package com.pubnub.api;

/**
 * PubnubError object is passed to errorCallback. It contains details of error, like
 * error code, error string, and optional message
 *
 * @author Pubnub
 *
 */
public class PubnubError {
    public static final int         PNERR_5000_TIMEOUT                          =       5000;
    public static final PubnubError PNERROBJ_5000_TIMEOUT                       =
            new PubnubError( PNERR_5000_TIMEOUT , "Timeout Occurred" );

    public static final int         PNERR_5001_ENCRYPTION_ERROR         =       5001;
    public static final PubnubError PNERROBJ_5001_ENCRYPTION_ERROR              =
            new PubnubError( PNERR_5001_ENCRYPTION_ERROR , "Encryption Error"            );

    public static final int         PNERR_5002_DECRYPTION_ERROR     =       5002;
    public static final PubnubError PNERROBJ_5002_DECRYPTION_ERROR          =
            new PubnubError( PNERR_5002_DECRYPTION_ERROR , "Decryption Error"            );

    public static final int         PNERR_5003_INVALID_JSON             =       5003;
    public static final PubnubError PNERROBJ_5003_INVALID_JSON                  =
            new PubnubError( PNERR_5003_INVALID_JSON , "Invalid Json"                );

    public static final int         PNERR_5004_JSON_ERROR               =       5004;
    public static final PubnubError PNERROBJ_5004_JSON_ERROR                    =
            new PubnubError( PNERR_5004_JSON_ERROR , "JSON Processing Error"       );

    public static final int         PNERR_5005_JSON_ERROR               =       5005;
    public static final PubnubError PNERROBJ_5005_JSON_ERROR                    =
            new PubnubError( PNERR_5005_JSON_ERROR , "JSON Processing Error"       );

    public static final int         PNERR_5006_MALFORMED_URL            =       5006;
    public static final PubnubError PNERROBJ_5006_MALFORMED_URL                 =
            new PubnubError( PNERR_5006_MALFORMED_URL , "Malformed URL"               );

    public static final int         PNERR_5007_PUBNUB_ERROR             =       5007;
    public static final PubnubError PNERROBJ_5007_PUBNUB_ERROR                  =
            new PubnubError( PNERR_5007_PUBNUB_ERROR , "Pubnub Error"                );

    public static final int         PNERR_5008_URL_OPEN                 =       5008;
    public static final PubnubError PNERROBJ_5008_URL_OPEN                      =
            new PubnubError( PNERR_5008_URL_OPEN , "Error opening url"           );

    public static final int         PNERR_5009_PROTOCOL_EXCEPTION       =       5009;
    public static final PubnubError PNERROBJ_5009_PROTOCOL_EXCEPTION            =
            new PubnubError( PNERR_5009_PROTOCOL_EXCEPTION , "Protocol Exception"          );

    public static final int         PNERR_5010_CONNECT_EXCEPTION        =       5010;
    public static final PubnubError PNERROBJ_5010_CONNECT_EXCEPTION             =
            new PubnubError( PNERR_5010_CONNECT_EXCEPTION , "Connect Exception"           );

    public static final int         PNERR_5011_HTTP_RC_ERROR            =       5011;
    public static final PubnubError PNERROBJ_5011_HTTP_RC_ERROR                 =
            new PubnubError( PNERR_5011_HTTP_RC_ERROR , "Unable to get Response Code" );

    public static final int         PNERR_5012_GETINPUTSTREAM           =       5012;
    public static final PubnubError PNERROBJ_5012_GETINPUTSTREAM                =
            new PubnubError( PNERR_5012_GETINPUTSTREAM , "Unable to get Input Stream"  );

    public static final int         PNERR_5013_GETINPUTSTREAM           =       5013;
    public static final PubnubError PNERROBJ_5013_GETINPUTSTREAM                =
            new PubnubError( PNERR_5013_GETINPUTSTREAM , "Unable to get Input Stream"  );

    public static final int         PNERR_5014_READINPUT                =       5014;
    public static final PubnubError PNERROBJ_5014_READINPUT                     =
            new PubnubError( PNERR_5014_READINPUT , "Unable to read Input Stream" );

    public static final int         PNERR_5015_INVALID_JSON             =       5015;
    public static final PubnubError PNERROBJ_5015_INVALID_JSON                  =
            new PubnubError( PNERR_5015_INVALID_JSON , "Invalid Json"                );

    public static final int         PNERR_5016_BAD_REQUEST              =       5016;
    public static final PubnubError PNERROBJ_5016_BAD_REQUEST                   =
            new PubnubError( PNERR_5016_BAD_REQUEST , "Bad request"                 );

    public static final int         PNERR_5017_HTTP_ERROR               =       5017;
    public static final PubnubError PNERROBJ_5017_HTTP_ERROR                    =
            new PubnubError( PNERR_5017_HTTP_ERROR , "HTTP Error"                  );

    public static final int         PNERR_5018_HTTP_ERROR               =       5018;
    public static final PubnubError PNERROBJ_5018_HTTP_ERROR                    =
            new PubnubError( PNERR_5018_HTTP_ERROR , "HTTP Error"                  );

    public static final int         PNERR_5019_HTTP_ERROR               =       5019;
    public static final PubnubError PNERROBJ_5019_HTTP_ERROR                    =
            new PubnubError( PNERR_5019_HTTP_ERROR , "HTTP Error"                  );

    public static final int         PNERR_5020_BAD_GATEWAY              =       5020;
    public static final PubnubError PNERROBJ_5020_BAD_GATEWAY                   =
            new PubnubError( PNERR_5020_BAD_GATEWAY , "Bad Gateway"                 );

    public static final int         PNERR_5021_CLIENT_TIMEOUT           =       5021;
    public static final PubnubError PNERROBJ_5021_CLIENT_TIMEOUT                =
            new PubnubError( PNERR_5021_CLIENT_TIMEOUT , "Client Timeout"              );

    public static final int         PNERR_5022_GATEWAY_TIMEOUT          =       5022;
    public static final PubnubError PNERROBJ_5022_GATEWAY_TIMEOUT               =
            new PubnubError( PNERR_5022_GATEWAY_TIMEOUT , "Gateway Timeout"             );

    public static final int         PNERR_5023_INTERNAL_ERROR           =       5023;
    public static final PubnubError PNERROBJ_5023_INTERNAL_ERROR                =
            new PubnubError( PNERR_5023_INTERNAL_ERROR , "Internal Server Error"       );

    public static final int         PNERR_5024_DECRYPTION_ERROR         =       5024;
    public static final PubnubError PNERROBJ_5024_DECRYPTION_ERROR              =
            new PubnubError( PNERR_5024_DECRYPTION_ERROR , "Decryption Error"            );

    public static final int         PNERR_5025_DECRYPTION_ERROR         =       5025;
    public static final PubnubError PNERROBJ_5025_DECRYPTION_ERROR              =
            new PubnubError( PNERR_5025_DECRYPTION_ERROR , "Decryption Error"            );

    public static final int         PNERR_5026_PARSING_ERROR            =       5026;
    public static final PubnubError PNERROBJ_5026_PARSING_ERROR                 =
            new PubnubError( PNERR_5026_PARSING_ERROR , "Parsing Error"               );

    public static final int         PNERR_5027_PUBNUB_EXCEPTION         =       5027;
    public static final PubnubError PNERROBJ_5027_PUBNUB_EXCEPTION              =
            new PubnubError( PNERR_5027_PUBNUB_EXCEPTION , "Pubnub Exception"            );

    public static final int         PNERR_5028_INVALID_JSON             =       5028;
    public static final PubnubError PNERROBJ_5028_INVALID_JSON                  =
            new PubnubError( PNERR_5028_INVALID_JSON , "Invalid Json"                );

    public static final int         PNERR_5029_DISCONNECT               =       5029;
    public static final PubnubError PNERROBJ_5029_DISCONNECT                    =
            new PubnubError( PNERR_5029_DISCONNECT , "Disconnect"                  );

    public static final int         PNERR_5030_DISCONN_AND_RESUB        =       5030;
    public static final PubnubError PNERROBJ_5030_DISCONN_AND_RESUB             =
            new PubnubError( PNERR_5030_DISCONN_AND_RESUB , "Disconnect and Resubscribe"  );

    public static final int         PNERR_5031_FORBIDDEN                =       5031;
    public static final PubnubError PNERROBJ_5031_FORBIDDEN                     =
            new PubnubError( PNERR_5031_FORBIDDEN , "Authentication Failure");

    public static final int         PNERR_5032_UNAUTHORIZED             =       5032;
    public static final PubnubError PNERROBJ_5032_UNAUTHORIZED                  =
            new PubnubError( PNERR_5032_UNAUTHORIZED , "Authentication Failure");


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

    static PubnubError getErrorObject(PubnubError error, String message) {
        return new PubnubError(error.errorCode, error.errorString, message);
    }
    public String getErrorString() {
        return errorString;
    }
}
