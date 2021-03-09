package okhttp3

internal class PNCallFactory(private val callFactory: Call.Factory) : Call.Factory {
    override fun newCall(request: Request): Call {
        return PNCall(callFactory.newCall(request) as RealCall)
    }
}
