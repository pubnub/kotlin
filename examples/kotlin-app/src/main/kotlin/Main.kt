
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.internal.PubNubImpl

fun main() {
    val pubnub: PubNub = PubNub.create(PNConfiguration(UserId("abc")))
    pubnub.addListener(object : SubscribeCallback() {
        /**
         * Receive status updates from the PubNub client.
         *
         * @see [PNStatus]
         *
         * @param pubnub The client instance which has this listener attached.
         * @param status Wrapper around the actual message content.
         */
        override fun status(pubnub: PubNub, status: PNStatus) {
            TODO("Not yet implemented")
        }

        /**
         * Receive messages at subscribed channels.
         *
         * @see [PubNubImpl.subscribe]
         *
         * @param pubnub The client instance which has this listener attached.
         * @param result Wrapper around the actual message content.
         */
        override fun message(pubnub: PubNub, result: PNMessageResult) {
            super.message(pubnub, result)
        }

        /**
         * Receive presence events for channels subscribed with presence enabled via
         * passing [com.pubnub.api.v2.subscriptions.SubscriptionOptions.receivePresenceEvents]
         * in [com.pubnub.api.v2.entities.BaseChannel.subscription].
         *
         * @param pubnub The client instance which has this listener attached.
         * @param result Wrapper around a presence event.
         */
        override fun presence(pubnub: PubNub, result: PNPresenceEventResult) {
            super.presence(pubnub, result)
        }

        /**
         * Receive signals at subscribed channels.
         *
         * @see [PubNubImpl.signal]
         *
         * @param pubnub The client instance which has this listener attached.
         * @param result Wrapper around a signal event.
         */
        override fun signal(pubnub: PubNub, result: PNSignalResult) {
            super.signal(pubnub, result)
        }

        /**
         * Receive message actions for messages in subscribed channels.
         *
         * @param pubnub The client instance which has this listener attached.
         * @param result Wrapper around a message action event.
         */
        override fun messageAction(pubnub: PubNub, result: PNMessageActionResult) {
            super.messageAction(pubnub, result)
        }

        /**
         * Receive channel metadata and UUID metadata events in subscribed channels.
         *
         * @param pubnub The client instance which has this listener attached.
         * @param result Wrapper around the object event.
         */
        override fun objects(pubnub: PubNub, result: PNObjectEventResult) {
            super.objects(pubnub, result)
        }

        /**
         * Receive file events in subscribed channels.
         *
         * @param pubnub The client instance which has this listener attached.
         * @param result Wrapper around the file event.
         */
        override fun file(pubnub: PubNub, result: PNFileEventResult) {
            super.file(pubnub, result)
        }
    })
}