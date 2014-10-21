package com.pubnub.api;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ChannelGroupParsingTest {
    ChannelGroup channelGroup;

    @Test
    public void testParseGroup() throws PubnubException {
        String name = "europe";
        channelGroup = new ChannelGroup(name);
        assertNull(channelGroup.namespace);
        assertEquals(name, channelGroup.group);
    }

    @Test
    public void testParseNamespacedGroup() throws PubnubException {
        String name = "news:europe";
        channelGroup = new ChannelGroup(name);
        assertEquals("news", channelGroup.namespace);
        assertEquals("europe", channelGroup.group);
    }

    @Test
    public void testParseNamespace() throws PubnubException {
        String name = "news:";
        channelGroup = new ChannelGroup(name);
        assertEquals("news", channelGroup.namespace);
        assertNull(channelGroup.group);
    }
}
