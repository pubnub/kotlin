package com.pubnub.api.models.consumer.push.payload

import com.pubnub.api.enums.PNPushEnvironment

class PushPayloadHelper {
    var commonPayload: Map<String, Any>? = null

    @Deprecated(
        replaceWith = ReplaceWith("fcmPayloadV2"),
        message = "The legacy GCM/FCM payload is deprecated and will" +
            "be removed in the next major release. Use `fcmPayloadV2` with the `FCMPayloadV2` message body instead."
    )
    var fcmPayload: FCMPayload? = null
    var fcmPayloadV2: FCMPayloadV2? = null
    var mpnsPayload: MPNSPayload? = null
    var apnsPayload: APNSPayload? = null

    fun build(): Map<String, Any> {
        return mutableMapOf<String, Any>().apply {
            apnsPayload?.let {
                it.toMap().run {
                    if (isNotEmpty()) {
                        put("pn_apns", this)
                    }
                }
            }
            fcmPayload?.let {
                it.toMap().run {
                    if (isNotEmpty()) {
                        put("pn_gcm", this)
                    }
                }
            }
            fcmPayloadV2?.let {
                it.toMap().run {
                    if (isNotEmpty()) {
                        put("pn_fcm", this)
                    }
                }
            }
            mpnsPayload?.let {
                it.toMap().run {
                    if (isNotEmpty()) {
                        put("pn_mpns", this)
                    }
                }
            }
            commonPayload?.let { putAll(it) }
        }
    }

    class APNSPayload : PushPayloadSerializer {
        var aps: APS? = null
        var apns2Configurations: List<APNS2Configuration>? = null
        var custom: Map<String, Any>? = null

        override fun toMap(): Map<String, Any> {
            return mutableMapOf<String, Any>().apply {
                aps?.let {
                    it.toMap().run {
                        if (this.isNotEmpty()) {
                            put("aps", this)
                        }
                    }
                }
                apns2Configurations?.let { put("pn_push", it.map { it.toMap() }.filter { it.isNotEmpty() }.toList()) }
                custom?.let { putAll(it) }
            }
        }

        class APS : PushPayloadSerializer {
            var alert: Any? = null
            var badge: Int? = null
            var sound: String? = null

            override fun toMap(): Map<String, Any> {
                return mutableMapOf<String, Any>().apply {
                    alert?.let { put("alert", it) }
                    badge?.let { put("badge", it) }
                    sound?.let { put("sound", it) }
                }
            }
        }

        class APNS2Configuration : PushPayloadSerializer {
            var collapseId: String? = null
            var expiration: String? = null
            var targets: List<Target>? = null
            var version: String? = null
            var authMethod: APNS2AuthMethod? = null

            enum class APNS2AuthMethod {
                TOKEN,
                CERT,
                CERTIFICATE;

                override fun toString(): String {
                    return name.lowercase()
                }
            }

            override fun toMap(): Map<String, Any> {
                return mutableMapOf<String, Any>().apply {
                    collapseId?.let { put("collapse_id", it) }
                    expiration?.let { put("expiration", it) }
                    targets?.let {
                        it.map { it.toMap() }.filter { it.isNotEmpty() }.toList().run {
                            if (this.isNotEmpty()) {
                                put("targets", this)
                            }
                        }
                    }
                    version?.let { put("version", it) }
                    authMethod?.let { put("auth_method", it.toString()) }
                }
            }

            class Target : PushPayloadSerializer {
                var topic: String? = null
                var excludeDevices: List<String>? = null
                var environment: PNPushEnvironment? = null

                override fun toMap(): Map<String, Any> {
                    return mutableMapOf<String, Any>().apply {
                        topic?.let { put("topic", it) }
                        excludeDevices?.let { put("excluded_devices", it) }
                        environment?.let { put("environment", it.name.lowercase()) }
                    }
                }
            }
        }
    }

    class MPNSPayload : PushPayloadSerializer {
        var count: Int? = null
        var backTitle: String? = null
        var title: String? = null
        var backContent: String? = null
        var type: String? = null
        var custom: Map<String, Any>? = null

        override fun toMap(): Map<String, Any> {
            return mutableMapOf<String, Any>().apply {
                count?.let { put("count", it) }
                backTitle?.let { put("back_title", it) }
                title?.let { put("title", it) }
                backContent?.let { put("back_content", it) }
                type?.let { put("type", it) }
                custom?.let { putAll(it) }
            }
        }
    }

    class FCMPayload : PushPayloadSerializer {
        var custom: Map<String, Any>? = null
        var data: Map<String, Any>? = null
        var notification: Notification? = null

