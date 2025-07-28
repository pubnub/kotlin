package com.pubnub.docs.accessManager

import com.pubnub.docs.SnippetBase

class SetTokenOther : SnippetBase() {
    private fun setToken() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/access-manager#set-token

        val pubnub = createPubNub()

        // snippet.setToken
        pubnub.setToken(
            "qEF2AkF0Gmgi5mVDdHRsGQU5Q3Jlc6VEY2hhbqFnc3BhY2UwMQhDZ3JwoENzcGOgQ3VzcqBEdXVpZKFmdXNlcjAxGCBDcGF0pURjaGFuoWdzcGFjZS4qAUNncnCgQ3NwY6BDdXNyoER1dWlkoWZ1c2VyLioYIERtZXRhoER1dWlkbmF1dGhvcml6ZWRVc2VyQ3NpZ1ggkOSK0vQY5LFE5IHctQ6rGokqHbRH8EopbQRGAbU7Zfo="
        )
        // snippet.end
    }
}
