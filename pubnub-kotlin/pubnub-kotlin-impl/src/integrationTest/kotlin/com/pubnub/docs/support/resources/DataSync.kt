package com.pubnub.docs.support.resources

import com.pubnub.docs.support.AreaResources
import com.pubnub.docs.support.DataSyncNames

/**
 * Names of the datasync group: the DataSync page, the DataSync entity pages and the DataSync listener samples. The classes
 * (`product` v1 and `ProductOwner` v1 next to the built in User, Channel and Membership) are provisioned once and never touched
 * here. Every id below is removed by the janitor, relationships and memberships first.
 */
object DataSync {
    val resources =
        AreaResources(
            dataSync =
                DataSyncNames(
                    relationships =
                        listOf(
                            // the ProductOwner link of the relationship samples
                            "rel-bob-owns-sneaker-42",
                            // the throwaway link of the event probe in the preflight
                            "docs-probe-rel",
                        ),
                    memberships =
                        listOf(
                            "membership-alice-summer-sale",
                            // the archived membership that the filter samples must leave out
                            "membership-alice-winter-sale",
                            "docs-probe-membership",
                        ),
                    entities =
                        listOf(
                            "product-sneaker-42",
                            // a second product that the filter samples must leave out
                            "product-boot-7",
                            "docs-probe-product",
                        ),
                    users =
                        listOf(
                            "user-alice",
                            // the owning end of the ProductOwner link, and a user that the type filter must leave out
                            "seller-bob",
                            "docs-probe-user",
                        ),
                    channels =
                        listOf(
                            "channel-summer-sale",
                            // the channel of the archived membership
                            "channel-winter-sale",
                            "docs-probe-channel",
                        ),
                ),
        )
}
