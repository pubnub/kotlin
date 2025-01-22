//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.endpoints.objects_api.uuid](../index.md)/[SetUUIDMetadata](index.md)/[ifMatchesEtag](if-matches-etag.md)

# ifMatchesEtag

[jvm]\
abstract fun [ifMatchesEtag](if-matches-etag.md)(@Nullableetag: @Nullable[String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)): [SetUUIDMetadata](index.md)

Optional entity tag from a previously received `PNUUIDMetadata`. The request will fail if this parameter is specified and the ETag value on the server doesn't match.

#### Return

this builder

#### Parameters

jvm

| | |
|---|---|
| etag | from PNUUIDMetadata |
