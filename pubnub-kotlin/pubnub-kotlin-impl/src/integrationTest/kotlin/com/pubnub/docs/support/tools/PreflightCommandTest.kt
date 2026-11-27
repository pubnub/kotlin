package com.pubnub.docs.support.tools

import com.pubnub.docs.support.Env
import com.pubnub.docs.support.Preflight
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * The preflight as a stand-alone run: `scripts/dev/docs-snippets.sh preflight`. Names every missing keyset feature. Needs
 * keys. It is in no group, so a group run never starts it.
 */
class PreflightCommandTest {
    @Test
    fun runPreflight() {
        Env.load()
        Env.printStatus()
        val roles = Env.keysetRoles.filter { Env.missing(it).isEmpty() }
        assertTrue(roles.isNotEmpty(), "no keyset role has keys. Set the docs env names or LIVE_ENV_FILE, see the tier README.")
        assertTrue(Preflight.runAndPrint(roles), "the preflight found missing keyset features, see the preflight lines above")
    }
}