        override fun toMap(): Map<String, Any> {
            return mutableMapOf<String, Any>().apply {
                custom?.let { putAll(it) }
                data?.let {
                    if (it.isNotEmpty()) {
                        put("data", it)
                    }
                }
                notification?.let {
                    it.toMap().run {
                        if (this.isNotEmpty()) {
                            put("notification", this)
                        }
                    }
                }
            }
        }

        class Notification : PushPayloadSerializer {
            var title: String? = null
            var body: String? = null
            var image: String? = null

            override fun toMap(): Map<String, Any> {
                return mutableMapOf<String, Any>().apply {
                    title?.let { put("title", it) }
                    body?.let { put("body", it) }
                    image?.let { put("image", it) }
                }
            }
        }
    }

    class FCMPayloadV2 : PushPayloadSerializer {
        var data: Map<String, String>? = null
        var notification: Notification? = null
        var android: AndroidConfig? = null
        var webpush: WebpushConfig? = null
        var apns: ApnsConfig? = null
        var fcmOptions: FcmOptions? = null

        override fun toMap(): Map<String, Any> {
            return mutableMapOf<String, Any>().apply {
                data?.let {
                    if (it.isNotEmpty()) {
                        put("data", it)
                    }
                }
                notification?.let {
                    it.toMap().let { map ->
                        if (map.isNotEmpty()) {
                            put("notification", map)
                        }
                    }
                }
                android?.let {
                    it.toMap().let { map ->
                        if (map.isNotEmpty()) {
                            put("android", map)
                        }
                    }
                }
                webpush?.let {
                    it.toMap().let { map ->
                        if (map.isNotEmpty()) {
                            put("webpush", map)
                        }
                    }
                }
                apns?.let {
                    it.toMap().let { map ->
                        if (map.isNotEmpty()) {
                            put("apns", map)
                        }
                    }
                }
                fcmOptions?.let {
                    it.toMap().let { map ->
                        if (map.isNotEmpty()) {
                            put("fcm_options", map)
                        }
                    }
                }
            }
        }

        class WebpushConfig : PushPayloadSerializer {
            var headers: Map<String, String>? = null
            var data: Map<String, String>? = null
            var notification: Map<String, Any>? = null
            var fcmOptions: WebpushFcmOptions? = null

            class WebpushFcmOptions : PushPayloadSerializer {
                var link: String? = null
                var analyticsLabel: String? = null

                override fun toMap(): Map<String, Any> {
                    return buildMap {
                        link?.let { put("link", it) }
                        analyticsLabel?.let { put("analytics_label", it) }
                    }
                }
            }

            override fun toMap(): Map<String, Any> {
                return buildMap {
                    headers?.let { put("headers", it) }
                    data?.let { put("data", it) }
                    notification?.let { put("notification", it) }
                    fcmOptions?.let { put("fcm_options", it.toMap()) }
                }
            }
        }

        class ApnsConfig : PushPayloadSerializer {
            var headers: Map<String, String>? = null
            var payload: Map<String, String>? = null
            var fcmOptions: ApnsFcmOptions? = null

            class ApnsFcmOptions : PushPayloadSerializer {
                var analyticsLabel: String? = null
                var image: String? = null

                override fun toMap(): Map<String, Any> {
                    return buildMap {
                        analyticsLabel?.let { put("analytics_label", it) }
                        image?.let { put("image", it) }
                    }
                }
            }

            override fun toMap(): Map<String, Any> {
                return buildMap {
                    headers?.let { put("headers", it) }
                    payload?.let { put("payload", it) }
                    fcmOptions?.let { put("fcm_options", it.toMap()) }
                }
            }
        }

        class FcmOptions : PushPayloadSerializer {
            var analyticsLabel: String? = null

            override fun toMap(): Map<String, Any> {
                return buildMap {
                    analyticsLabel?.let { put("analytics_label", it) }
                }
            }
        }

        class AndroidConfig : PushPayloadSerializer {
            var collapseKey: String? = null
            var priority: AndroidMessagePriority = AndroidMessagePriority.NORMAL
            var ttl: String? = null
            var restrictedPackageName: String? = null
            var data: Map<String, Any>? = null
            var notification: AndroidNotification? = null
            var fcmOptions: AndroidFcmOptions? = null
            var directBootOk: Boolean = false

            class AndroidFcmOptions : PushPayloadSerializer {
                var analyticsLabel: String? = null

                override fun toMap(): Map<String, Any> {
                    return buildMap {
                        analyticsLabel?.let { put("analytics_label", it) }
                    }
                }
            }

