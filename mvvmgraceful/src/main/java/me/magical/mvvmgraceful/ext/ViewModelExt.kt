package me.magical.mvvmgraceful.ext
import androidx.lifecycle.viewModelScope
import com.google.gson.annotations.Until
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.magical.mvvmgraceful.base.BaseViewModel
import me.magical.mvvmgraceful.livedata.UnFlowLiveData
import me.magical.mvvmgraceful.request.core.BaseResponse
import me.magical.mvvmgraceful.request.core.CustomException
import me.magical.mvvmgraceful.request.core.DataState
import me.magical.mvvmgraceful.request.download.DownLoadManager
import me.magical.mvvmgraceful.request.download.FileTool


/**
 * 回调式网络请求，过滤信息，添加成功失败的loading和toast的触发
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
fun <T : Any> BaseViewModel.uiRequest(
    block: suspend () -> BaseResponse<T>?,
    onSuccess: ((T?) -> Unit)? = null,
    onError: ((CustomException) -> Unit)? = null,
    onComplete: (() -> Unit)? = null,
    isLoading: Boolean = true,
    isToast: Boolean = true,
    dialog: String = "加载中...",
) {
    viewModelScope.launch(Dispatchers.IO) {
        try {
            if (isLoading) {
                showLoading(dialog)
            }
            val blockResult = block()!!

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
        } catch (e: Exception) {
            //当请求异常时，将抛出的异常进行转换，转换为自定义的Exception，并用onError将异常信息返回
            e.printStackTrace()
            val handleException = CustomException.handleException(e)
            if (isToast) {
                showToast(handleException.msg)
            }
            onError?.let { it(handleException) }
        } finally {
            //请求结束时调用返回
            if (isLoading) {
                dismissLoading()
            }
            onComplete?.let { it() }
        }
    }
}

/**
 * 回调式网络请求，过滤信息
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
fun <T : Any> BaseViewModel.request(
    block: suspend () -> BaseResponse<T>?,
    onStart: (() -> Unit)?=null,
    onSuccess: ((T?) -> Unit)? = null,
    onError: ((CustomException) -> Unit)? = null,
    onComplete: (() -> Unit)? = null,
) {
    viewModelScope.launch(Dispatchers.IO) {
        try {
            onStart?.invoke()
            val blockResult = block()!!

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
        } catch (e: Exception) {
            //当请求异常时，将抛出的异常进行转换，转换为自定义的Exception，并用onError将异常信息返回
            e.printStackTrace()
            val handleException = CustomException.handleException(e)

            onError?.let { it(handleException) }
        } finally {
            //请求结束时调用返回
            onComplete?.let { it() }
        }
    }
}


///**
// * 处理请求回的State,将state转换为回调方式
// */
//fun <T> BaseViewModel.uiParse(
//    state: DataState<T>,
//    onStart: ((message: String) -> Unit)? = null,
//    onSuccess: ((T?) -> Unit),
//    onError: ((CustomException) -> Unit)? = null,
//    onComplete: (() -> Unit)? = null
//) {
//    when (state) {
//        is DataState.OnStart -> {
//            if (onStart == null) {
//                showLoading(state.message)
//            } else {
////                onStart.run { this }
//                onStart.invoke(state.message)
//            }
//        }
//        is DataState.OnSuccess -> {
//            onSuccess.invoke(state.data)
//        }
//        is DataState.OnError -> {
//            onError?.invoke(state.exception)
//        }
//        is DataState.OnComplete -> {
//            onComplete.run { this }
//            dismissLoading()
//        }
//    }
//}

/**
 * 处理请求回的State,将state转换为回调方式
 */
fun <T> parse(
    state: DataState<T>,
    onStart: ((message: String) -> Unit)? = null,
    onSuccess: ((T?) -> Unit),
    onError: ((CustomException) -> Unit)? = null,
    onComplete: (() -> Unit)? = null
) {
    when (state) {
        is DataState.OnStart -> {
            onStart?.invoke(state.message)
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
 * 发起请求，过滤信息，并将结果加入到State当中，添加成功失败的loading和toast的触发
 */
fun <T> BaseViewModel.uiRequest(
    resultState: UnFlowLiveData<DataState<T>>,
    block: suspend () -> BaseResponse<T>?,
    isLoading: Boolean = true,
    isToast: Boolean = true,
    loadingText: String = "加载中...",
): Job {
    return viewModelScope.launch(Dispatchers.IO) {
        runCatching {
            resultState.postValue(DataState.OnStart(loadingText))
            if (isLoading) {
                showLoading(loadingText)
            }
            block()
        }.onSuccess {
            it!!
            if (it.isSuccess()){
                resultState.postValue(DataState.OnSuccess(it.getResponseData()))
            }else{
                if(isToast){
                    showToast(it.getThrowableMessage())
                }
                resultState.postValue(DataState.OnError( CustomException(it.getResponseCode(), it.getThrowableMessage() ?: "未知异常")))
            }
            if (isLoading) {
                dismissLoading()
            }
            resultState.postValue(DataState.OnComplete)
        }.onFailure {
            it.stackTrace
            GLog.e( "request: ", it.message.toString())
            if(isToast){
                showToast(it.message)
            }
            if (isLoading) {
                dismissLoading()
            }
            resultState.postValue(DataState.OnError( CustomException.handleException(it)))
            resultState.postValue(DataState.OnComplete)
        }
    }
}

/**
 * 发起请求，并将结果加入到State当中，
 */
fun <T> BaseViewModel.request(
    resultState: UnFlowLiveData<DataState<T>>,
    block: suspend () -> BaseResponse<T>?,
): Job {
    return viewModelScope.launch(Dispatchers.IO) {
        runCatching {
            resultState.postValue(DataState.OnStart(""))
            block()
        }.onSuccess {
            it!!
            if (it.isSuccess()){
                resultState.postValue(DataState.OnSuccess(it.getResponseData()))
            }else{

                resultState.postValue(DataState.OnError( CustomException(it.getResponseCode(), it.getThrowableMessage() ?: "未知异常")))
            }
            resultState.postValue(DataState.OnComplete)
        }.onFailure {
            it.stackTrace
            GLog.e( "request: ", it.message.toString())
            resultState.postValue(DataState.OnError( CustomException.handleException(it)))
            resultState.postValue(DataState.OnComplete)
        }
    }
}