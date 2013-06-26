package com.pubnub.api;

/**
 * PubnubError object is passed to errorCallback. It contains details of error, like
 * error code, error string, and optional message
 *
 * @author Pubnub
 *
 */
public class PubnubError {
    /**
     * Timeout Error .
     */
    public static final int         PNERR_5000_TIMEOUT                      =       5000;
    public static final PubnubError PNERROBJ_5000_TIMEOUT                   =
            new PubnubError     (
                                       PNERR_5000_TIMEOUT , "Timeout Occurred"
                                );

    /**
     * Error while encrypting message to be published to Pubnub Cloud .
     * Please contact support with error details.
     */
    static final int         PNERR_5001_ENCRYPTION_ERROR             =       5001;
    public static final PubnubError PNERROBJ_5001_ENCRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5001_ENCRYPTION_ERROR ,
                                    "Error while encrypting message to be published to Pubnub Cloud ." +
                                    "Please contact support with error details."
                                );

    /**
     * Decryption Error .
     * Please contact support with error details.
     */
    static final int         PNERR_5002_DECRYPTION_ERROR             =       5002;
    public static final PubnubError PNERROBJ_5002_DECRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5002_DECRYPTION_ERROR ,
                                    "Decryption Error. " +
                                    "Please contact support with error details."
                                );

    /**
     * Invalid Json .
     * Please contact support with error details.
     */
    static final int         PNERR_5003_INVALID_JSON                 =       5003;
    public static final PubnubError PNERROBJ_5003_INVALID_JSON                 =
            new PubnubError     (
                                    PNERR_5003_INVALID_JSON ,
                                    "Invalid Json. " +
                                    "Please contact support with error details."
                                );

    /**
     * JSON Error while processing API response.
     * Please contact support with error details.
     */
    static final int         PNERR_5004_JSON_ERROR                   =       5004;
    public static final PubnubError PNERROBJ_5004_JSON_ERROR                =
            new PubnubError     (
                                    PNERR_5004_JSON_ERROR ,
                                    "JSON Error while processing API response. " +
                                    "Please contact support with error details."
                                );

    /**
     * A JSON error occurred .
     * Please contact support with error details.
     */
    static final int         PNERR_5005_JSON_ERROR                   =       5005;
    public static final PubnubError PNERROBJ_5005_JSON_ERROR                =
            new PubnubError     (
                                    PNERR_5005_JSON_ERROR ,
                                    "JSON Processing Error. " +
                                    "Please contact support with error details."
                                );

    /**
     * Malformed URL .
     * Please contact support with error details .
     */
    static final int         PNERR_5006_MALFORMED_URL                =       5006;
    public static final PubnubError PNERROBJ_5006_MALFORMED_URL             =
            new PubnubError     (
                                    PNERR_5006_MALFORMED_URL ,
                                    "Malformed URL ." +
                                    "Please contact support with error details."
                                );

    /**
     *
     */
    public static final int         PNERR_5007_PUBNUB_ERROR                 =       5007;
    public static final PubnubError PNERROBJ_5007_PUBNUB_ERROR              =
            new PubnubError     (
                                     PNERR_5007_PUBNUB_ERROR ,
                                     "Pubnub Error"
                                );

    /**
     * Error in opening URL .
     * Please contact support with error details.
     */
    static final int         PNERR_5008_URL_OPEN                     =       5008;
    public static final PubnubError PNERROBJ_5008_URL_OPEN                  =
            new PubnubError     (
                                    PNERR_5008_URL_OPEN ,
                                    "Error opening url. "   +
                                    "Please contact support with error details."
                                );

    /**
     * Protocol Exception .
     * Please contact support with error details.
     */
    static final int         PNERR_5009_PROTOCOL_EXCEPTION           =       5009;
    public static final PubnubError PNERROBJ_5009_PROTOCOL_EXCEPTION        =
            new PubnubError     (
                                    PNERR_5009_PROTOCOL_EXCEPTION ,
                                    "Protocol Exception. " +
                                    "Please contact support with error details."
                                );

    /**
     * Connect Exception .
     * Network Unreachable.
     */
    public static final int         PNERR_5010_CONNECT_EXCEPTION            =       5010;
    public static final PubnubError PNERROBJ_5010_CONNECT_EXCEPTION         =
            new PubnubError     (
                                    PNERR_5010_CONNECT_EXCEPTION ,
                                    "Connect Exception. " +
                                    "Please verify if network is reachable. "
                                );

    /**
     * Unable to get response code .
     * Please contact support with error details.
     */
    public static final int         PNERR_5011_HTTP_RC_ERROR                =       5011;
    public static final PubnubError PNERROBJ_5011_HTTP_RC_ERROR             =
            new PubnubError     (
                                    PNERR_5011_HTTP_RC_ERROR ,
                                    "Unable to get Response Code. " +
                                    "Please contact support with error details."
                                );

    /**
     * Unable to open input stream .
     * Please contact support with error details.
     */
    public static final int         PNERR_5012_GETINPUTSTREAM               =       5012;
    public static final PubnubError PNERROBJ_5012_GETINPUTSTREAM            =
            new PubnubError     (
                                    PNERR_5012_GETINPUTSTREAM ,
                                    "Unable to get Input Stream. " +
                                    "Please contact support with error details."
                                );

    /**
     * Unable to open input stream .
     * Please contact support with error details.
     */
    public static final int         PNERR_5013_GETINPUTSTREAM               =       5013;
    public static final PubnubError PNERROBJ_5013_GETINPUTSTREAM            =
            new PubnubError     (
                                    PNERR_5013_GETINPUTSTREAM ,
                                    "Unable to get Input Stream" +
                                    "Please contact support with error details."
                                );

    /**
     * Unable to read input stream .
     * Please contact support with error details.
     */
    public static final int         PNERR_5014_READINPUT                    =       5014;
    public static final PubnubError PNERROBJ_5014_READINPUT                 =
            new PubnubError     (
                                    PNERR_5014_READINPUT ,
                                    "Unable to read Input Stream. " +
                                    "Please contact support with error details."

                                );

    /**
     * Invalid JSON .
     * Please contact support with error details.
     */
    public static final int         PNERR_5015_INVALID_JSON                 =       5015;
    public static final PubnubError PNERROBJ_5015_INVALID_JSON              =
            new PubnubError     (
                                    PNERR_5015_INVALID_JSON ,
                                    "Invalid Json. " +
                                    "Please contact support with error details."
                                );

    /**
     * Bad Request .
     * Please contact support with error details.
     */
    public static final int         PNERR_5016_BAD_REQUEST                  =       5016;
    public static final PubnubError PNERROBJ_5016_BAD_REQUEST               =
            new PubnubError     (
                                    PNERR_5016_BAD_REQUEST ,
                                    "Bad request. " +
                                    "Please contact support with error details."
                                );

    /**
     * Please check network connectivity.
     * Please contact support with error details if issue persists.
     */
    public static final int         PNERR_5017_HTTP_ERROR                   =       5017;
    public static final PubnubError PNERROBJ_5017_HTTP_ERROR                =
            new PubnubError     (
                                    PNERR_5017_HTTP_ERROR ,
                                    "HTTP Error. " +
                                    "Please check network connectivity. " +
                                    "Please contact support with error details if issue persists."
                                );

    /**
     * HTTP Error .
     * Please contact support with error details.
     */
    public static final int         PNERR_5018_HTTP_ERROR                   =       5018;
    public static final PubnubError PNERROBJ_5018_HTTP_ERROR                =
            new PubnubError     (
                                    PNERR_5018_HTTP_ERROR ,
                                    "HTTP Error" +
                                    "Please contact support with error details."
                                );

    /**
     * HTTP Error .
     * Please contact support with error details.
     */
    public static final int         PNERR_5019_HTTP_ERROR                   =       5019;
    public static final PubnubError PNERROBJ_5019_HTTP_ERROR                =
            new PubnubError     (
                                    PNERR_5019_HTTP_ERROR ,
                                    "HTTP Error" +
                                    "Please contact support with error details."
                                );

    /**
     * Bad gateway .
     * Please contact support with error details.
     */
    public static final int         PNERR_5020_BAD_GATEWAY                  =       5020;
    public static final PubnubError PNERROBJ_5020_BAD_GATEWAY               =
            new PubnubError     (
                                    PNERR_5020_BAD_GATEWAY ,
                                    "Bad Gateway. " +
                                    "Please contact support with error details."
                                );

    /**
     * Client Timeout .
     */
    public static final int         PNERR_5021_CLIENT_TIMEOUT               =       5021;
    public static final PubnubError PNERROBJ_5021_CLIENT_TIMEOUT            =
            new PubnubError     (
                                    PNERR_5021_CLIENT_TIMEOUT ,
                                    "Client Timeout"
                                );

    /**
     * Gateway Timeout
     */
    public static final int         PNERR_5022_GATEWAY_TIMEOUT              =       5022;
    public static final PubnubError PNERROBJ_5022_GATEWAY_TIMEOUT           =
            new PubnubError     (
                                    PNERR_5022_GATEWAY_TIMEOUT ,
                                    "Gateway Timeout"
                                );

    /**
     * Pubnub server returned HTTP 502 internal server error status code.
     * Please contact support with error details.
     */
    public static final int         PNERR_5023_INTERNAL_ERROR               =       5023;
    public static final PubnubError PNERROBJ_5023_INTERNAL_ERROR            =
            new PubnubError     (
                                    PNERR_5023_INTERNAL_ERROR ,
                                    "Internal Server Error. " +
                                    "Please contact support with error details."
                                );

    /**
     * Decryption Error .
     * Please contact support with error details.
     */
    public static final int         PNERR_5024_DECRYPTION_ERROR             =       5024;
    public static final PubnubError PNERROBJ_5024_DECRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5024_DECRYPTION_ERROR ,
                                    "Decryption Error. " +
                                    "Please contact support with error details."

                                );

    /**
     * Decryption Error .
     * Please contact support with error details.
     */
    public static final int         PNERR_5025_DECRYPTION_ERROR             =       5025;
    public static final PubnubError PNERROBJ_5025_DECRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5025_DECRYPTION_ERROR ,
                                    "Decryption Error. " +
                                    "Please contact support with error details."
                                );

    /**
     * Parsing Error .
     */
    static final int         PNERR_5026_PARSING_ERROR                =       5026;
    public static final PubnubError PNERROBJ_5026_PARSING_ERROR             =
            new PubnubError     (
                                    PNERR_5026_PARSING_ERROR ,
                                    "Parsing Error"
                                );

    /**
     * Pubnub Exception .
     */
    public static final int         PNERR_5027_PUBNUB_EXCEPTION             =       5027;
    public static final PubnubError PNERROBJ_5027_PUBNUB_EXCEPTION          =
            new PubnubError     (
                                    PNERR_5027_PUBNUB_EXCEPTION ,
                                    "Pubnub Exception"
                                );

    /**
     * Invalid Json .
     */
    public static final int         PNERR_5028_INVALID_JSON                 =       5028;
    public static final PubnubError PNERROBJ_5028_INVALID_JSON              =
            new PubnubError     (
                                    PNERR_5028_INVALID_JSON ,
                                    "Invalid Json"
                                );

    /**
     * Disconnect .
     */
    public static final int         PNERR_5029_DISCONNECT                   =       5029;
    public static final PubnubError PNERROBJ_5029_DISCONNECT                =
            new PubnubError     (
                                    PNERR_5029_DISCONNECT ,
                                    "Disconnect"
                                );

    /**
     * Disconnect and Resubscribe Received .
     */
    public static final int         PNERR_5030_DISCONN_AND_RESUB            =       5030;
    public static final PubnubError PNERROBJ_5030_DISCONN_AND_RESUB         =
            new PubnubError     (
                                    PNERR_5030_DISCONN_AND_RESUB ,
                                    "Disconnect and Resubscribe"
                                );

    /**
     * Pubnub server returned HTTP 403 forbidden status code.
     * Happens when wrong authentication key is used .
     */
    public static final int         PNERR_5031_FORBIDDEN                    =       5031;
    public static final PubnubError PNERROBJ_5031_FORBIDDEN                 =
            new PubnubError     (
                                    PNERR_5031_FORBIDDEN ,
                                    "Authentication Failure. " +
                                    "Incorrect Authentication Key"
                                );

    /**
     * Pubnub server returned HTTP 401 unauthorized status code
     * Happens when authentication key is missing .
     */
    public static final int         PNERR_5032_UNAUTHORIZED                 =       5032;
    public static final PubnubError PNERROBJ_5032_UNAUTHORIZED              =
            new PubnubError     (
                                    PNERR_5032_UNAUTHORIZED ,
                                    "Authentication Failure. " +
                                    "Authentication Key is missing"
                                );

    /**
     * Error while encrypting message to be published to Pubnub Cloud .
     * Please contact support with error details.
     */
    static final int         PNERR_5033_ENCRYPTION_ERROR             =       5033;
    public static final PubnubError PNERROBJ_5033_ENCRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5033_ENCRYPTION_ERROR ,
                                    "Encryption Error. "  +
                                    "Please contact support with error details."

                                );

    /**
     * Error while encrypting message to be published to Pubnub Cloud .
     * Please contact support with error details.
     */
    static final int         PNERR_5034_ENCRYPTION_ERROR             =       5034;
    public static final PubnubError PNERROBJ_5034_ENCRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5034_ENCRYPTION_ERROR ,
                                    "Encryption Error. " +
                                    "Please contact support with error details."
                                );

    /**
     * Error while encrypting message to be published to Pubnub Cloud .
     * Please contact support with error details.
     */
    public static final int         PNERR_5035_ENCRYPTION_ERROR             =       5035;
    public static final PubnubError PNERROBJ_5035_ENCRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5035_ENCRYPTION_ERROR ,
                                    "Encryption Error. " +
                                    "Please contact support with error details."
                                );

    /**
     * Decryption Error .
     * Please contact support with error details.
     */
    public static final int         PNERR_5036_DECRYPTION_ERROR             =       5036;
    public static final PubnubError PNERROBJ_5036_DECRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5036_DECRYPTION_ERROR ,
                                    "Decryption Error. " +
                                    "Please contact support with error details."
                                );

    /**
     * Decryption Error .
     * Please contact support with error details.
     */
    public static final int         PNERR_5037_DECRYPTION_ERROR             =       5037;
    public static final PubnubError PNERROBJ_5037_DECRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5037_DECRYPTION_ERROR ,
                                    "Decryption Error. " +
                                    "Please contact support with error details."
                                );

    /**
     * Decryption Error .
     * Please contact support with error details.
     */
    public static final int         PNERR_5038_DECRYPTION_ERROR             =       5038;
    public static final PubnubError PNERROBJ_5038_DECRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5038_DECRYPTION_ERROR ,
                                    "Decryption Error. " +
                                    "Please contact support with error details."
                                );

    /**
     * Decryption Error .
     * Please contact support with error details.
     */
    public static final int         PNERR_5039_DECRYPTION_ERROR             =       5039;
    public static final PubnubError PNERROBJ_5039_DECRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5039_DECRYPTION_ERROR ,
                                    "Decryption Error. " +
                                    "Please contact support with error details."
                                );

    /**
     * Invalid data length for decryption.
     * Please contact support with error details.
     */
    public static final int         PNERR_5040_DECRYPTION_ERROR             =       5040;
    public static final PubnubError PNERROBJ_5040_DECRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5040_DECRYPTION_ERROR ,
                                    "Decryption Error. " +
                                    "Please contact support with error details."
                                );

    /**
     * Error while decrypting incoming message.
     * Please contact support with error details.
     */
    public static final int         PNERR_5041_DECRYPTION_ERROR             =       5041;
    public static final PubnubError PNERROBJ_5041_DECRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5041_DECRYPTION_ERROR ,
                                    "Decryption Error. " +
                                    "Please contact support with error details."
                                );

    /**
     * Cipher text Received on channel was invalid.
     * Please contact support with error details.
     */
    public static final int         PNERR_5042_DECRYPTION_ERROR             =       5042;
    public static final PubnubError PNERROBJ_5042_DECRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5042_DECRYPTION_ERROR ,
                                    "Decryption Error. " +
                                    "Please contact support with error details."
                                );

    /**
     * An I/O error occurred while decrypting message.
     * Please contact support with error details.
     */
    public static final int         PNERR_5043_DECRYPTION_ERROR             =       5043;
    public static final PubnubError PNERROBJ_5043_DECRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5043_DECRYPTION_ERROR ,
                                    "Decryption Error. " +
                                    "Please contact support with error details."
                                );
    /**
     * Invalid data length for decryption.
     * Please contact support with error details.
     */
    public static final int         PNERR_5044_DECRYPTION_ERROR             =       5044;
    public static final PubnubError PNERROBJ_5044_DECRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5044_DECRYPTION_ERROR ,
                                    "Decryption Error. " +
                                    "Please contact support with error details."
                                );

    /**
     * Error while decrypting incoming message.
     * Please contact support with error details.
     */
    public static final int         PNERR_5045_DECRYPTION_ERROR             =       5045;
    public static final PubnubError PNERROBJ_5045_DECRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5045_DECRYPTION_ERROR ,
                                    "Decryption Error. " +
                                    "Please contact support with error details."
                                );

    /**
     * Cipher text Received on channel was invalid.
     * Please contact support with error details.
     */
    public static final int         PNERR_5046_DECRYPTION_ERROR             =       5046;
    public static final PubnubError PNERROBJ_5046_DECRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5046_DECRYPTION_ERROR ,
                                    "Decryption Error. " +
                                    "Please contact support with error details."
                                );

    /**
     * An I/O error occurred while decrypting message.
     * Please contact support with error details.
     */
    public static final int         PNERR_5047_DECRYPTION_ERROR             =       5047;
    public static final PubnubError PNERROBJ_5047_DECRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5047_DECRYPTION_ERROR ,
                                    "Decryption Error. " +
                                    "Please contact support with error details."
                                );

    /**
     * Invalid data length for decryption.
     * Please contact support with error details.
     */
    public static final int         PNERR_5048_DECRYPTION_ERROR             =       5048;
    public static final PubnubError PNERROBJ_5048_DECRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5044_DECRYPTION_ERROR ,
                                    "Decryption Error. " +
                                    "Please contact support with error details."
                                );

    /**
     * Error while decrypting incoming message.
     * Please contact support with error details.
     */
    public static final int         PNERR_5049_DECRYPTION_ERROR             =       5049;
    public static final PubnubError PNERROBJ_5049_DECRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5045_DECRYPTION_ERROR ,
                                    "Decryption Error. " +
                                    "Please contact support with error details."
                                );

    /**
     * Cipher text Received on channel was invalid.
     * Please contact support with error details.
     */
    public static final int         PNERR_5050_DECRYPTION_ERROR             =       5050;
    public static final PubnubError PNERROBJ_5050_DECRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5046_DECRYPTION_ERROR ,
                                    "Decryption Error. " +
                                    "Please contact support with error details."
                                );

    /**
     * An I/O error occurred while decrypting message.
     * Please contact support with error details.
     */
    public static final int         PNERR_5051_DECRYPTION_ERROR             =       5051;
    public static final PubnubError PNERROBJ_5051_DECRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5047_DECRYPTION_ERROR ,
                                    "Decryption Error. " +
                                    "Please contact support with error details."
                                );

    /**
     * An error occurred while decrypting message.
     * Please contact support with error details.
     */
    public static final int         PNERR_5052_DECRYPTION_ERROR             =       5052;
    public static final PubnubError PNERROBJ_5052_DECRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5052_DECRYPTION_ERROR ,
                                    "Decryption Error. " +
                                    "Please contact support with error details."
                                );

    /**
     * An error occurred while decrypting message.
     * Please contact support with error details.
     */
    public static final int         PNERR_5053_DECRYPTION_ERROR             =       5053;
    public static final PubnubError PNERROBJ_5053_DECRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5053_DECRYPTION_ERROR ,
                                    "Decryption Error. " +
                                    "Please contact support with error details."
                                );
    /**
     * An error occurred while decrypting message.
     * Please contact support with error details.
     */
    public static final int         PNERR_5054_DECRYPTION_ERROR             =       5054;
    public static final PubnubError PNERROBJ_5054_DECRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5053_DECRYPTION_ERROR ,
                                    "Decryption Error. " +
                                    "Please contact support with error details."
                                );

    /**
     * An error occurred while decrypting message.
     * Please contact support with error details.
     */
    public static final int         PNERR_5055_DECRYPTION_ERROR             =       5055;
    public static final PubnubError PNERROBJ_5055_DECRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5053_DECRYPTION_ERROR ,
                                    "Decryption Error. " +
                                    "Please contact support with error details."
                                );

    /**
     * An error occurred while decrypting message.
     * Please contact support with error details.
     */
    public static final int         PNERR_5056_DECRYPTION_ERROR             =       5056;
    public static final PubnubError PNERROBJ_5056_DECRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5056_DECRYPTION_ERROR ,
                                    "Decryption Error. " +
                                    "Please contact support with error details."
                                );
    /**
     * An error occurred while decrypting message.
     * Please contact support with error details.
     */
    public static final int         PNERR_5057_DECRYPTION_ERROR             =       5057;
    public static final PubnubError PNERROBJ_5057_DECRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5057_DECRYPTION_ERROR ,
                                    "Decryption Error. " +
                                    "Please contact support with error details."
                                );
    /**
     * An error occurred while encrypting message.
     * Please contact support with error details.
     */
    public static final int         PNERR_5058_ENCRYPTION_ERROR             =       5058;
    public static final PubnubError PNERROBJ_5058_ENCRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5058_ENCRYPTION_ERROR ,
                                    "Encryption Error. " +
                                    "Please contact support with error details."
                                );
    /**
     * An error occurred while decrypting message.
     * Please contact support with error details.
     */
    public static final int         PNERR_5059_DECRYPTION_ERROR             =       5059;
    public static final PubnubError PNERROBJ_5059_DECRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5056_DECRYPTION_ERROR ,
                                    "Decryption Error. " +
                                    "Please contact support with error details."
                                );
    /**
     * An error occurred while decrypting message.
     * Please contact support with error details.
     */
    public static final int         PNERR_5060_DECRYPTION_ERROR             =       5060;
    public static final PubnubError PNERROBJ_5060_DECRYPTION_ERROR          =
            new PubnubError     (
                                    PNERR_5060_DECRYPTION_ERROR ,
                                    "Decryption Error. " +
                                    "Please contact support with error details."
                                );
    /**
     * An JSON error occurred .
     * Please contact support with error details.
     */
    public static final int         PNERR_5061_INVALID_JSON                 =       5061;
    public static final PubnubError PNERROBJ_5061_INVALID_JSON              =
            new PubnubError     (
                                    PNERR_5061_INVALID_JSON ,
                                    "Invalid JSON . " +
                                    "Please contact support with error details."
                                );

    /**
     * Secret key not configured
     */
    public static final int         PNERR_5062_SECRET_KEY_MISSING            =       5062;
    public static final PubnubError PNERROBJ_5062_SECRET_KEY_MISSING         =
            new PubnubError     (
                                    PNERR_5062_SECRET_KEY_MISSING ,
                                    "ULS configuration failed. Secret Key not configured. "
                                );
    /**
     * Secret key not configured
     */
    public static final int         PNERR_5063_SECRET_KEY_MISSING            =       5063;
    public static final PubnubError PNERROBJ_5063_SECRET_KEY_MISSING         =
            new PubnubError     (
                                    PNERR_5062_SECRET_KEY_MISSING ,
                                    "ULS configuration failed. Secret Key not configured. "
                                );

    /**
     * Secret key not configured
     */
    public static final int         PNERR_5064_SECRET_KEY_MISSING            =       5064;
    public static final PubnubError PNERROBJ_5064_SECRET_KEY_MISSING         =
            new PubnubError     (
                                    PNERR_5062_SECRET_KEY_MISSING ,
                                    "ULS configuration failed. Secret Key not configured. "
                                );
    /**
     * Secret key not configured
     */
    public static final int         PNERR_5065_SECRET_KEY_MISSING            =       5065;
    public static final PubnubError PNERROBJ_5065_SECRET_KEY_MISSING         =
            new PubnubError     (
                                    PNERR_5065_SECRET_KEY_MISSING ,
                                    "ULS configuration failed. Secret Key not configured. "
                                );
    /**
     * An JSON error occurred .
     * Please contact support with error details.
     */
    public static final int         PNERR_5066_INVALID_JSON                 =       5066;
    public static final PubnubError PNERROBJ_5066_INVALID_JSON              =
            new PubnubError     (
                                    PNERR_5066_INVALID_JSON ,
                                    "Invalid JSON . " +
                                    "Please contact support with error details."
                                );
    /**
     * An JSON error occurred .
     * Please contact support with error details.
     */
    public static final int         PNERR_5067_INVALID_JSON                 =       5067;
    public static final PubnubError PNERROBJ_5067_INVALID_JSON              =
            new PubnubError     (
                                    PNERR_5061_INVALID_JSON ,
                                    "Invalid JSON . " +
                                    "Please contact support with error details."
                                );
    /**
     * An JSON error occurred .
     * Please contact support with error details.
     */
    public static final int         PNERR_5068_INVALID_JSON                 =       5068;
    public static final PubnubError PNERROBJ_5068_INVALID_JSON              =
            new PubnubError     (
                                    PNERR_5068_INVALID_JSON ,
                                    "Invalid JSON . " +
                                    "Please contact support with error details."
                                );

    /**
     * An JSON error occurred .
     * Please contact support with error details.
     */
    public static final int         PNERR_5069_INVALID_JSON                 =       5069;
    public static final PubnubError PNERROBJ_5069_INVALID_JSON              =
            new PubnubError     (
                                    PNERR_5068_INVALID_JSON ,
                                    "Invalid JSON . " +
                                    "Please contact support with error details."
                                );
    /**
     * An ULS singature error occurred .
     * Please contact support with error details.
     */
    public static final int         PNERR_5071_ULSSIGN_ERROR                =       5071;
    public static final PubnubError PNERROBJ_5071_ULSSIGN_ERROR             =
            new PubnubError     (
                                    PNERR_5071_ULSSIGN_ERROR ,
                                    "Invalid Signature . " +
                                    "Please contact support with error details."
                                );
    /**
     * An ULS singature error occurred .
     * Please contact support with error details.
     */
    public static final int         PNERR_5072_ULSSIGN_ERROR                =       5072;
    public static final PubnubError PNERROBJ_5072_ULSSIGN_ERROR             =
            new PubnubError     (
                                    PNERR_5072_ULSSIGN_ERROR ,
                                    "Invalid Signature . " +
                                    "Please contact support with error details."
                                );

    /**
     * An ULS singature error occurred .
     * Please contact support with error details.
     */
    public static final int         PNERR_5073_ULSSIGN_ERROR                =       5073;
    public static final PubnubError PNERROBJ_5073_ULSSIGN_ERROR             =
            new PubnubError     (
                                    PNERR_5073_ULSSIGN_ERROR ,
                                    "Invalid Signature . " +
                                    "Please contact support with error details."
                                );
    /**
     * An ULS singature error occurred .
     * Please contact support with error details.
     */
    public static final int         PNERR_5074_ULSSIGN_ERROR                =       5074;
    public static final PubnubError PNERROBJ_5074_ULSSIGN_ERROR             =
            new PubnubError     (
                                    PNERR_5074_ULSSIGN_ERROR ,
                                    "Invalid Signature . " +
                                    "Please contact support with error details."
                                );

    /**
     * Please verify if network is reachable
     */
    public static final int         PNERR_5075_NETWORK_ERROR                 =       5075;
    public static final PubnubError PNERROBJ_5075_NETWORK_ERROR              =
            new PubnubError     (
                                    PNERR_5075_NETWORK_ERROR ,
                                    "Netowork Error. " +
                                    "Please verify if network is reachable."
                                );


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
