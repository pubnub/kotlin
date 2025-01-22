## v10.4.0
January 21 2025

#### Added
- A new optional parameter `ifMatchesEtag` is added to `setUUIDMetadata` and `setChannelMetadata`. When provided, the server compares the argument value with the ETag on the server and if they don't match a HTTP 412 error is returned.

## v10.3.4
January 13 2025

#### Fixed
- Internal fixes.

## v10.3.3
January 07 2025

#### Fixed
- Added validation for Leave and Heartbeat to ensure user receives an error when providing empty strings for channels or groups.

#### Modified
- Internal refactor.

## v10.3.2
December 16 2024

#### Modified
- Internal fixes.

## v10.3.1
December 12 2024

#### Modified
- Internal changes for Chat SDK.

## v10.3.0
December 05 2024

#### Added
- Added type aka. membershipType to Memberships and ChannelMembers APIs. Added also a way to specify optional data being added to the response for Membership and ChannelMembers APIs.

## v10.2.1
December 03 2024

#### Modified
- Internal changes in preparation for other releases.

## v10.2.0
November 18 2024

#### Added
- Added customMessageType to publish, signal, file, subscribe and history.

## v10.1.0
November 06 2024

#### Added
- Add extension selection and support for single file in migration_tool.
- Move shared java/kotlin APIs to a shared module.
- Added timetoken utils methods.
- Added getToken to Kotlin SDK.
- Added authToken (PAM v3) to Kotlin SDK PNConfiguration.

#### Fixed
- Fix for NullPointerException in `pubnub-gson` when grant() is called without authKeys.

## v10.0.0
September 24 2024

#### Added
- `PatchValue` is now used in objects returning optional data from the server, such as `PNChannelMetadata`, `PNUUIDMetadata` for example.
- Removed mutable `PNConfiguration` classes which were deprecated in previous releases.

#### Fixed
- A migration script is provided with this release to help with package name changes.

#### Modified
- Please consult the migration guide for JVM SDKs version 10.0.0 for required changes to your code.

## v9.2.4
August 19 2024

#### Fixed
- Fixes a crash on Android after `PubNub.destroy` is called and there are requests running.

## v9.2.3
July 29 2024

#### Fixed
- Fixed incorrect multiple callbacks (with exception) when sending files.

## v9.2.2
July 04 2024

#### Fixed
- SetState via Heartbeat fix.

## v9.2.1
July 02 2024

#### Added
- Add missing `auth_method` to APNS2 configuration.

## v9.2.0
June 11 2024

#### Added
- Add new FCMPayloadV2 for required new FCM push message format.

#### Fixed
- Disallow DTD in XML parser and enable SecureRandom.

## v9.1.1
April 15 2024

#### Fixed
- The parameter names were not saved in compiled class files and were shown as `o`, `s1` etc. in the IDE. This change fixes the SDK to correctly show parameter names.

## v9.1.0
April 09 2024

#### Added
- Added methods for publishing messages and signals on the `Channel` class. Also added new builder factories on `PubNub` with required parameters provided upfront, e.g. `PubNub.publish(message,channel)`. .
- Allow overriding certain PubNub configuration options per API call through `Endpoint.overrideConfiguration`.

#### Modified
- Remaining classes from `com.pubnub.internal` package were hidden from compilation classpath. Users should only use classes in `com.pubnub.api` package.

## v9.0.0
March 28 2024

#### Modified
- From now on, Java and Kotlin SDKs will be versioned and released together, and clients using both SDKs will get new features and bug fixes at the same time.
- RemoteAction.async() now provides a single `Result<Output>` parameter to the callback. Please see documentation for details.
- PubNub initialization must be done through `PubNub.create()`. Constructor initialization is disallowed.
- There is a new immutable PNConfiguration class with builder. The old PNConfiguration class is marked deprecated, but functional for the time being.
- Internal classes and interfaces have been moved to the `com.pubnub.internal` classes or removed from the compile classpath. You should not use them in your app. All user facing classes are in `com.pubnub.api` package.

