package me.magical.graceful

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.magical.mvvmgraceful.base.BaseViewModel
import me.magical.mvvmgraceful.livedata.UnFlowLiveData
import me.magical.mvvmgraceful.request.download.*


class DownloadViewModel : BaseViewModel() {

    val listener = UnFlowLiveData<DownloadState>()
    val tag = "111111111"

    fun download() {
        /**
         * viewModelScope是一个绑定到你的ViewModel的CoroutineScope。
         * 这意味着当ViewModel清除了该作用域中的协程时，该作用域中的协程也会被取消。
         *
         * 参数一：下载的标记tag,String类型，非空
         * 参数二：下载地址，String类型，非空
         * 参数三：请求头，Map类型，可为空
         * 参数四：保存地址结尾不能是 / ，String类型，非空
         * 参数五：保存本地的文件名，非空
         * 参数六：回调接口，非空
         * 参数七：是否覆盖下载，可为空
         */
        viewModelScope.launch(Dispatchers.IO) {
            DownLoadManager.downLoad(
                tag,
                "https://dl.google.com/android/repository/sdk-tools-windows-4333796.zip",
                FileTool.getBasePath(),
                "test.zip",
                getStateListener(listener)
            )
        }


    }

    fun cancel() {
        DownLoadManager.cancel(tag)
    }

    fun pause() {
        DownLoadManager.pause(tag)
    }
}