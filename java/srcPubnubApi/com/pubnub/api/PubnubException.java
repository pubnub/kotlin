package com.pubnub.api;

import org.json.JSONObject;

/**
 * PubnubException is thrown by various Pubnub APIs
 *
 * @author PubnubCore
 */
public class PubnubException extends Exception {
    private String errormsg = "";
    private PubnubError pubnubError = PubnubError.PNERROBJ_PUBNUB_ERROR;
    private JSONObject jso;
    private String response;


    /**
     * Constructor for PubnubException Class with error message as argument
     *
     * @param s
     *            Error message
     */
    public PubnubException(String s) {
        this.errormsg = s;
    }

    /**
     * Constructor for PubnubException Class with error message as argument
     *
     * @param pubnubError
     *            Error message
     */
    public PubnubException(PubnubError pubnubError) {
        this.pubnubError = pubnubError;
    }

    /**
     * Constructor for PubnubException Class with error message as argument
     *
     * @param s
     *            Error message
     */
    public PubnubException(PubnubError pubnubError, String s) {
        this.errormsg = s;
        this.pubnubError = pubnubError;
    }

    /**
     * Constructor for PubnubException Class with error message as argument
     *
     * @param pubnubError
     * @param s
     *            Error message
     * @param response
     * @param jso
     */
    public PubnubException(PubnubError pubnubError, String s, String response, JSONObject jso) {
        this.errormsg = s;
        this.pubnubError = pubnubError;
        this.jso = jso;
        this.response = response;
    }

    /**
     * Constructor for PubnubException Class with error message as argument
     *
     * @param pubnubError
     * @param response
     * @param jso
     */
    public PubnubException(PubnubError pubnubError, String response, JSONObject jso) {
        this.pubnubError = pubnubError;
        this.jso = jso;
        this.response = response;
    }
    
    
    /**
     * Read the exception error message
     *
     * @return String
     */
    public String toString() {
        String msg = pubnubError.toString();
        if (errormsg.length() > 0 )
            msg = msg + " . " + errormsg;
        return msg;
    }

    public PubnubError getPubnubError() {
        return pubnubError;
    }
    
    public String getErrorResponse() {
    	return response;
    }
    public JSONObject getErrorJsonObject() {
    	return jso;
    }
    
}
