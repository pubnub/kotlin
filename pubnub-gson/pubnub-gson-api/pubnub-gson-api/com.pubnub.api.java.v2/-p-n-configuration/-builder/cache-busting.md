//[pubnub-gson-api](../../../../index.md)/[com.pubnub.api.java.v2](../../index.md)/[PNConfiguration](../index.md)/[Builder](index.md)/[cacheBusting](cache-busting.md)

# cacheBusting

[jvm]\
abstract fun [cacheBusting](cache-busting.md)(cacheBusting: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [PNConfiguration.Builder](index.md)

abstract val [cacheBusting](cache-busting.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

If operating behind a misbehaving proxy, allow the client to shuffle the subdomains.

Defaults to `false`.
