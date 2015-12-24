package com.pubnub.api;

/**
 * Created by work1 on 06/08/15.
 */
interface PubnubAsyncInterfacePush {
    /**
     *
     * @param channel
     * @param gcmRegistrationId
     * @param callback
     */
    void	enablePushNotificationsOnChannel(String channel, String gcmRegistrationId, Callback callback);

    /**
     *
     * @param channels
     * @param gcmRegistrationId
     * @param callback
     */
    void	enablePushNotificationsOnChannels(String[] channels, String gcmRegistrationId, Callback callback);

    /**
     *
     * @param channel
     * @param gcmRegistrationId
     * @param callback
     */
    void	disablePushNotificationsOnChannel(String channel, String gcmRegistrationId, Callback callback);

    /**
     *
     * @param channels
     * @param gcmRegistrationId
     * @param callback
     */
    void	disablePushNotificationsOnChannels(String[] channels, String gcmRegistrationId, Callback callback);
}
