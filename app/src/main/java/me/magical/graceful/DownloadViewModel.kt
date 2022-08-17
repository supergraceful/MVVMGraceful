package me.magical.graceful

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.magical.mvvmgraceful.base.BaseViewModel
import me.magical.mvvmgraceful.livedata.UnFlowLiveData
import me.magical.mvvmgraceful.request.download.DownLoadManager
import me.magical.mvvmgraceful.request.download.DownloadState
import me.magical.mvvmgraceful.request.download.FileTool
import me.magical.mvvmgraceful.request.download.setStateListener


class DownloadViewModel : BaseViewModel() {

    val listener = UnFlowLiveData<DownloadState>()
    val tag = "111111111"

    fun download() {
        viewModelScope.launch(Dispatchers.IO) {
            DownLoadManager.downLoad(
                tag,
                "https://dl.google.com/android/repository/sdk-tools-windows-4333796.zip",
                FileTool.getBasePath(),
                "test.zip",
                setStateListener(listener)
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