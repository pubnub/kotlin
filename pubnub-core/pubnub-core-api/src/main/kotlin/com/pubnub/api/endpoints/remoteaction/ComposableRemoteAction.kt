package com.pubnub.api.endpoints.remoteaction

import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.v2.callbacks.Result
import java.util.function.Consumer

class ComposableRemoteAction<T, U>(
    private val remoteAction: ExtendedRemoteAction<T>,
    private val createNextRemoteAction: (T) -> ExtendedRemoteAction<U>,
    private var checkpoint: Boolean,
) : ExtendedRemoteAction<U> {
    private var nextRemoteAction: ExtendedRemoteAction<U>? = null
    private var isCancelled = false

    fun <Y> then(factory: (U) -> ExtendedRemoteAction<Y>): ComposableRemoteAction<U, Y> {
        return ComposableRemoteAction(this, factory, false)
    }

    @Synchronized
    fun checkpoint(): ComposableRemoteAction<T, U> {
        checkpoint = true
        return this
    }

    @Throws(PubNubException::class)
    override fun sync(): U {
        return remoteAction.sync().let { result ->
            createNextRemoteAction(result).sync()
        }
    }

    override fun async(callback: Consumer<Result<U>>) {
        remoteAction.async { r: Result<T> ->
            r.onFailure {
                callback.accept(Result.failure(it.copy(remoteAction = this)))
            }.onSuccess {
                try {
                    synchronized(this) {
                        if (!isCancelled) {
                            val newNextRemoteAction =
                                createNextRemoteAction(it) // if s is not error r shouldn't be null
                            nextRemoteAction = newNextRemoteAction
                            newNextRemoteAction.async { r2: Result<U> ->
                                r2.onFailure {
                                    callback.accept(Result.failure(it.copy(remoteAction = this)))
                                }.onSuccess {
                                    callback.accept(Result.success(it))
                                }
                            }
                        }
                    }
                } catch (ex: PubNubException) {
                    callback.accept(Result.failure(ex.copy(remoteAction = this)))
                }
            }
        }
    }

    @Synchronized
    override fun retry() {
        if (checkpoint && nextRemoteAction != null) {
            nextRemoteAction!!.retry()
        } else {
            remoteAction.retry()
        }
    }

    @Synchronized
    override fun silentCancel() {
        isCancelled = true
        remoteAction.silentCancel()
        if (nextRemoteAction != null) {
            nextRemoteAction!!.silentCancel()
        }
    }

    override fun operationType(): PNOperationType {
        return nextRemoteAction?.operationType() ?: remoteAction.operationType()
    }

    class ComposableBuilder<T>(private val remoteAction: ExtendedRemoteAction<T>) {
        private var checkpoint = false

        fun <U> then(factory: (T) -> ExtendedRemoteAction<U>): ComposableRemoteAction<T, U> {
            return ComposableRemoteAction(remoteAction, factory, checkpoint)
        }

        fun checkpoint(): ComposableBuilder<T> {
            checkpoint = true
            return this
        }
    }

    companion object {
        fun <T> firstDo(remoteAction: ExtendedRemoteAction<T>): ComposableBuilder<T> {
            return ComposableBuilder(remoteAction)
        }
    }
}
