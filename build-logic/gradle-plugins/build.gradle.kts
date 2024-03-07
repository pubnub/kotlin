plugins {
    `java-gradle-plugin`
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain(8)
}

dependencies {
    compileOnly(gradleKotlinDsl())
    compileOnly(libs.nexus.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("pubnubShared") {
            id = "pubnub.shared"
            implementationClass = "com.pubnub.gradle.PublishPlugin"
        }
    }
}