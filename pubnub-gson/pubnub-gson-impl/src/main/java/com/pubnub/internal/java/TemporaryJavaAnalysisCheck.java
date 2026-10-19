package com.pubnub.internal.java;

// TEMPORARY: deliberately bad code to verify that PMD and SpotBugs fail the build. Delete before merging.
final class TemporaryJavaAnalysisCheck {
    // PMD bestpractices > UnusedPrivateField
    private final String unusedField = "unused";

    int nullDereference() {
        String value = null;
        // SpotBugs NP_ALWAYS_NULL
        return value.length();
    }
}
