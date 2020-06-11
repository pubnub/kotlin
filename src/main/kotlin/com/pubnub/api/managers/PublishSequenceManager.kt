package com.pubnub.api.managers

internal class PublishSequenceManager(private val maxSequence: Int) {

    private var nextSequence = 0

    internal fun nextSequence(): Int {
        synchronized(nextSequence) {
            if (maxSequence == nextSequence) {
                nextSequence = 1
            } else {
                nextSequence++
            }
            return nextSequence
        }
    }
}