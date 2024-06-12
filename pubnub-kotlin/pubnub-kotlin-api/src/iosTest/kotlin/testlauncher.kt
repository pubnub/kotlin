package testlauncher

import platform.CoreFoundation.*
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.concurrent.*
import kotlin.native.internal.test.*
import kotlin.system.*

@OptIn(ExperimentalNativeApi::class)
fun mainBackground(args: Array<String>) {
    val worker = Worker.start(name = "main-background")
    worker.execute(TransferMode.SAFE, { args.freeze() }) {
        val result = testLauncherEntryPoint(it)
        exitProcess(result)
    }
    CFRunLoopRun()
    error("CFRunLoopRun should never return")
}