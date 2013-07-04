package com.pubnub.api;


/**
 *  PubnubError object is passed to errorCallback. It contains details of error, like
 *  error code, error string, and optional message
 * 
 *  @author Pubnub
 */
public class PubnubError {

	/**
	 *  Timeout Error .
	 */
	public static final int PNERR_TIMEOUT = 100;

	/**
	 */
	public static final int PNERR_PUBNUB_ERROR = 101;

	/**
	 *  Connect Exception .
	 *  Network Unreachable.
	 */
	public static final int PNERR_CONNECT_EXCEPTION = 102;

	/**
	 *  Please check network connectivity.
	 *  Please contact support with error details if issue persists.
	 */
	public static final int PNERR_HTTP_ERROR = 103;

	/**
	 *  Client Timeout .
	 */
	public static final int PNERR_CLIENT_TIMEOUT = 104;

	/**
	 *  Please verify if network is reachable
	 */
	public static final int PNERR_NETWORK_ERROR = 106;

	/**
	 *  Pubnub Exception .
	 */
	public static final int PNERR_PUBNUB_EXCEPTION = 108;

	/**
	 *  Disconnect .
	 */
	public static final int PNERR_DISCONNECT = 109;

	/**
	 *  Disconnect and Resubscribe Received .
	 */
	public static final int PNERR_DISCONN_AND_RESUB = 110;

	/**
	 *  Gateway Timeout
	 */
	public static final int PNERR_GATEWAY_TIMEOUT = 111;

	/**
	 *  Pubnub server returned HTTP 403 forbidden status code.
	 *  Happens when wrong authentication key is used .
	 */
	public static final int PNERR_FORBIDDEN = 112;

	/**
	 *  Pubnub server returned HTTP 401 unauthorized status code
	 *  Happens when authentication key is missing .
	 */
	public static final int PNERR_UNAUTHORIZED = 113;

	/**
	 *  Secret key not configured
	 */
	public static final int PNERR_SECRET_KEY_MISSING = 114;

	/**
	 *  Error while encrypting message to be published to Pubnub Cloud .
	 *  Please contact support with error details.
	 */
	public static final int PNERR_ENCRYPTION_ERROR = 115;

	/**
	 *  Decryption Error .
	 *  Please contact support with error details.
	 */
	public static final int PNERR_DECRYPTION_ERROR = 116;

	/**
	 *  Invalid Json .
	 *  Please contact support with error details.
	 */
	public static final int PNERR_INVALID_JSON = 117;

	/**
	 *  Error in opening URL .
	 *  Please contact support with error details.
	 */
	public static final int PNERR_URL_OPEN = 120;

	public final int errorCode;

	public final int errorCodeExtended;

	public PubnubError(PubnubError error, String message) {
	}

	public String toString() {
	}

	public String getErrorString() {
	}
}
