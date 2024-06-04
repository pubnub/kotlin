package com.pubnub.api.models.consumer.push.payload

import com.pubnub.api.enums.PNPushEnvironment
import java.beans.Visibility
import java.util.Locale

class PushPayloadHelper {
    var commonPayload: Map<String, Any>? = null
    var fcmPayload: FCMPayload? = null
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
                        environment?.let { put("environment", it.name.lowercase(Locale.getDefault())) }
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
        var data: Map<String, Any>? = null
        var notification: Notification? = null
        var android: AndroidConfig? = null
        var webpush: WebpushConfig? = null

        class WebpushConfig : PushPayloadSerializer {
            var headers: Map<String, String>? = null
            var data: Map<String, String>? = null
            var notification: Map<String, Any>? = null
            var fcm_options: WebpushFcmOptions? = null

            class WebpushFcmOptions : PushPayloadSerializer {
                var link: String? = null
                var analytics_label: String? = null

                override fun toMap(): Map<String, Any> {
                    TODO("Not yet implemented")
                }
            }

            override fun toMap(): Map<String, Any> {
                TODO("Not yet implemented")
            }

        }

        var apns: ApnsConfig? = null

        class ApnsConfig : PushPayloadSerializer {
            var headers: Map<String, String>? = null
            var payload: Map<String, String>? = null
            var fcm_options: ApnsFcmOptions? = null

            class ApnsFcmOptions : PushPayloadSerializer {
                var analytics_label: String? = null
                var image: String? = null

                override fun toMap(): Map<String, Any> {
                    TODO("Not yet implemented")
                }

            }

            override fun toMap(): Map<String, Any> {
                TODO("Not yet implemented")
            }

        }

        var fcm_options: FcmOptions? = null

        class FcmOptions: PushPayloadSerializer {
            var analytics_label: String? = null

            override fun toMap(): Map<String, Any> {
                TODO("Not yet implemented")
            }

        }

        var token: String? = null
            set(value) {
                topic = null
                condition = null
                field = value
            }
        var topic: String? = null
            set(value) {
                token = null
                condition = null
                field = value
            }
        var condition: String? = null
            set(value) {
                topic = null
                token = null
                field = value
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
                var analytics_label: String? = null
                override fun toMap(): Map<String, Any> {
                    return buildMap {
                        analytics_label?.let { put("analytics_label", it) }
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
                var body_loc_key: String? = null
                var body_loc_args: List<String>? = null
                var title_loc_key: String? = null
                var title_loc_args: List<String>? = null
                var channel_id: String? = null
                var ticker: String? = null
                var sticky: Boolean = false
                var event_time: String? = null
                var local_only: Boolean? = null
                var notification_priority: NotificationPriority = NotificationPriority.PRIORITY_DEFAULT
                var default_sound: Boolean? = null
                var default_vibrate_timings: Boolean? = null
                var default_light_settings: Boolean? = null
                var vibrate_timings: List<String>? = null
                var visibility: Visibility? = null
                var notification_count: Int? = null
                var light_settings: LightSettings? = null
                var image: String? = null

                class LightSettings : PushPayloadSerializer {
                    //                    var color : {
//                        "red": number,
//                        "green": number,
//                        "blue": number,
//                        "alpha": number
//                    }
                    var light_on_duration: String? = null
                    var light_off_duration: String? = null

                    override fun toMap(): Map<String, Any> {
                        TODO("Not yet implemented")
                    }

                }

                enum class Visibility {
                    VISIBILITY_UNSPECIFIED,
                    PRIVATE,
                    PUBLIC,
                    SECRET,
                }

                enum class NotificationPriority {
                    PRIORITY_UNSPECIFIED,
                    PRIORITY_MIN,
                    PRIORITY_LOW,
                    PRIORITY_DEFAULT,
                    PRIORITY_HIGH,
                    PRIORITY_MAX
                }

                override fun toMap(): Map<String, Any> =
                    buildMap {
                        title?.let { put("title", it) }
                        body?.let { put("body", it) }
                        icon?.let { put("icon", it) }
                        color?.let { put("color", it) }
                        sound?.let { put("sound", it) }
                        tag?.let { put("tag", it) }
                        body_loc_key?.let { put("body_loc_key", it) }
                        body_loc_args?.let { put("body_loc_args", it) }
                        title_loc_key?.let { put("title_loc_key", it) }
                        title_loc_args?.let { put("title_loc_args", it) }
                        channel_id?.let { put("channel_id", it) }
                        ticker?.let { put("ticker", it) }
                        put("sticky", sticky)
                        event_time?.let { put("event_time", it) }
                        local_only?.let { put("local_only", it) }
                        put("notification_priority", notification_priority.name)
                        default_sound?.let { put("default_sound", it) }
                        default_vibrate_timings?.let { put("default_vibrate_timings", it) }
                        default_light_settings?.let { put("default_light_settings", it) }
                        vibrate_timings?.let { put("vibrate_timings", it) }
                        visibility?.let { put("visibility", it.name) }
                        notification_count?.let { put("notification_count", it) }
                        light_settings?.let { put("light_settings", it.toMap()) }
                        image?.let { put("image", it) }
                    }

            }


            enum class AndroidMessagePriority {
                NORMAL, HIGH
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
}
