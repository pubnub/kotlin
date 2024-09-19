package com.pubnub.api.v2

import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.retry.RetryConfiguration

interface BasePNConfigurationOverride {
    interface Builder {
        /**
         * The subscribe key from the admin panel.
         */
        val subscribeKey: String

        /**
         * The publish key from the admin panel (only required if publishing).
         */
        val publishKey: String

        /**
         * The secret key from the admin panel (only required for modifying/revealing access permissions).
         *
         * Keep away from Android.
         */
        val secretKey: String

        /**
         * Retry configuration for requests.
         *  Defaults to [RetryConfiguration.None].
         *
         *  Use [RetryConfiguration.Linear] to set retry with linear delay interval
         *  Use [RetryConfiguration.Exponential] to set retry with exponential delay interval
         *  Delay will vary from provided value by random value.
         */
        val retryConfiguration: RetryConfiguration

        /**
         * The user ID that the PubNub client will use.
         */
        val userId: UserId

        /**
         * Whether to include a [PubNub.instanceId] with every request.
         *
         * Defaults to `false`.
         */
        val includeInstanceIdentifier: Boolean

        /**
         * Whether to include a [PubNub.requestId] with every request.
         *
         * Defaults to `true`.
         */
        val includeRequestIdentifier: Boolean

        /**
         * If Access Manager is utilized, client will use this authKey in all restricted requests.
         */
        val authKey: String

        /**
         * CryptoModule is responsible for handling encryption and decryption.
         * If set, all communications to and from PubNub will be encrypted.
         */
        val cryptoModule: CryptoModule?

        /**
         * How long before the client gives up trying to connect with the server.
         *
         * The value is in seconds.
         *
         * Defaults to 5.
         */
        val connectTimeout: Int

        /**
         * For non subscribe operations (publish, herenow, etc),
         * This property relates to a read timeout that is applied from the moment the connection between a client
         * and the server has been successfully established. It defines a maximum time of inactivity between two
         * data packets when waiting for the server’s response.
         *
         * The value is in seconds.
         *
         * Defaults to 10.
         */
        val nonSubscribeReadTimeout: Int
    }
}
