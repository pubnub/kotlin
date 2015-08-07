package com.pubnub.api;

import org.json.JSONObject;


interface PubnubSyncInterfacePam {
    /**
     *
     * @return
     */
    public JSONObject   pamAudit();

    /**
     *
     * @param channel
     * @return
     */
    public JSONObject	pamAudit(String channel);

    /**
     *
     * @param channel
     * @param auth_key
     * @return
     */
    public JSONObject	pamAudit(String channel, String auth_key);

    /**
     *
     * @param group
     * @return
     */
    public JSONObject	pamAuditChannelGroup(String group);

    /**
     *
     * @param group
     * @param auth_key
     * @return
     */
    public JSONObject	pamAuditChannelGroup(String group, String auth_key);

    /**
     *
     * @param channel
     * @param read
     * @param write
     * @return
     */
    public JSONObject	pamGrant(String channel, boolean read, boolean write);

    /**
     *
     * @param channel
     * @param read
     * @param write
     * @param ttl
     * @return
     */
    public JSONObject	pamGrant(String channel, boolean read, boolean write, int ttl);

    /**
     *
     * @param channel
     * @param auth_key
     * @param read
     * @param write
     * @return
     */
    public JSONObject	pamGrant(String channel, String auth_key, boolean read, boolean write);

    /**
     *
     * @param channel
     * @param auth_key
     * @param read
     * @param write
     * @param ttl
     * @return
     */
    public JSONObject	pamGrant(String channel, String auth_key, boolean read, boolean write, int ttl);

    /**
     *
     * @param group
     * @param read
     * @param management
     * @return
     */
    public JSONObject	pamGrantChannelGroup(String group, boolean read, boolean management);

    /**
     *
     * @param group
     * @param read
     * @param management
     * @param ttl
     * @return
     */
    public JSONObject	pamGrantChannelGroup(String group, boolean read, boolean management, int ttl);

    /**
     *
     * @param group
     * @param auth_key
     * @param read
     * @param management
     * @return
     */
    public JSONObject	pamGrantChannelGroup(String group, String auth_key, boolean read, boolean management);

    /**
     *
     * @param group
     * @param auth_key
     * @param read
     * @param management
     * @param ttl
     * @return
     */
    public JSONObject	pamGrantChannelGroup(String group, String auth_key, boolean read, boolean management, int ttl);

    /**
     *
     * @param channel
     * @return
     */
    public JSONObject	pamRevoke(String channel);

    /**
     *
     * @param channel
     * @param auth_key
     * @return
     */
    public JSONObject	pamRevoke(String channel, String auth_key);

    /**
     *
     * @param group
     * @return
     */
    public JSONObject	pamRevokeChannelGroup(String group);

    /**
     *
     * @param group
     * @param auth_key
     * @return
     */
    public JSONObject	pamRevokeChannelGroup(String group, String auth_key);

}
