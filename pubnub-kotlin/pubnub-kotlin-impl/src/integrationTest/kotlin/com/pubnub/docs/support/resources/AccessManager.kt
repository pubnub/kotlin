package com.pubnub.docs.support.resources

import com.pubnub.docs.support.AreaResources
import com.pubnub.docs.support.DataSyncNames

/**
 * Names of the access-manager group. Channels the grants name and that a token client publishes to (nothing is stored, the
 * janitor still clears their history), the user ids that tokens are bound to, and the DataSync ids of the grant samples.
 * `docs-am-untouched` and `docs-am-untouched-product` are fresh names that no grant touches, for the negative checks.
 */
object AccessManager {
    val resources =
        AreaResources(
            channels =
                listOf(
                    "channel-a",
                    "channel-b",
                    "channel-c",
                    "channel-d",
                    "channel-e",
                    "channel-summer-sale",
                    "token-demo-channel",
                    "restricted-channel",
                    "docs-am-room",
                    "docs-am-revoke-room",
                    "docs-am-untouched",
                ),
            channelGroups = listOf("channel-group-b"),
            users =
                listOf(
                    "my-authorized-uuid",
                    "uuid-c",
                    "uuid-d",
                    "user-alice",
                    "client-user",
                    "server-admin",
                    "myServerUserId",
                    "myUserId",
                    "demoUserId",
                    "docs-am-alice",
                    "docs-am-revokee",
                    "docs-am-lister",
                    "docs-am-stranger",
                ),
            dataSync =
                DataSyncNames(
                    entities = listOf("product-sneaker-42", "docs-am-untouched-product"),
                    relationships = listOf("rel-bob-owns-sneaker-42"),
                    memberships = listOf("membership-alice-summer-sale"),
                    users = listOf("user-alice"),
                    channels = listOf("channel-summer-sale"),
                ),
        )
}
