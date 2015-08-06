package com.pubnub.api;

import org.json.JSONObject;


interface PubnubSyncInterfacePam {

    public JSONObject   pamAudit();

    public JSONObject	pamAudit(String channel);

    public JSONObject	pamAudit(String channel, String auth_key);

    public JSONObject	pamAuditChannelGroup(String group);

    public JSONObject	pamAuditChannelGroup(String group, String auth_key);

    public JSONObject	pamGrant(String channel, boolean read, boolean write);

    public JSONObject	pamGrant(String channel, boolean read, boolean write, int ttl);

    public JSONObject	pamGrant(String channel, String auth_key, boolean read, boolean write);

    public JSONObject	pamGrant(String channel, String auth_key, boolean read, boolean write, int ttl);

    public JSONObject	pamGrantChannelGroup(String group, boolean read, boolean management);

    public JSONObject	pamGrantChannelGroup(String group, boolean read, boolean management, int ttl);

    public JSONObject	pamGrantChannelGroup(String group, String auth_key, boolean read, boolean management);

    public JSONObject	pamGrantChannelGroup(String group, String auth_key, boolean read, boolean management, int ttl);

    public JSONObject	pamRevoke(String channel);

    public JSONObject	pamRevoke(String channel, String auth_key);

    public JSONObject	pamRevokeChannelGroup(String group);

    public JSONObject	pamRevokeChannelGroup(String group, String auth_key);

}
