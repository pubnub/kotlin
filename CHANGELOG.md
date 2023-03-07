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


