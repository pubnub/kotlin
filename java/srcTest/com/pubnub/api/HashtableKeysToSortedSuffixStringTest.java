package com.pubnub.api;

import org.junit.Test;

import java.util.Hashtable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HashtableKeysToSortedSuffixStringTest {
    private static String SUFFIX = "-pnpres";

    @Test
    public void testPlainStrings() {
        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        hashtable.put("foo", "");
        hashtable.put("bar", "");
        hashtable.put("baz", "");

        String result = PubnubUtil.hashTableKeysToSortedSuffixString(hashtable, ",", SUFFIX);
        String[] resultArray = PubnubUtil.splitString(result, ",");

        assertEquals(hashtable.size(), resultArray.length);
        assertTrue(elementsWithSuffixAreMovedToTheEnd(resultArray));
    }

    @Test
    public void testStringsWithOneSuffixedElement() {
        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        hashtable.put("foo", "");
        hashtable.put("bar" + SUFFIX, "");
        hashtable.put("bar", "");
        hashtable.put("baz", "");

        String result = PubnubUtil.hashTableKeysToSortedSuffixString(hashtable, ",", SUFFIX);
        String[] resultArray = PubnubUtil.splitString(result, ",");

        assertEquals(hashtable.size(), resultArray.length);
        assertTrue(elementsWithSuffixAreMovedToTheEnd(resultArray));
    }

    @Test
    public void testStringsWithMultipleSuffixedElement() {
        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        hashtable.put("foo" + SUFFIX, "");
        hashtable.put("foo", "");
        hashtable.put("bar", "");
        hashtable.put("bar" + SUFFIX, "");
        hashtable.put("baz" + SUFFIX, "");
        hashtable.put("baz", "");

        String result = PubnubUtil.hashTableKeysToSortedSuffixString(hashtable, ",", SUFFIX);
        String[] resultArray = PubnubUtil.splitString(result, ",");

        assertEquals(hashtable.size(), resultArray.length);
        assertTrue(elementsWithSuffixAreMovedToTheEnd(resultArray));
    }

    @Test
    public void testStringsWithOnlyPresence() {
        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        hashtable.put("bar" + SUFFIX, "");
        hashtable.put("foo" + SUFFIX, "");
        hashtable.put("baz" + SUFFIX, "");

        String result = PubnubUtil.hashTableKeysToSortedSuffixString(hashtable, ",", SUFFIX);
        String[] resultArray = PubnubUtil.splitString(result, ",");

        assertEquals(hashtable.size(), resultArray.length);
        assertTrue(elementsWithSuffixAreMovedToTheEnd(resultArray));
    }

    @Test
    public void testEmptyString() {
        Hashtable<String, String> hashtable = new Hashtable<String, String>();

        String result = PubnubUtil.hashTableKeysToSortedSuffixString(hashtable, ",", SUFFIX);
        assertEquals("", result);
    }

    private boolean elementsWithSuffixAreMovedToTheEnd(String[] input) {
        boolean presenceCatch = false;
        boolean success = true;

        for (String current : input) {

            if (presenceCatch && !current.contains(SUFFIX)) {
                success = false;
            }

            if (current.contains(SUFFIX)) {
                presenceCatch = true;
            }
        }

        return success;
    }
}
