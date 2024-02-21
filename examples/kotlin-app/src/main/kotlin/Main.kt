import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.UserId

fun main() {
    val pubnub: PubNub = PubNub(PNConfiguration(UserId("abc")))
    pubnub.publish("aaa", "bbb")
}