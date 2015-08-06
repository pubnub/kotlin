package com.pubnub.api;

/**
 * Created by work1 on 06/08/15.
 */
public interface PubnubAsyncInterfacePam {

    public void	pamAudit(Callback callback);

    public void	pamAudit(String channel, Callback callback);

    public void	pamAudit(String channel, String auth_key, Callback callback);

    public void	pamAuditChannelGroup(String group, Callback callback);

    public void	pamAuditChannelGroup(String group, String auth_key, Callback callback);

    public void	pamGrant(String channel, boolean read, boolean write, Callback callback);

    public void	pamGrant(String channel, boolean read, boolean write, int ttl, Callback callback);

    public void	pamGrant(String channel, String auth_key, boolean read, boolean write, Callback callback);

    public void	pamGrant(String channel, String auth_key, boolean read, boolean write, int ttl, Callback callback);

    public void	pamGrantChannelGroup(String group, boolean read, boolean management, Callback callback);

    public void	pamGrantChannelGroup(String group, boolean read, boolean management, int ttl, Callback callback);

    public void	pamGrantChannelGroup(String group, String auth_key, boolean read, boolean management, Callback callback);

    public void	pamGrantChannelGroup(String group, String auth_key, boolean read, boolean management, int ttl, Callback callback);

    public void	pamRevoke(String channel, Callback callback);

    public void	pamRevoke(String channel, String auth_key, Callback callback);

    public void	pamRevokeChannelGroup(String group, Callback callback);

    public void	pamRevokeChannelGroup(String group, String auth_key, Callback callback);
}
