package com.pubnub.api;

/**
 * Sender missing exception will be raised when publish method of
 * PnMessage object is invoked and sender object has not been 
 * attached to PnMessage
 * @author Pubnub
 *
 */
public class PubnubSenderMissingException extends PubnubException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * PubnubSenderMissingException constructor
	 * @param message
	 * 		Error message
	 */
	public PubnubSenderMissingException(String message) {
		super(message);
	}
	
	/**
	 * PubnubSenderMissingException constructor
	 * @param error
	 * 		Error object
	 */
	public PubnubSenderMissingException(PubnubError error) {
		super(error);
	}
	
	/**
	 * PubnubSenderMissingException constructor
	 * @param error
	 * 		Error object
	 * @param message
	 * 		Error message
	 */
	public PubnubSenderMissingException(PubnubError error, String message) {
		super(error, message);
	}

}
