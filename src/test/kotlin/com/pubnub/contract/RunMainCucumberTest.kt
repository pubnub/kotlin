package com.pubnub.contract

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
    features = ["src/test/resources/sdk-specifications/features"],
    tags = "not @skip and not @na=kotlin and not @beta",
    plugin = ["pretty", "summary", "junit:build/reports/cucumber-reports/main.xml"]
)
class RunMainCucumberTest {
    companion object {
        fun getCucumberOptions(): CucumberOptions? {
            val clazz = RunMainCucumberTest::class.java
            val optionsAnnotation = clazz.getAnnotation(CucumberOptions::class.java)
            return optionsAnnotation
        }
    }
}

@RunWith(Cucumber::class)
@CucumberOptions(
    features = ["src/test/resources/sdk-specifications/features"],
    tags = "not @skip and not @na=kotlin and @beta",
    plugin = ["pretty", "summary", "junit:build/reports/cucumber-reports/beta.xml"]
)
class RunBetaCucumberTest {
    companion object {
        fun getCucumberOptions(): CucumberOptions? {
            val clazz = RunBetaCucumberTest::class.java
            val optionsAnnotation = clazz.getAnnotation(CucumberOptions::class.java)
            return optionsAnnotation
        }
    }
}
