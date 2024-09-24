//[pubnub-kotlin-api](../../index.md)/[com.pubnub.kmp](index.md)/[alsoAsync](also-async.md)

# alsoAsync

[common]\
fun &lt;[T](also-async.md)&gt; [PNFuture](-p-n-future/index.md)&lt;[T](also-async.md)&gt;.[alsoAsync](also-async.md)(action: ([T](also-async.md)) -&gt; [PNFuture](-p-n-future/index.md)&lt;*&gt;): [PNFuture](-p-n-future/index.md)&lt;[T](also-async.md)&gt;

Execute a second PNFuture after this PNFuture completes successfully, and return the *original* value of this PNFuture after the second PNFuture completes successfully.

Failures are propagated to the resulting PNFuture.
