plugins {
    alias(libs.plugins.benmanes.versions)
    id("pubnub.kotlin-library")
    id("pubnub.dokka")
    id("pubnub.shared")
}

dependencies {
    api(project(":pubnub-gson:pubnub-gson-api"))
    implementation(project(":pubnub-gson:pubnub-gson-impl"))

    implementation(libs.hamcrest)
    implementation(libs.slf4j)

    // ↓ SLF4J SIMPLE ↓ to use it comment out LOG4J 2  and LOGBACK dependencies
    // implementation("org.slf4j:slf4j-simple:1.7.36")

    // ↓ LOG4J 2 ↓ to use it comment out SLF4J SIMPLE and LOGBACK dependencies
    val log4j2Version = "2.24.3"
    // implementation("org.apache.logging.log4j:log4j-api:$log4j2Version")
    // implementation("org.apache.logging.log4j:log4j-core:$log4j2Version")
    // implementation("org.apache.logging.log4j:log4j-slf4j2-impl:$log4j2Version")

    // ↓ LOGBACK ↓ to use it comment out SLF4J SIMPLE and LOG4J 2 dependencies
    implementation("ch.qos.logback:logback-classic:1.3.15")
}
