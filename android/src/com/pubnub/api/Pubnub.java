package com.pubnub.api;

import java.util.Hashtable;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;

/**
 * Pubnub object facilitates querying channels for messages and listening on
 * channels for presence/message events
 *
 * @author Pubnub
 *
 */

public class Pubnub extends PubnubCoreShared {
	
	String gcmRegistrationId = null;
    /**
     * Pubnub Constructor
     *
     * @param publish_key
     *            Publish Key
     * @param subscribe_key
     *            Subscribe Key
     * @param secret_key
     *            Secret Key
     * @param cipher_key
     *            Cipher Key
     * @param ssl_on
     *            SSL on ?
     */
    public Pubnub(String publish_key, String subscribe_key, String secret_key,
                  String cipher_key, boolean ssl_on) {
        super(publish_key, subscribe_key, secret_key, cipher_key, ssl_on);
    }

    /**
     * Pubnub Constructor
     *
     * @param publish_key
     *            Publish key
     * @param subscribe_key
     *            Subscribe Key
     * @param secret_key
     *            Secret Key
     * @param ssl_on
     *            SSL on ?
     */
    public Pubnub(String publish_key, String subscribe_key, String secret_key,
                  boolean ssl_on) {
        super(publish_key, subscribe_key, secret_key, "", ssl_on);
    }

    /**
     * Pubnub Constructor
     *
     * @param publish_key
     *            Publish Key
     * @param subscribe_key
     *            Subscribe Key
     */
    public Pubnub(String publish_key, String subscribe_key) {
        super(publish_key, subscribe_key, "", "", false);
    }

    /**
     * @param publish_key
     *            Publish Key
     * @param subscribe_key
     *            Subscribe Key
     * @param ssl
     */
    public Pubnub(String publish_key, String subscribe_key, boolean ssl) {
        super(publish_key, subscribe_key, "", "", ssl);
    }

    /**
     * @param publish_key
     * @param subscribe_key
     * @param secret_key
     */
    public Pubnub(String publish_key, String subscribe_key, String secret_key) {
        super(publish_key, subscribe_key, secret_key, "", false);
    }

    /**
     * @param publish_key
     * @param subscribe_key
     * @param secret_key
     * @param cipher_key
     */
    public Pubnub(String publish_key, String subscribe_key, String secret_key, String cipher_key) {
        super(publish_key, subscribe_key, secret_key, cipher_key, false);
    }

    /**
    *
    * Constructor for Pubnub Class
    *
    * @param publish_key
    *            Publish Key
    * @param subscribe_key
    *            Subscribe Key
    * @param secret_key
    *            Secret Key
    * @param cipher_key
    *            Cipher Key
    * @param ssl_on
    *            SSL enabled ?
    * @param initialization_vector
    *            Initialization vector
    */

    public Pubnub(String publish_key, String subscribe_key,
                  String secret_key, String cipher_key, boolean ssl_on, String initialization_vector) {
        super(publish_key, subscribe_key, secret_key, cipher_key, ssl_on, initialization_vector);
    }

    protected String getUserAgent() {
        return "(Android " + android.os.Build.VERSION.RELEASE +
               "; " + android.os.Build.MODEL +
               " Build) PubNub-Java/Android/" + VERSION;
    }
    
    public void setGcmRegistrationId(String registrationId) {
    	this.gcmRegistrationId = registrationId;
    }
    
