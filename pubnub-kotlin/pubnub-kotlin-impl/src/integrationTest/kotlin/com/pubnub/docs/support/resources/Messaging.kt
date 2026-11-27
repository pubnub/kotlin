package com.pubnub.docs.support.resources

import com.pubnub.docs.support.AreaResources

/** Names of the messaging group: publish and subscribe, presence, channel groups, history, message actions, files, push. */
object Messaging {
    val resources =
        AreaResources(
            channels =
                listOf(
                    // publish and subscribe, Kotlin and Java pages
                    "my_channel",
                    "my_channel01",
                    "my_channel02",
                    "other_channel",
                    "myChannel",
                    "channelName",
                    "channelName03",
                    "demo-channel",
                    "suchChannel",
                    "coolChannel",
                    "bar",
                    "ch1",
                    "ch2",
                    "ch3",
                    "awesomeChannel",
                    // a sample with a wildcard, never published to
                    "foo.*",
                    // presence
                    "demo-channel-1",
                    "demo-channel-2",
                    "coolChannel2",
                    // channel groups
                    "demo-channel-a",
                    "demo-channel-b",
                    "demo-channel-c",
                    "son",
                    // entities
                    "chat-marketing",
                    "chat-engineering",
                    "chat-support",
                    "demo-entity-channel",
                    // history and message counts
                    "channel_1",
                    "channel_2",
                    "history_channel",
                    "news",
                    "info",
                    "kotlin-ch1",
                    "kotlin-ch2",
                    // files
                    "demo-file-channel",
                ),
            channelGroups =
                listOf(
                    "my_channel_group",
                    "channelGroup",
                    "cg1",
                    "cg2",
                    "cg3",
                    "awesomeChannelGroup",
                    "demo-channel-group",
                    "demo-entity-group",
                    "family",
                    "myGroup",
                ),
            pushDevices =
                listOf(
                    // the live tokens that the tests swap for the placeholders googleDevice and appleDevice (hex, so the janitor
                    // can remove them on every gateway)
                    "f1d8c4e07a39b2c65d1e8f3a4b7c9d0e2f6a8b1c3d5e7f90a2b4c6d8e0f1a3b5",
                    // the Java sample that registers a device by itself uses this FCM token as the docs show it
                    "googleDevice",
                    "740f4707bebcf74f9b7c25d48e3358945f6aa01da5ddb387462c7eaf61bb78ad",
                ),
            users =
                listOf(
                    // publish and subscribe
                    "myUserId",
                    "yourUserId",
                    "demoUserId",
                    "publish-demo-user",
                    // the second clients of the publish and subscribe tests
                    "docs-receiver",
                    "docs-joiner",
                    // user metadata events travel on the channel with the name of the user, see the listener tests
                    "my_channel",
                    // presence, history, message actions, files and entities
                    "such_uuid",
                    "someUuid",
                    "some-other-uuid",
                    "suchUUID",
                    "add-channels-demo",
                    "channel-group-entity-demo-user",
                    "channel-entity-demo-user",
                    "file-upload-demo-user",
                    "history-demo-user",
                    "message-action-demo-user",
                    "here-now-demo-user",
                ) +
                    // the crowd that pushes the open keyset above its announce max, so that it sends interval events
                    (1..21).map { "docs-crowd-$it" },
        )
}
