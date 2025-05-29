plugins {
    alias(libs.plugins.benmanes.versions)
    id("pubnub.kotlin-library")
    id("pubnub.dokka")
    id("pubnub.shared")
}

dependencies {
    api(project(":pubnub-kotlin:pubnub-kotlin-api"))
    implementation(project(":pubnub-kotlin:pubnub-kotlin-impl"))

    implementation(kotlin("test"))
    implementation(libs.slf4j)

    // ↓ LOG4J 2 ↓
    val log4j2Version = "2.24.3"
    implementation("org.apache.logging.log4j:log4j-api:$log4j2Version")
    implementation("org.apache.logging.log4j:log4j-core:$log4j2Version")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:$log4j2Version")

    // ↓ LOGBACK ↓ If you want to use Logback instead of Log4j2, comment log4j dependencies above and uncomment the following line:
    // implementation("ch.qos.logback:logback-classic:1.3.15")
}
