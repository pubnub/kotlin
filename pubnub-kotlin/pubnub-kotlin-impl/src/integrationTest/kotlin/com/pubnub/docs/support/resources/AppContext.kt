package com.pubnub.docs.support.resources

import com.pubnub.docs.support.AreaResources

/**
 * Names of the app-context group: users, channel metadata, memberships. Every id that a sample of the objects page or of the
 * user and channel metadata entity pages sets is listed, so the janitor removes the metadata, the memberships and the
 * presence of exactly these names.
 */
object AppContext {
    val resources =
        AreaResources(
            channels =
                listOf(
                    "myChannel",
                    "myChannelToSet",
                    "myChannelToRemove",
                    "myChannelId",
                    "myChannelName",
                    "channel01",
                    "Channel1on1",
                    "channelId",
                    "channelId01",
                    "channelId02",
                    "testChannelId",
                    "team-chat",
                    "user-123",
                ),
            users =
                listOf(
                    "uuid-metadata-demo-user",
                    "user-1",
                    "user-2",
                    "user-3",
                    "user01",
                    "userId",
                    "userId01",
                    "userId02",
                    "userId1",
                    "userId2",
                    "myUserId",
                    "demoUserId",
                    "channel-metadata-entity-demo-user",
                    "user-metadata-entity-demo-user",
                    "user-123",
                    // the sample's own client of the Kotlin and of the Java page, and the second clients of a test
                    "docs-appctx-kotlin",
                    "docs-appctx-java",
                    "docs-reader",
                    "docs-receiver",
                ),
        )
}
