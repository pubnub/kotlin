## [v5.1.0](https://github.com/pubnub/kotlin/releases/tag/v5.1.0)
December 16 2020

[Full Changelog](https://github.com/pubnub/kotlin/compare/v5.0.2...v5.1.0)

- Files support includes sending, downloading, listing, deleting together with notifications about file events. 
- New methods can set and remove memberships/channelMembers in one call. 
- New field `page` can be returned from the backend in case there's more data to fetch for the original query. This new field can be used directly in new `fetchMessages` and `getMessagesActions` method versions. The old versions in which paging information was in separate arguments has been deprecated. 
- FetchMessages has a default limit of 100 for single-channel call. 
- Make PNMessageActionResultevent accessible (no longer internal). 
- Method `addMemberships` has been deprecated and shouldn't be used in the future. 


