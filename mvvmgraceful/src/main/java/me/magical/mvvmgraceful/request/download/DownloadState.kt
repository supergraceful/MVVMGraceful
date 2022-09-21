package me.magical.mvvmgraceful.request.download

import android.util.Log
import me.magical.mvvmgraceful.livedata.UnFlowLiveData

sealed class DownloadState {

    data class Progress(val progress: Int, val read: Long,val count: Long,val done: Boolean):DownloadState()

    data class Success(val path: String,val size: Long):DownloadState()

    data class Error(val throwable:Throwable):DownloadState()

    object Prepare:DownloadState()

    object Pause:DownloadState()
}

/**
 * 回调转换为状态函数
 */
fun getStateListener(result: UnFlowLiveData<DownloadState>):OnDownLoadListener{
    return object : OnDownLoadListener {
        override fun onDownloadSuccess(tag: String, path: String, size: Long) {
            result.postValue(DownloadState.Success(path,size))
        }

        override fun onDownloadError(tag: String, throwable: Throwable) {

            result.postValue(DownloadState.Error(throwable))
        }

        override fun onDownloadPrepare(tag: String) {
           result.postValue(DownloadState.Prepare)
        }

        override fun onDownLoadPause(tag: String) {
            result.postValue(DownloadState.Pause)
        }

        override fun onDownloadProgress(
            tag: String,
            progress: Int,
            read: Long,
            count: Long,
            done: Boolean
        ) {
            result.postValue(DownloadState.Progress(progress, read, count, done))
        }
    }
}