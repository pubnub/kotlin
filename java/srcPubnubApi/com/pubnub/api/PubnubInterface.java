package com.pubnub.api;

interface PubnubInterface {

    /**
     * This method unsets auth key.
     *
     */
    public void unsetAuthKey();

    /**
     * This method returns unique identifier.
     * 
     * @return Unique Identifier .
     */
    public String uuid();

    /**
     * This method sets auth key.
     *
     * @param authKey
     *            . 0 length string or null unsets auth key
     */
    public void setAuthKey(String authKey);

    /**
     * Sets domain value, default is "pubnub.com"
     *
     * @param domain
     *            Domain value
     */
    public void setDomain(String domain);

    /**
     * Sets origin value, default is "pubsub"
     *
     * @param origin
     *            Origin value
     */
    public void setOrigin(String origin);

    /**
     * Gets current UUID
     *
     * @return uuid current UUID value for Pubnub client
     */
    public String getUUID();

    /**
     * Returns domain
     * 
     * @return domain
     */
    public String getDomain();

    /**
     * This method returns auth key. Return null if not set
     *
     * @return Auth Key. null if auth key not set
     */
    public String getAuthKey();

    /**
     * Sets value for UUID
     *
     * @param uuid
     *            UUID value for Pubnub client
     */
    public void setUUID(String uuid);

}
