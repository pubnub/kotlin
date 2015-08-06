package com.pubnub.api;

/**
 * Created by work1 on 06/08/15.
 */
interface PubnubAsyncInterfacePush {

    void	enablePushNotificationsOnChannel(String channel, String gcmRegistrationId, Callback callback);

    void	enablePushNotificationsOnChannels(String[] channels, String gcmRegistrationId, Callback callback);

    void	disablePushNotificationsOnChannel(String channel, String gcmRegistrationId, Callback callback);

    void	disablePushNotificationsOnChannels(String[] channels, String gcmRegistrationId, Callback callback);
}
