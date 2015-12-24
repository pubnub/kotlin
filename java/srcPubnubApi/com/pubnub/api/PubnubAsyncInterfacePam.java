package com.pubnub.api;

/**
 * Created by work1 on 06/08/15.
 */
interface PubnubAsyncInterfacePam {
    /** PAM Audit
     * @param callback
     */
    public void	pamAudit(Callback callback);

    /** PAM audit by channel
     * @param channel
     * @param callback
     */
    public void	pamAudit(String channel, Callback callback);

    /** PAM audit by channel and auth key
     * @param channel
     * @param auth_key
     * @param callback
     */
    public void	pamAudit(String channel, String auth_key, Callback callback);

    /**
     *
     * @param group
     * @param callback
     */
    public void	pamAuditChannelGroup(String group, Callback callback);

    /**
     *
     * @param group
     * @param auth_key
     * @param callback
     */
    public void	pamAuditChannelGroup(String group, String auth_key, Callback callback);

    /**
     *
     * @param channel
     * @param read
     * @param write
     * @param callback
     */
    public void	pamGrant(String channel, boolean read, boolean write, Callback callback);

    /**
     *
     * @param channel
     * @param read
     * @param write
     * @param ttl
     * @param callback
     */
    public void	pamGrant(String channel, boolean read, boolean write, int ttl, Callback callback);

    /**
     *
     * @param channel
     * @param auth_key
     * @param read
     * @param write
     * @param callback
     */
    public void	pamGrant(String channel, String auth_key, boolean read, boolean write, Callback callback);

    /**
     *
     * @param channel
     * @param auth_key
     * @param read
     * @param write
     * @param ttl
     * @param callback
     */
    public void	pamGrant(String channel, String auth_key, boolean read, boolean write, int ttl, Callback callback);

    /**
     *
     * @param group
     * @param read
     * @param management
     * @param callback
     */
    public void	pamGrantChannelGroup(String group, boolean read, boolean management, Callback callback);

    /**
     *
     * @param group
     * @param read
     * @param management
     * @param ttl
     * @param callback
     */
    public void	pamGrantChannelGroup(String group, boolean read, boolean management, int ttl, Callback callback);

    /**
     *
     * @param group
     * @param auth_key
     * @param read
     * @param management
     * @param callback
     */
    public void	pamGrantChannelGroup(String group, String auth_key, boolean read, boolean management, Callback callback);

    /**
     *
     * @param group
     * @param auth_key
     * @param read
     * @param management
     * @param ttl
     * @param callback
     */
    public void	pamGrantChannelGroup(String group, String auth_key, boolean read, boolean management, int ttl, Callback callback);

    /** PAM revoke by channel
     * @param channel
     * @param callback
     */
    public void	pamRevoke(String channel, Callback callback);

    /** PAM revoke by channel and auth key
     * @param channel
     * @param auth_key
     * @param callback
     */
    public void	pamRevoke(String channel, String auth_key, Callback callback);

    /**
     *
     * @param group
     * @param callback
     */
    public void	pamRevokeChannelGroup(String group, Callback callback);

    /**
     *
     * @param group
     * @param auth_key
     * @param callback
     */
    public void	pamRevokeChannelGroup(String group, String auth_key, Callback callback);
}
