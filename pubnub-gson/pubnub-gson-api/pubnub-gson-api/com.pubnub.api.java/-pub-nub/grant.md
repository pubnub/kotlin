//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java](../index.md)/[PubNub](index.md)/[grant](grant.md)

# grant

[jvm]\
abstract fun [grant](grant.md)(): [Grant](../../com.pubnub.api.java.endpoints.access/-grant/index.md)

This function establishes access permissions for PubNub Access Manager (PAM) by setting the `read` or `write` attribute to `true`. A grant with `read` or `write` set to `false` (or not included) will revoke any previous grants with `read` or `write` set to `true`.

Permissions can be applied to any one of three levels:

- 
   Application level privileges are based on `subscribeKey` applying to all associated channels.
- 
   Channel level privileges are based on a combination of `subscribeKey` and `channel` name.
- 
   User level privileges are based on the combination of `subscribeKey`, `channel`, and `auth_key`.