    public void registerDeviceOnChannel(final String channel, final Callback callback) throws PubnubGcmRegistrationIdMissingException {
    	final Callback cb = getWrappedCallback(callback);

    	if (this.gcmRegistrationId == null || this.gcmRegistrationId.length() <= 0)
    		throw new PubnubGcmRegistrationIdMissingException("Registration Id Missing");
    	
        Hashtable parameters = PubnubUtil.hashtableClone(params);
        String[] urlargs = null;
        urlargs = new String[]{ getPubnubUrl(), "v1", "push", "sub-key",
                this.SUBSCRIBE_KEY, "devices", this.gcmRegistrationId
        };
        

        parameters.put("type", "gcm");
        parameters.put("add", channel);

        HttpRequest hreq = new HttpRequest(urlargs, parameters,
                new ResponseHandler() {
            public void handleResponse(HttpRequest hreq, String response) {
                JSONArray jsarr;
                try {
                    jsarr = new JSONArray(response);
                } catch (JSONException e) {
                    handleError(hreq,
                            PubnubError.getErrorObject(PubnubError.PNERROBJ_INVALID_JSON, 1, response));
                    return;
                }
                callback.successCallback("", jsarr);
            }

            public void handleError(HttpRequest hreq, PubnubError error) {
                callback.errorCallback("", error);
                return;
            }
        });

        _request(hreq, nonSubscribeManager);
    }
    public void unregisterDeviceFromChannel(final String channel, final Callback callback) throws PubnubGcmRegistrationIdMissingException {
    	final Callback cb = getWrappedCallback(callback);

    	if (this.gcmRegistrationId == null || this.gcmRegistrationId.length() <= 0)
    		throw new PubnubGcmRegistrationIdMissingException("Registration Id Missing");
    	
        Hashtable parameters = PubnubUtil.hashtableClone(params);
        String[] urlargs = null;
        urlargs = new String[]{ getPubnubUrl(), "v1", "push", "sub-key",
                this.SUBSCRIBE_KEY, "devices", this.gcmRegistrationId
        };
        

        parameters.put("type", "gcm");
        parameters.put("remove", channel);

        HttpRequest hreq = new HttpRequest(urlargs, parameters,
                new ResponseHandler() {
            public void handleResponse(HttpRequest hreq, String response) {
                JSONArray jsarr;
                try {
                    jsarr = new JSONArray(response);
                } catch (JSONException e) {
                    handleError(hreq,
                            PubnubError.getErrorObject(PubnubError.PNERROBJ_INVALID_JSON, 1, response));
                    return;
                }
                callback.successCallback("", jsarr);
            }

            public void handleError(HttpRequest hreq, PubnubError error) {
                callback.errorCallback("", error);
                return;
            }
        });

        _request(hreq, nonSubscribeManager);
    }
    public void getChannelListforDevice(final Callback callback) {
    	final Callback cb = getWrappedCallback(callback);
        Hashtable parameters = PubnubUtil.hashtableClone(params);
        String[] urlargs = null;
        urlargs = new String[]{ getPubnubUrl(), "v1", "push", "sub-key",
                this.SUBSCRIBE_KEY, "devices", this.gcmRegistrationId
        };
        

        parameters.put("type", "gcm");

        HttpRequest hreq = new HttpRequest(urlargs, parameters,
                new ResponseHandler() {
            public void handleResponse(HttpRequest hreq, String response) {
                JSONArray jsarr;
                try {
                    jsarr = new JSONArray(response);
                } catch (JSONException e) {
                    handleError(hreq,
                            PubnubError.getErrorObject(PubnubError.PNERROBJ_INVALID_JSON, 1, response));
                    return;
                }
                callback.successCallback("", jsarr);
            }

            public void handleError(HttpRequest hreq, PubnubError error) {
                callback.errorCallback("", error);
                return;
            }
        });
    }
    public void unregisterAllChannelsForDevice(final Callback callback) {
    	final Callback cb = getWrappedCallback(callback);
        Hashtable parameters = PubnubUtil.hashtableClone(params);
        String[] urlargs = null;
        urlargs = new String[]{ getPubnubUrl(), "v1", "push", "sub-key",
                this.SUBSCRIBE_KEY, "devices", this.gcmRegistrationId, "remove"
        };
        

        parameters.put("type", "gcm");

        HttpRequest hreq = new HttpRequest(urlargs, parameters,
                new ResponseHandler() {
            public void handleResponse(HttpRequest hreq, String response) {
                JSONArray jsarr;
                try {
                    jsarr = new JSONArray(response);
                } catch (JSONException e) {
                    handleError(hreq,
                            PubnubError.getErrorObject(PubnubError.PNERROBJ_INVALID_JSON, 1, response));
                    return;
                }
                callback.successCallback("", jsarr);
            }

            public void handleError(HttpRequest hreq, PubnubError error) {
                callback.errorCallback("", error);
                return;
            }
        });
    }
}
