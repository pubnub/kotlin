//[pubnub-kotlin-api](../../index.md)/[com.pubnub.kmp](index.md)/[onFailureHandler](on-failure-handler.md)

# onFailureHandler

[apple]\
fun &lt;[T](on-failure-handler.md)&gt; [Consumer](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2.callbacks/-consumer/index.md)&lt;[Result](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[T](on-failure-handler.md)&gt;&gt;.[onFailureHandler](on-failure-handler.md)(mapper: (<!---  GfmCommand {"@class":"org.jetbrains.dokka.gfm.ResolveLinkGfmCommand","dri":{"packageName":"","classNames":"<Error class: unknown class>","callable":null,"target":{"@class":"org.jetbrains.dokka.links.PointingToDeclaration"},"extra":null}} --->&lt;Error class: unknown class&gt;<!--- --->?) -&gt; [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html) = { error: NSError? -&gt;
        if (error is KMPError) {
            PubNubException(error.statusCode().toInt(), error.localizedDescription, null)
        } else {
            PubNubException(errorMessage = error?.localizedDescription)
        }
    }): (<!---  GfmCommand {"@class":"org.jetbrains.dokka.gfm.ResolveLinkGfmCommand","dri":{"packageName":"","classNames":"<Error class: unknown class>","callable":null,"target":{"@class":"org.jetbrains.dokka.links.PointingToDeclaration"},"extra":null}} --->&lt;Error class: unknown class&gt;<!--- --->?) -&gt; [Unit](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-unit/index.html)
