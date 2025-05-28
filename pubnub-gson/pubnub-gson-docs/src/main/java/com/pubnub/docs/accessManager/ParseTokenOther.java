package com.pubnub.docs.accessManager;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.docs.SnippetBase;

public class ParseTokenOther extends SnippetBase {
    private void parseToken() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/access-manager#basic-usage-3

        PubNub pubnub = createPubNub();

        // snippet.parseToken
        pubnub.parseToken("qEF2AkF0Gmgi5mVDdHRsGQU5Q3Jlc6VEY2hhbqFnc3BhY2UwMQhDZ3JwoENzcGOgQ3VzcqBEdXVpZKFmdXNlcjAxGCBDcGF0pURjaGFuoWdzcGFjZS4qAUNncnCgQ3NwY6BDdXNyoER1dWlkoWZ1c2VyLioYIERtZXRhoER1dWlkbmF1dGhvcml6ZWRVc2VyQ3NpZ1ggkOSK0vQY5LFE5IHctQ6rGokqHbRH8EopbQRGAbU7Zfo=");
        // snippet.end

        // snippet.parseTokenResult
          /*
          * {
          "version": 2,
          "timestamp": 1747117669,
          "ttl": 1337,
          "authorizedUUID": "authorizedUser",
          "resources": {
            "channels": {
              "space01": {
                "read": false,
                "write": false,
                "manage": false,
                "delete": true,
                "get": false,
                "update": false,
                "join": false
              }
            },
            "channelGroups": {},
            "uuids": {
              "user01": {
                "read": false,
                "write": false,
                "manage": false,
                "delete": false,
                "get": true,
                "update": false,
                "join": false
              }
            }
          },
          "patterns": {
            "channels": {
              "space.*": {
                "read": true,
                "write": false,
                "manage": false,
                "delete": false,
                "get": false,
                "update": false,
                "join": false
              }
            },
            "channelGroups": {},
            "uuids": {
              "user.*": {
                "read": false,
                "write": false,
                "manage": false,
                "delete": false,
                "get": true,
                "update": false,
                "join": false
              }
            }
          },
          "meta": {}
        }
          */
        // snippet.end

    }
} 