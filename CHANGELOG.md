## v8.0.0
February 22 2024

#### Added
- A new version of subscription and presence handling is enabled by default (`enableEventEngine` flag is set to `true`). Please consult the documentation for new PNStatus values that are emitted for subscriptions, as code changes might be required to support this change.
- Channels, ChannelGroups, ChannelMetadata and UserMetadata.

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


