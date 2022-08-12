package me.magical.mvvmgraceful.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.magical.mvvmgraceful.livedata.UnFlowLiveData
import me.magical.mvvmgraceful.request.core.BaseResponse
import me.magical.mvvmgraceful.request.core.CustomException

abstract class BaseViewModel : ViewModel() {

    companion object {
        const val CLASS = "CLASS"
        const val BUNDLE = "BUNDLE"
    }

    val mUIData = UIChangeLiveData()

    protected fun showToastEvent(content: String?) {

        mUIData.showToastEvent.postValue(content)
    }

    protected fun showDialog(title: String?) {
        mUIData.showDialogEvent.postValue(title)
    }

    protected fun dismissDialog() {
        mUIData.dismissDialogEvent.call()
    }

    protected fun startActivity(clz: Class<*>, bundle: Bundle?) {
        val params = mutableMapOf<String, Any>()
        params[CLASS] = clz
        bundle?.also {
            params[BUNDLE] = bundle
        }
        mUIData.startActivityEvent.postValue(params)

    }

    protected fun finish() {
        mUIData.finishEvent.call()
    }

    protected fun onBackPressed() {
        mUIData.onBackPressedEvent.call()
    }

    /**
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
    protected fun <T : Any> launch(
        block: suspend () -> BaseResponse<T>?,
        onSuccess: ((T?) -> Unit)? = null,
        onError: ((CustomException) -> Unit)? = null,
        onComplete: (() -> Unit)? = null,
        showDialog: Boolean = true,
        showToast: Boolean = true,
        dialog: String = "加载中...",
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (showDialog) {
                    showDialog(dialog)
                }
                val blockResult = block()!!

                //请求完成后，判断是否成功获取数据，如果没有成功获取数据，用onError将错误信息返回
                if ( blockResult.isSuccess()) {
                    onSuccess?.invoke(blockResult.getData())
                } else {
                    if (showToast) {
                        showToastEvent(blockResult.getMessage() ?: "未知异常")
                    }
                    onError?.let {
                        it(
                            CustomException(
                                blockResult.getCode(),
                                blockResult.getMessage() ?: "未知异常"
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                //当请求异常时，将抛出的异常进行转换，转换为自定义的Exception，并用onError将异常信息返回
                e.printStackTrace()
                val handleException = CustomException.handleException(e)
                if (showToast) {
                    showToastEvent(handleException.msg)
                }
                onError?.let { it(handleException) }
            } finally {
                //请求结束时调用返回
                if (showDialog) {
                    dismissDialog()
                }
                onComplete?.let { it() }
            }
        }
    }


    inner class UIChangeLiveData {

        val showDialogEvent by lazy {
            UnFlowLiveData<String>()
        }
        val showToastEvent by lazy {
            UnFlowLiveData<String>()
        }
        val dismissDialogEvent by lazy {
            UnFlowLiveData<Void>()
        }
        val startActivityEvent by lazy {
            UnFlowLiveData<Map<String, Any>>()
        }
//        val startContainerActivityEvent by lazy {
//            UnFlowLiveData<Map<String, Any>>()
//        }
        val finishEvent by lazy {
            UnFlowLiveData<Void>()
        }
        val onBackPressedEvent by lazy {
            UnFlowLiveData<Void>()
        }

    }
}