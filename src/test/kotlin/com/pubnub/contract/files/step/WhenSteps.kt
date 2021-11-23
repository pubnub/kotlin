package com.pubnub.contract.files.step

import com.pubnub.api.PubNubException
import com.pubnub.contract.state.World
import io.cucumber.java.en.When

private const val channel = "channel"

class WhenSteps(private val world: World) {
    @When("I list files")
    fun i_list_files() {
        try {
            world.pubnub
                .listFiles(channel = channel)
                .sync()
        } catch (ex: PubNubException) {
            world.pnException = ex
        }
    }

    @When("I publish file message")
    fun i_publish_file_message() {
        try {
            world.pubnub
                .publishFileMessage(
                    channel = channel,
                    fileId = "fileId",
                    fileName = "fileName",
                    message = "hello"
                )
                .sync()
        } catch (ex: PubNubException) {
            world.pnException = ex
        }
    }

    @When("I delete file")
    fun i_delete_file() {
        try {
            world.pubnub
                .deleteFile(
                    channel = channel,
                    fileId = "fileId",
                    fileName = "fileName"
                )
                .sync()
        } catch (ex: PubNubException) {
            world.pnException = ex
        }
    }

    @When("I download file")
    fun i_download_file() {
        try {
            world.pubnub
                .downloadFile(
                    channel = channel,
                    fileName = "fileName",
                    fileId = "fileId"
                )
                .sync()
        } catch (ex: PubNubException) {
            world.pnException = ex
        }
    }
}