            class AndroidNotification : PushPayloadSerializer {
                var title: String? = null
                var body: String? = null
                var icon: String? = null
                var color: String? = null
                var sound: String? = null
                var tag: String? = null
                var clickAction: String? = null
                var bodyLocKey: String? = null
                var bodyLocArgs: List<String>? = null
                var titleLocKey: String? = null
                var titleLocArgs: List<String>? = null
                var channelId: String? = null
                var ticker: String? = null
                var sticky: Boolean = false
                var eventTime: String? = null
                var localOnly: Boolean? = null
                var notificationPriority: NotificationPriority = NotificationPriority.PRIORITY_DEFAULT
                var defaultSound: Boolean? = null
                var defaultVibrateTimings: Boolean? = null
                var defaultLightSettings: Boolean? = null
                var vibrateTimings: List<String>? = null
                var visibility: Visibility? = null
                var notificationCount: Int? = null
                var lightSettings: LightSettings? = null
                var image: String? = null

                class LightSettings : PushPayloadSerializer {
                    var color: Color? = null
                    var lightOnDuration: String? = null
                    var lightOffDuration: String? = null

                    class Color(val red: Float, val green: Float, val blue: Float, val alpha: Float) : PushPayloadSerializer {
                        override fun toMap(): Map<String, Any> {
                            return buildMap {
                                put("red", red)
                                put("green", green)
                                put("blue", blue)
                                put("alpha", alpha)
                            }
                        }
                    }

                    override fun toMap(): Map<String, Any> {
                        return buildMap {
                            color?.let { put("color", it.toMap()) }
                            lightOnDuration?.let { put("light_on_duration", it) }
                            lightOffDuration?.let { put("light_off_duration", it) }
                        }
                    }
                }

                enum class Visibility {
                    PRIVATE,
                    PUBLIC,
                    SECRET;

                    override fun toString(): String {
                        return name.lowercase()
                    }
                }

                enum class NotificationPriority {
                    PRIORITY_MIN,
                    PRIORITY_LOW,
                    PRIORITY_DEFAULT,
                    PRIORITY_HIGH,
                    PRIORITY_MAX
                }

                override fun toMap(): Map<String, Any> = buildMap {
                    title?.let { put("title", it) }
                    body?.let { put("body", it) }
                    icon?.let { put("icon", it) }
                    color?.let { put("color", it) }
                    sound?.let { put("sound", it) }
                    tag?.let { put("tag", it) }
                    clickAction?.let { put("click_action", it) }
                    bodyLocKey?.let { put("body_loc_key", it) }
                    bodyLocArgs?.let { put("body_loc_args", it) }
                    titleLocKey?.let { put("title_loc_key", it) }
                    titleLocArgs?.let { put("title_loc_args", it) }
                    channelId?.let { put("channel_id", it) }
                    ticker?.let { put("ticker", it) }
                    put("sticky", sticky)
                    eventTime?.let { put("event_time", it) }
                    localOnly?.let { put("local_only", it) }
                    put("notification_priority", notificationPriority.name)
                    defaultSound?.let { put("default_sound", it) }
                    defaultVibrateTimings?.let { put("default_vibrate_timings", it) }
                    defaultLightSettings?.let { put("default_light_settings", it) }
                    vibrateTimings?.let { put("vibrate_timings", it) }
                    visibility?.let { put("visibility", it.name) }
                    notificationCount?.let { put("notification_count", it) }
                    lightSettings?.let { put("light_settings", it.toMap()) }
                    image?.let { put("image", it) }
                }
            }

            enum class AndroidMessagePriority {
                NORMAL,
                HIGH;

                override fun toString(): String {
                    return name.lowercase()
                }
            }

            override fun toMap(): Map<String, Any> {
                return buildMap {
                    collapseKey?.let { put("collapse_key", it) }
                    put("priority", priority.name)
                    ttl?.let { put("ttl", it) }
                    restrictedPackageName?.let { put("restricted_package_name", it) }
                    data?.let { put("data", it) }
                    notification?.let { put("notification", it.toMap()) }
                    fcmOptions?.let { put("fcm_options", it.toMap()) }
                    put("direct_boot_ok", directBootOk)
                }
            }
        }

        class Notification : PushPayloadSerializer {
            var title: String? = null
            var body: String? = null
            var image: String? = null

            override fun toMap(): Map<String, Any> {
                return mutableMapOf<String, Any>().apply {
                    title?.let { put("title", it) }
                    body?.let { put("body", it) }
                    image?.let { put("image", it) }
                }
            }
        }
    }
}
