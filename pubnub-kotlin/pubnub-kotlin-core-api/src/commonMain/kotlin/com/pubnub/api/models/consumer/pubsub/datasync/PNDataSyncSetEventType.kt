package com.pubnub.api.models.consumer.pubsub.datasync

/**
 * Whether a DataSync realtime Set event (`e=5`) is a first write ([CREATE]) or a change to an
 * existing object ([UPDATE]).
 *
 * The backend never mislabels a first write as [UPDATE] or vice versa — the value is reliable.
 * However, realtime delivery is **at-least-once with no server-side de-duplication**: the same
 * logical change (including a [CREATE]) can be delivered more than once. Handle events idempotently,
 * keyed on the object `id` plus `eTag`/`updatedAt`; do not assume a [CREATE] fires exactly once.
 *
 * (`delete` is not a value here — deletes arrive as `PNDelete…` leaves, not Set leaves. A verb this
 * SDK does not recognize arrives as `PNUnknownDataSyncEventMessage`, not a Set leaf, so this enum has
 * no unknown entry.)
 */
enum class PNDataSyncSetEventType {
    CREATE,
    UPDATE,
}
