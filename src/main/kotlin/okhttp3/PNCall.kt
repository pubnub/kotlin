package okhttp3

import org.slf4j.LoggerFactory
import java.net.SocketException

internal class PNCall(
    private val realCall: RealCall
) : Call by realCall {
    private val log = LoggerFactory.getLogger("PNCall")

    override fun cancel() {
        try {
            realCall.streamAllocation()?.connection()?.socket()?.shutdownInput()
        } catch (se: SocketException) {
            log.warn("Caught exception when canceling call", se)
        }
        realCall.cancel()
    }
}