## v8.0.0
February 22 2024

#### Added
- A new version of subscription and presence handling is enabled by default (`enableEventEngine` flag is set to `true`). Please consult the documentation for new PNStatus values that are emitted for subscriptions, as code changes might be required to support this change.
- Added support for scoped event listeners using new entity objects: Channels, ChannelGroups, ChannelMetadata and UserMetadata.

## v7.8.1
February 06 2024

#### Fixed
- Fixed transitions so that SubscribeEvenEngine can handle multiple subscribe calls executed one by one.
- Add missing Leave events with EE enabled and heartbeat = 0.



## v7.8.0
January 17 2024

#### Added
- Added `enableEventEngine`, `retryConfiguration` and `maintainPresenceState` configuration flags.

## v7.7.4
November 28 2023

#### Added
- Add `error` field to `PNFileEventResult` and set it in case of decryption failure.

## v7.7.3
November 23 2023

#### Fixed
- Handle unencrypted message in subscribe and history when crypto configured (error flag is set on message result).

## v7.7.2
November 08 2023

#### Fixed
- Fixed reading unencrypted history message when crypto is configured.



## v7.7.1
October 30 2023

#### Fixed
- Updated the JSON lib to version 20231013.
- Changed license type from MIT to PubNub Software Development Kit License.

## v7.7.0
October 16 2023

#### Added
- Add crypto module that allows configure SDK to encrypt and decrypt messages.

#### Fixed
- Improved security of crypto implementation by adding enhanced AES-CBC cryptor.

## v7.6.0
August 14 2023

#### Added
- Kotlin SDK doesn't depend on Jackson library anymore. This reduces the size of required dependencies which should result in size reduction of applications that use Kotlin SDK.

## v7.5.0
April 19 2023

#### Added
- It's now possible to return messageType in PNFetchMessageItem from fetchMessages method. Possible values are either Message or File .

## v7.4.3
April 11 2023

#### Fixed
- Do not cancel subscribe call on set state.

## v7.4.2
March 07 2023

#### Fixed
- Remove deprecation for Grant Token methods.
- Update Jackson and Json libraries to avoid vulnerabilities.

## v7.4.1
February 02 2023

#### Fixed
- Uploading an encrypted file correctly sets the content size in the multipart body.

## v7.4.0
September 20 2022

#### Added
- PNFileEventResult has messageJson field with the same JsonElement type as in other events.

## v7.3.2
July 21 2022

#### Fixed
- Gson, logback.
- Removed "create" for space as a valid permission that can be assigned to token.

## v7.3.1
June 28 2022

#### Fixed
- Renamed name to id in SpacePermissions.

## v7.3.0
June 28 2022

#### Added
- Introduced new GrantToken method with no channel groups.

## v7.2.0
June 14 2022

#### Added
- PNChannelMetadata and PNUUIDMetadata has status and type now.
- PNChannelMembership and PNMember has status now.

#### Fixed
- It's possible to sort memberships and members by nested fields.

## v7.1.0
May 23 2022

#### Added
- Upgrade okhttp library .

## v7.0.1
April 19 2022

#### Fixed
- Update Jackson library with fixes for CVE-2020-36518.

## v7.0.0
January 12 2022

#### Modified
- BREAKING CHANGES: uuid is required parameter in PNConfiguration constructor.

## v6.3.0
December 16 2021

#### Added
- Add revoke token feature.

## [v6.2.0](https://github.com/pubnub/kotlin/releases/tag/v6.2.0)
October 6 2021

[Full Changelog](https://github.com/pubnub/kotlin/compare/v6.1.0...v6.2.0)

- Acceptance tests plugged into CI pipeline. 
- Internal telemetry enhancement. 
- Meta field exposed correctly in PNToken class. 


