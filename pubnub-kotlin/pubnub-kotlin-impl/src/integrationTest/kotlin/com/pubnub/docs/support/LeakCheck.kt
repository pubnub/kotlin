package com.pubnub.docs.support

/** Finds threads that would keep a JVM alive after the run: alive, not daemon, and not part of the test runner. */
object LeakCheck {
    // "/127.0.0.1:1 to /127.0.0.1:2 workers" are the message threads between the Gradle test worker and the Gradle daemon.
    private val runnerThreads = listOf("main", "Test worker", "Gradle", "Daemon", "Thread-", "Attach Listener", "junit-", "/127.0.0.1")

    /**
     * The threads of clients that were destroyed a moment ago are still winding down (an executor shut down with
     * `shutdownNow` needs a few milliseconds, more on a small CI machine), so the check waits up to [waitMs] for them to
     * end before it calls them leaked. A real leak (a client that is never destroyed) stays alive, so the longer wait
     * only delays that failure and does not hide it.
     */
    @JvmStatic
    @JvmOverloads
    fun nonDaemonThreadsAfterWinddown(waitMs: Long = 20_000): List<String> {
        val deadline = System.nanoTime() + waitMs * 1_000_000
        var found = nonDaemonThreads()
        while (found.isNotEmpty() && System.nanoTime() < deadline) {
            Thread.sleep(100)
            found = nonDaemonThreads()
        }
        return found
    }

    @JvmStatic
    fun nonDaemonThreads(): List<String> =
        Thread.getAllStackTraces().keys
            .filter { it.isAlive && !it.isDaemon && it !== Thread.currentThread() }
            .map { it.name }
            .filter { name -> runnerThreads.none { name.startsWith(it) } }
            .sorted()
}
