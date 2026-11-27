package com.pubnub.docs.support.resources

import com.pubnub.docs.support.AreaResources

/** Names of the getting-started group: configuration, logging, encryption, miscellaneous. */
object GettingStarted {
    val resources =
        AreaResources(
            channels =
                listOf(
                    // channels the logging samples publish to
                    "log4j-demo-channel",
                    "log4j2-demo-channel",
                    "logging-demo-channel",
                    "slf4j-simple-demo-channel",
                    // channels of the configuration and miscellaneous samples
                    "myChannel",
                    "foo",
                    // a notification channel id inside the push payload sample, never a PubNub channel
                    "meeting_reminders",
                    // channels that only the arrangement of the tests uses (never in a sample)
                    "docs-config-filter",
                    "docs-config-crypto",
                    "docs-config-pam",
                    "docs-log-content",
                    "docs-log-pam",
                    "docs-misc-destroy",
                    "docs-misc-ch1",
                    "docs-misc-ch2",
                ),
            channelGroups =
                listOf(
                    "docs-misc-group",
                ),
            users =
                listOf(
                    // configuration page, Kotlin and Java
                    "abc",
                    "new-user-id",
                    "yourUserId",
                    "demoUserId",
                    "basic-user-id",
                    "advanced-user-id",
                    "encrypted-user-id",
                    "myUserId",
                    // logging page
                    "log4jDemoUser",
                    "log4j2DemoUser",
                    "loggingDemoUser",
                    "slf4jSimpleDemoUser",
                    // miscellaneous page
                    "push-helper-demo-user",
                    // users of the arrangement
                    "docs-config-sender",
                    "docs-config-receiver",
                    "docs-config-writer",
                    "docs-misc-control",
                ),
        )
}
