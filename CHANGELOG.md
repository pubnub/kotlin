## [v5.1.2](https://github.com/pubnub/kotlin/releases/tag/v5.1.2)
March 9 2021

[Full Changelog](https://github.com/pubnub/kotlin/compare/v5.1.1...v5.1.2)

- In some specific timing conditions subscription loop could loose reference to one of the retrofit call and we would loose posibility to control it. In the meantime we'd start yet another subscription call. One of them is obviously not necessary Synchronization has been improved so it's no longer possible. 
- It was not possible to properly cancel the OkHttp connection when Google Security Provider (ProviderInstaller) is being used. 


