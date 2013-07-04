package com.pubnub.api;


/**
 *  PubnubException is thrown by various Pubnub APIs
 * 
 *  @author PubnubCore
 */
public class PubnubException extends Exception {

	/**
	 *  Constructor for PubnubException Class with error message as argument
	 * 
	 *  @param s
	 *             Error message
	 */
	public PubnubException(String s) {
	}

	/**
	 *  Constructor for PubnubException Class with error message as argument
	 * 
	 *  @param pubnubError
	 *             Error message
	 */
	public PubnubException(PubnubError pubnubError) {
	}

	/**
	 *  Constructor for PubnubException Class with error message as argument
	 * 
	 *  @param s
	 *             Error message
	 */
	public PubnubException(PubnubError pubnubError, String s) {
	}

	/**
	 *  Read the exception error message
	 * 
	 *  @return String
	 */
	public String toString() {
	}

	public PubnubError getPubnubError() {
	}
}
