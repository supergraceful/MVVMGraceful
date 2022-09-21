package me.magical.mvvmgraceful.request

import me.magical.mvvmgraceful.request.core.CustomException
import me.magical.mvvmgraceful.request.core.DataState
import me.magical.mvvmgraceful.request.core.ResponseImpl


/**
 * 处理请求回的State,将state转换为回调参数
 */
fun <T> parse(
    state: DataState<T>,
    onStart: (() -> Unit)? = null,
    onSuccess: (T?) -> Unit,
    onError: ((CustomException) -> Unit)? = null,
    onComplete: (() -> Unit)? = null
) {
    when (state) {
        is DataState.OnStart -> {
            onStart?.invoke()
        }
        is DataState.OnSuccess -> {
            onSuccess.invoke(state.data)
        }

        is DataState.OnError -> {
            onError?.invoke(state.exception)
        }

        is DataState.OnComplete -> {
            onComplete.run { this }
        }
    }
}

/**
 * 处理请求回的State,将state转换为接口回调
 */
fun <T> parse(state: DataState<T>,responseImpl: ResponseImpl<T>) {

    when (state) {
        is DataState.OnStart -> {
            responseImpl.onStart()
        }
        is DataState.OnSuccess -> {
            responseImpl.onSuccess(state.data)
        }

        is DataState.OnError -> {
            responseImpl.onError(state.exception)
        }
        is DataState.OnComplete -> {
            responseImpl.onComplete()
        }
    }
}

