package com.pubnub.docs.offline;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pubnub.docs.support.Env;
import com.pubnub.docs.support.Redact;
import com.pubnub.docs.support.Tier;
import com.pubnub.docs.support.Wait;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

/** Proves that the Java tree can use the Kotlin support code. Needs no keys and no network. */
class JavaSupportTest {
    @Test
    void javaTreeSeesTheKotlinSupport() {
        List<String> status = Env.status();
        assertEquals(4, status.size());
        for (String line : status) {
            assertTrue(line.matches("java [a-z]+: (set|missing: [A-Z_, ]+)"), "an env status line has an unexpected shape");
        }
        assertTrue(Tier.INSTANCE.getJavaDir().isDirectory());
    }

    /** The gson task runs with docs.sdk=java, so the Java tests use the Java keysets (JAVA_ env names). */
    @Test
    void javaTestsUseTheJavaKeysets() {
        assertEquals("java", Env.getSdk());
        assertEquals("JAVA_PUBLISH_KEY", Env.actualName("PUBLISH_KEY"));
        assertEquals("JAVA_PAM_SECRET_KEY", Env.actualName("PAM_SECRET_KEY"));
        assertEquals("DOCS_SNIPPETS_JAVA_DS_KEYSET_ID", Env.actualName("DOCS_SNIPPETS_DS_KEYSET_ID"));
        assertEquals("PUBNUB_ADMIN_API_KEY", Env.actualName("PUBNUB_ADMIN_API_KEY"));
        assertEquals("LIVE_GROUP", Env.actualName("LIVE_GROUP"));
        for (String name : Env.missing("open")) {
            assertTrue(name.startsWith("JAVA_"), "a missing Java key must be named with the JAVA_ prefix");
        }
    }

    @Test
    void javaTestsCanRedactAndWait() {
        Redact.register("a-java-test-secret-value");
        assertFalse(Redact.scrub("x a-java-test-secret-value y").contains("secret"));
        AtomicInteger calls = new AtomicInteger();
        Wait.eventuallyRun("the second call passes", () -> {
            if (calls.incrementAndGet() < 2) {
                throw new AssertionError("not yet");
            }
        });
        assertEquals(2, calls.get());
    }
}
