package com.pubnub.api;

/**
 * PubnubException is thrown by various Pubnub APIs
 *
 * @author PubnubCore
 */
public class PubnubException extends Exception {
    private String errormsg = "";
    private PubnubError pubnubError = PubnubError.PNERROBJ_PUBNUB_ERROR;


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

}
