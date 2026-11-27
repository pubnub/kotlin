package com.pubnub.docs.support.tools

import com.pubnub.docs.support.Env
import com.pubnub.docs.support.Janitor
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * The janitor as a stand-alone run: `scripts/dev/docs-snippets.sh janitor`. Cleans the listed names on every role that has
 * keys, revokes the application level grant of the Access Manager keysets and waits until they refuse a client without a
 * token again. Needs keys. It is in no group, so a group run never starts it.
 */
class JanitorCommandTest {
    @Test
    fun runJanitor() {
        Env.load()
        Env.printStatus()
        val roles = Env.keysetRoles.filter { Env.missing(it).isEmpty() }
        assertTrue(roles.isNotEmpty(), "no keyset role has keys. Set the docs env names or LIVE_ENV_FILE, see the tier README.")
        assertTrue(Janitor.runAndPrint(roles), "the janitor reported failures, see the janitor lines above")
        roles.filter { Env.hasSecret(it) }.forEach { role ->
            println("janitor: application level grant of $role is closed after ${Janitor.verifyApplicationGrantClosed(role)}s")
        }
    }
}
