import com.pubnub.gradle.enableJsTarget

plugins {
    alias(libs.plugins.benmanes.versions)
    id("pubnub.dokka")
    alias(libs.plugins.npm.publish)
    id("pubnub.multiplatform")
    id("pubnub.shared")
}

npmPublish {
    packages {
        getByName("js") {
            scope = "pubnub"
            packageName = "matchmaking"
//            types.set("index.d.ts") // todo not needed if types are generated and defined in package.json
            packageJsonTemplateFile = project.layout.projectDirectory.file("js-matchmaking/package_template.json")
        }
    }
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":pubnub-matchmaking-kotlin:pubnub-matchmaking-kotlin-api"))
                implementation(project(":pubnub-matchmaking-kotlin:pubnub-matchmaking-kotlin-impl"))
            }
        }
    }
    if (enableJsTarget) {
        js {
// keep this in here for ad-hoc testing
//            browser {
//                testTask {
//                    useMocha {
//                        timeout = "15s"
//                    }
//                }
//            }

            compilerOptions {
                target.set("es2015")
            }
            binaries.library()
            generateTypeScriptDefinitions() // generates pubnub-matchmaking-kotlin/build/dist/js/productionLibrary/pubnub-pubnub-matchmaking-kotlin.d.ts
        }
    }
}
