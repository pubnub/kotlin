plugins {
    kotlin("jvm") version "2.0.21"
}

// todo modify

group = "com.pubnub"
version = "10.4.2"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.aeonbits.owner:owner:1.0.12")
    implementation("ch.qos.logback:logback-classic:1.4.11")
    api(project(":pubnub-kotlin:pubnub-kotlin-api"))  // todo is this fine?
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}