package com.pubnub.docs.accessManager

import com.pubnub.docs.SnippetBase

class RevokeTokenOther : SnippetBase() {
    private fun revokeToken() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/access-manager#revoke-token

        val pubnub = createPubNub()

        // snippet.revokeToken
        pubnub.revokeToken("p0thisAkFl043rhDdHRsCkNyZXisRGNoYW6hanNlY3JldAFDZ3Jwsample3KgQ3NwY6BDcGF0pERjaGFuoENnctokenV")
        // snippet.end
    }
}
