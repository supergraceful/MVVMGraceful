package me.magical.mvvmgraceful.request

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.magical.mvvmgraceful.base.BaseViewModel
import me.magical.mvvmgraceful.request.core.BasBean
import me.magical.mvvmgraceful.request.core.CustomException
import me.magical.mvvmgraceful.request.core.DataState
import me.magical.mvvmgraceful.request.core.ResponseImpl


/**
 * 参数回调式网络请求，过滤信息，添加成功失败的loading和toast的触发
 *
 * viewModelScope会随是有生命周期感知的所在viewModel销毁时同时会清理掉协程内所有任务
 *
 * launch({},{},{},showToast = true,dialog = "加载中...")
 *
 * @param block 请求方法体 请求方法必须是suspend方法
 * @param onSuccess 成功时回调 方法体内部返回it为成功的数据  （默认为null）
 * @param onError 失败时回调 方法体内部返回it为失败信息 （默认为null）
 * @param onComplete 完成时回调  （默认为null）
 * @param showDialog 加载时是否弹出Dialog （默认弹出）
 * @param showToast 失败时是否弹出Toast提示框 （默认弹出）
 * @param dialog 加载loading图标标题
 */
fun <T> BaseViewModel.uiRequest(
    block: suspend () -> BasBean<T>?,
    onSuccess: ((T?) -> Unit)? = null,
    onError: ((CustomException) -> Unit)? = null,
    onComplete: (() -> Unit)? = null,
    isLoading: Boolean = true,
    isToast: Boolean = true,
    dialog: String = "加载中...",
): Job {
    return viewModelScope.launch(Dispatchers.IO) {
        try {
            if (isLoading) {
                showLoading(dialog)
            }
            val blockResult = block()!!

            withContext(Dispatchers.Main) {
                //请求完成后，判断是否成功获取数据，如果没有成功获取数据，用onError将错误信息返回
                if (blockResult.isSuccess()) {
                    onSuccess?.invoke(blockResult.getResponseData())
                } else {
                    if (isToast) {
                        showToast(blockResult.getThrowableMessage() ?: "未知异常")
                    }
                    onError?.let {
                        it(
                            CustomException(
                                blockResult.getResponseCode(),
                                blockResult.getThrowableMessage() ?: "未知异常"
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            //当请求异常时，将抛出的异常进行转换，转换为自定义的Exception，并用onError将异常信息返回
            e.printStackTrace()
            val handleException = CustomException.handleException(e)
            if (isToast) {
                showToast(handleException.msg)
            }
            withContext(Dispatchers.Main) {
                onError?.let { it(handleException) }
            }
        } finally {
            //请求结束时调用返回
            if (isLoading) {
                dismissLoading()
            }
            withContext(Dispatchers.Main) {
                onComplete?.let { it() }
            }

        }
    }
}

/**
 * 参数回调式网络请求，过滤信息
 *
 * viewModelScope会随是有生命周期感知的所在viewModel销毁时同时会清理掉协程内所有任务
 *
 * launch({},{},{},{})
 *
 * @param block 请求方法体 请求方法必须是suspend方法
 * @param onStart 开始时回调
 * @param onSuccess 成功时回调 方法体内部返回it为成功的数据  （默认为null）
 * @param onError 失败时回调 方法体内部返回it为失败信息 （默认为null）
 * @param onComplete 完成时回调  （默认为null）
 */
fun <T> BaseViewModel.request(
    block: suspend () -> BasBean<T>?,
    onStart: (() -> Unit)?=null,
    onSuccess: ((T?) -> Unit)? = null,
    onError: ((CustomException) -> Unit)? = null,
    onComplete: (() -> Unit)? = null,
) : Job{
    return viewModelScope.launch(Dispatchers.IO) {
        try {
            onStart?.invoke()
            val blockResult = block()!!

            withContext(Dispatchers.Main) {
                //请求完成后，判断是否成功获取数据，如果没有成功获取数据，用onError将错误信息返回
                if (blockResult.isSuccess()) {
                    onSuccess?.invoke(blockResult.getResponseData())
                } else {
                    onError?.let {
                        it(
                            CustomException(
                                blockResult.getResponseCode(),
                                blockResult.getThrowableMessage() ?: "未知异常"
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            //当请求异常时，将抛出的异常进行转换，转换为自定义的Exception，并用onError将异常信息返回
            e.printStackTrace()
            val handleException = CustomException.handleException(e)
            withContext(Dispatchers.Main) {
                onError?.let { it(handleException) }
            }
        } finally {
            withContext(Dispatchers.Main) {
                //请求结束时调用返回
                onComplete?.let { it() }
            }
        }
    }
}


/**
 * 状态方法式发起请求，过滤信息，并将结果加入到State当中，添加成功失败的loading和toast的触发
 */
fun <T> BaseViewModel.uiRequest(
    resultState: MutableLiveData<DataState<T>>,
    block: suspend () -> BasBean<T>?,
    isLoading: Boolean = true,
    isToast: Boolean = true,
    loadingText: String = "加载中...",
): Job {
    return viewModelScope.launch{
        runCatching {
            resultState.postValue(DataState.OnStart(loadingText))
            if (isLoading) {
                showLoading(loadingText)
            }
            block()
        }.onSuccess {
            it!!
            if (it.isSuccess()){
                resultState.value=DataState.OnSuccess(it.getResponseData())
            }else{
                if(isToast){
                    showToast(it.getThrowableMessage())
                }
                resultState.value=DataState.OnError( CustomException(it.getResponseCode(), it.getThrowableMessage() ?: "未知异常"))
            }
            if (isLoading) {
                dismissLoading()
            }

        }.onFailure {
            it.stackTrace
            if(isToast){
                showToast(it.message)
            }
            if (isLoading) {
                dismissLoading()
            }
            resultState.value=DataState.OnError( CustomException.handleException(it))

        }
        resultState.value=DataState.OnComplete
    }
}

/**
 * 状态方法式发发起请求，并将结果加入到State当中，
 */
fun <T> BaseViewModel.request(
    resultState: MutableLiveData<DataState<T>>,
    block: suspend () -> BasBean<T>?,
): Job {
    return uiRequest(resultState,block,false,false)
//    return viewModelScope.launch {
//        runCatching {
//            resultState.value=DataState.OnStart("")
//            block()
//        }.onSuccess {
//            it!!
//            if (it.isSuccess()){
//                resultState.value=DataState.OnSuccess(it.getResponseData())
//            }else{
//
//                resultState.value=DataState.OnError( CustomException(it.getResponseCode(), it.getThrowableMessage() ?: "未知异常"))
//            }
//        }.onFailure {
//            it.stackTrace
//            resultState.value=DataState.OnError( CustomException.handleException(it))
//        }
//        resultState.value=DataState.OnComplete

//    }
}


/**
 * 参数回调式网络请求，过滤信息，添加成功失败的loading和toast的触发
 *
 * viewModelScope会随是有生命周期感知的所在viewModel销毁时同时会清理掉协程内所有任务
 *
 * launch({},{},{},showToast = true,dialog = "加载中...")
 *
 * @param block 请求方法体 请求方法必须是suspend方法
 * @param onSuccess 成功时回调 方法体内部返回it为成功的数据  （默认为null）
 * @param onError 失败时回调 方法体内部返回it为失败信息 （默认为null）
 * @param onComplete 完成时回调  （默认为null）
 * @param showDialog 加载时是否弹出Dialog （默认弹出）
 * @param showToast 失败时是否弹出Toast提示框 （默认弹出）
 * @param dialog 加载loading图标标题
 */
fun <T> BaseViewModel.uiRequest(
    block: suspend () -> BasBean<T>?,
    responseImpl: ResponseImpl<T>,
    isLoading: Boolean = true,
    isToast: Boolean = true,
    dialog: String = "加载中...",
): Job {
    return viewModelScope.launch(Dispatchers.IO) {
        try {
            responseImpl.onStart()
            if (isLoading) {
                showLoading(dialog)
            }
            val blockResult = block()!!

            withContext(Dispatchers.Main) {
                //请求完成后，判断是否成功获取数据，如果没有成功获取数据，用onError将错误信息返回
                if (blockResult.isSuccess()) {
                    responseImpl.onSuccess(blockResult.getResponseData())

                } else {
                    if (isToast) {
                        showToast(blockResult.getThrowableMessage() ?: "未知异常")
                    }
                    responseImpl.onError(CustomException(
                        blockResult.getResponseCode(),
                        blockResult.getThrowableMessage() ?: "未知异常"
                    ))
                }
            }
        } catch (e: Exception) {
            //当请求异常时，将抛出的异常进行转换，转换为自定义的Exception，并用onError将异常信息返回
            e.printStackTrace()
            val handleException = CustomException.handleException(e)
            if (isToast) {
                showToast(handleException.msg)
            }
            withContext(Dispatchers.Main) {
                responseImpl.onError(handleException)
            }
        } finally {
            //请求结束时调用返回
            if (isLoading) {
                dismissLoading()
            }
            withContext(Dispatchers.Main) {
                responseImpl.onComplete()
            }

        }
    }
}

/**
 * 参数回调式网络请求，过滤信息
 *
 * viewModelScope会随是有生命周期感知的所在viewModel销毁时同时会清理掉协程内所有任务
 *
 * launch({},{},{},{})
 *
 * @param block 请求方法体 请求方法必须是suspend方法
 * @param onStart 开始时回调
 * @param onSuccess 成功时回调 方法体内部返回it为成功的数据  （默认为null）
 * @param onError 失败时回调 方法体内部返回it为失败信息 （默认为null）
 * @param onComplete 完成时回调  （默认为null）
 */
fun <T> BaseViewModel.request(
    block: suspend () -> BasBean<T>?,
    responseImpl: ResponseImpl<T>
) : Job{
    return viewModelScope.launch(Dispatchers.IO) {
        try {
            responseImpl.onStart()
            val blockResult = block()!!

            withContext(Dispatchers.Main) {
                //请求完成后，判断是否成功获取数据，如果没有成功获取数据，用onError将错误信息返回
                if (blockResult.isSuccess()) {
                    responseImpl.onSuccess(blockResult.getResponseData())
                } else {
                    responseImpl.onError(CustomException(
                        blockResult.getResponseCode(),
                        blockResult.getThrowableMessage() ?: "未知异常"
                    ))
                }
            }
        } catch (e: Exception) {
            //当请求异常时，将抛出的异常进行转换，转换为自定义的Exception，并用onError将异常信息返回
            e.printStackTrace()
            val handleException = CustomException.handleException(e)
            withContext(Dispatchers.Main) {
                responseImpl.onError(handleException)
            }
        } finally {
            withContext(Dispatchers.Main) {
                //请求结束时调用返回
                responseImpl.onComplete()
            }
        }
    }
}
