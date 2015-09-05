package com.pubnub.api;

/**
 * Created by work1 on 06/08/15.
 */
interface PubnubSyncInterfacePush {
    /**
     *
     * @param channel
     * @param gcmRegistrationId
	 * @return
     *      Response of method call. Can also be an error response.
     */
    Object	enablePushNotificationsOnChannel(String channel, String gcmRegistrationId);

    /**
     *
     * @param channels
     * @param gcmRegistrationId
	 * @return
     *      Response of method call. Can also be an error response.
     */
    Object	enablePushNotificationsOnChannels(String[] channels, String gcmRegistrationId);

    /**
     *
     * @param channel
     * @param gcmRegistrationId
	 * @return
     *      Response of method call. Can also be an error response.
     */
    Object	disablePushNotificationsOnChannel(String channel, String gcmRegistrationId);

    /**
     *
     * @param channels
     * @param gcmRegistrationId
	 * @return
     *      Response of method call. Can also be an error response.
     */
    Object	disablePushNotificationsOnChannels(String[] channels, String gcmRegistrationId);

}
