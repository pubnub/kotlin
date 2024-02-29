package com.pubnub.internal

import com.pubnub.api.models.consumer.access_manager.v3.PNToken
import com.pubnub.internal.callbacks.SubscribeCallback
import com.pubnub.internal.subscribe.eventengine.configuration.EventEnginesConf
import com.pubnub.internal.v2.callbacks.InternalEventListener
import com.pubnub.internal.v2.callbacks.InternalStatusListener
import com.pubnub.internal.v2.entities.BaseChannelGroupImpl
import com.pubnub.internal.v2.entities.BaseChannelImpl
import com.pubnub.internal.v2.entities.BaseChannelMetadataImpl
import com.pubnub.internal.v2.entities.BaseUserMetadataImpl
import com.pubnub.internal.v2.subscription.BaseSubscriptionImpl
import com.pubnub.internal.v2.subscription.BaseSubscriptionSetImpl
import java.io.InputStream

class TestPubNub internal constructor(configuration: CorePNConfiguration, eventEnginesConf: EventEnginesConf = EventEnginesConf()) :
    BasePubNubImpl<
        InternalEventListener,
        BaseSubscriptionImpl<InternalEventListener>,
        BaseChannelImpl<InternalEventListener, BaseSubscriptionImpl<InternalEventListener>>,
        BaseChannelGroupImpl<InternalEventListener, BaseSubscriptionImpl<InternalEventListener>>,
        BaseChannelMetadataImpl<InternalEventListener, BaseSubscriptionImpl<InternalEventListener>>,
        BaseUserMetadataImpl<InternalEventListener, BaseSubscriptionImpl<InternalEventListener>>,
        BaseSubscriptionSetImpl<InternalEventListener, BaseSubscriptionImpl<InternalEventListener>>,
        InternalStatusListener,
        >(configuration, eventEnginesConf) {
        override fun channel(name: String): BaseChannelImpl<InternalEventListener, BaseSubscriptionImpl<InternalEventListener>> {
            TODO("Not yet implemented")
        }

        override fun channelGroup(name: String): BaseChannelGroupImpl<InternalEventListener, BaseSubscriptionImpl<InternalEventListener>> {
            TODO("Not yet implemented")
        }

        override fun channelMetadata(
            id: String,
        ): BaseChannelMetadataImpl<InternalEventListener, BaseSubscriptionImpl<InternalEventListener>> {
            TODO("Not yet implemented")
        }

        override fun userMetadata(id: String): BaseUserMetadataImpl<InternalEventListener, BaseSubscriptionImpl<InternalEventListener>> {
            TODO("Not yet implemented")
        }

        /**
         * Perform Cryptographic decryption of an input string using cipher key provided by [PNConfiguration.cipherKey].
         *
         * @param inputString String to be decrypted.
         *
         * @return String containing the decryption of `inputString` using `cipherKey`.
         * @throws PubNubException throws exception in case of failed decryption.
         */
        override fun decrypt(inputString: String): String {
            TODO("Not yet implemented")
        }

        /**
         * Perform Cryptographic decryption of an input string using a cipher key.
         *
         * @param inputString String to be decrypted.
         * @param cipherKey cipher key to be used for decryption. Default is [PNConfiguration.cipherKey]
         *
         * @return String containing the decryption of `inputString` using `cipherKey`.
         * @throws PubNubException throws exception in case of failed decryption.
         */
        override fun decrypt(
            inputString: String,
            cipherKey: String?,
        ): String {
            TODO("Not yet implemented")
        }

        /**
         * Perform Cryptographic decryption of an input stream using provided cipher key.
         *
         * @param inputStream InputStream to be encrypted.
         * @param cipherKey Cipher key to be used for decryption.
         *
         * @return InputStream containing the encryption of `inputStream` using `cipherKey`.
         * @throws PubNubException Throws exception in case of failed decryption.
         */
        override fun decryptInputStream(
            inputStream: InputStream,
            cipherKey: String?,
        ): InputStream {
            TODO("Not yet implemented")
        }

        /**
         * Perform Cryptographic encryption of an input string and a cipher key.
         *
         * @param inputString String to be encrypted.
         * @param cipherKey Cipher key to be used for encryption. Default is [PNConfiguration.cipherKey]
         *
         * @return String containing the encryption of `inputString` using `cipherKey`.
         * @throws PubNubException Throws exception in case of failed encryption.
         */
        override fun encrypt(
            inputString: String,
            cipherKey: String?,
        ): String {
            TODO("Not yet implemented")
        }

        /**
         * Perform Cryptographic encryption of an input stream using provided cipher key.
         *
         * @param inputStream InputStream to be encrypted.
         * @param cipherKey Cipher key to be used for encryption.
         *
         * @return InputStream containing the encryption of `inputStream` using `cipherKey`.
         * @throws PubNubException Throws exception in case of failed encryption.
         */
        override fun encryptInputStream(
            inputStream: InputStream,
            cipherKey: String?,
        ): InputStream {
            TODO("Not yet implemented")
        }

        override fun parseToken(token: String): PNToken {
            TODO("Not yet implemented")
        }

        override fun setToken(token: String?) {
            TODO("Not yet implemented")
        }

        /**
         * Cancel any subscribe and heartbeat loops or ongoing re-connections.
         *
         * Monitor the results in [SubscribeCallback.status]
         */
        override fun disconnect() {
            TODO("Not yet implemented")
        }

        /**
         * Unsubscribe from all channels and all channel groups
         */
        override fun unsubscribeAll() {
            TODO("Not yet implemented")
        }

        override fun subscriptionSetOf(
            subscriptions: Set<BaseSubscriptionImpl<InternalEventListener>>,
        ): BaseSubscriptionSetImpl<InternalEventListener, BaseSubscriptionImpl<InternalEventListener>> {
            TODO("Not yet implemented")
        }

        fun addListener(listener: SubscribeCallback) {
            listenerManager.addListener(listener)
        }

        /**
         * Add a listener.
         *
         * @param listener The listener to be added.
         */
        override fun addListener(listener: InternalEventListener) {
            listenerManager.addListener(listener)
        }

        /**
         * Add a listener.
         *
         * @param listener The listener to be added.
         */
        override fun addListener(listener: InternalStatusListener) {
            listenerManager.addListener(listener)
        }
    }
