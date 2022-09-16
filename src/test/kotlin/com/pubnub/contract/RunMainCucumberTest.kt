package com.pubnub.contract

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
    features = ["src/test/resources"],
    tags = "not @skip and not @na=kotlin and not @beta and @featureSet=objectsv2",
    plugin = ["pretty", "summary", "junit:build/reports/cucumber-reports/main.xml"]
)
class RunMainCucumberTest

@RunWith(Cucumber::class)
@CucumberOptions(
    features = ["src/test/resources"],
    tags = "not @skip and not @na=kotlin and @beta and @featureSet=objectsv2",
    plugin = ["pretty", "summary", "junit:build/reports/cucumber-reports/beta.xml"]
)
class RunBetaCucumberTest
