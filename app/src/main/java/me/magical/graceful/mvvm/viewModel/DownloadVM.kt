package me.magical.graceful.mvvm.viewModel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.magical.mvvmgraceful.base.BaseViewModel
import me.magical.mvvmgraceful.livedata.UnFlowLiveData
import me.magical.mvvmgraceful.request.download.*
import me.magical.mvvmgraceful.utils.UtilsBridge


class DownloadVM : BaseViewModel() {

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
                UtilsBridge.getApplication().getExternalFilesDir(null).toString(),
                "test.zip",
                getStateListener(listener)
            )
        }


//        viewModelScope.launch(Dispatchers.IO) {
//            DownLoadManager.downLoad(
//                tag,
//                "https://dl.google.com/android/repository/sdk-tools-windows-4333796.zip",
//                FileTool.getBasePath(),
//                "test.zip",
//                object :OnDownLoadListener{
//                    override fun onDownloadSuccess(tag: String, path: String, size: Long) {
//                       //下载成功回调
//                    }
//
//                    override fun onDownloadError(tag: String, throwable: Throwable) {
//                        //下载异常回调
//                    }
//
//                    override fun onDownloadPrepare(tag: String) {
//                        //等待下载回调
//                    }
//
//                    override fun onDownLoadPause(tag: String) {
//                        //下载暂停回调
//                    }
//
//                    override fun onDownloadProgress(
//                        tag: String,
//                        progress: Int,
//                        read: Long,
//                        count: Long,
//                        done: Boolean
//                    ) {
//                        //下载进度回调
//                    }
//                }
//            )
//        }


    }

    fun cancel() {
        DownLoadManager.cancel(tag)
    }

    fun pause() {
        DownLoadManager.pause(tag)
    }
}