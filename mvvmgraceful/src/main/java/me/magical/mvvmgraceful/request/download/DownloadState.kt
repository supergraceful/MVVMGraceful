package me.magical.mvvmgraceful.request.download

sealed class DownloadState {

    data class Progress(val progress: Int, val read: Long,val count: Long,val done: Boolean):DownloadState()

    data class Success(val path: String,val size: Long):DownloadState()

    data class Error(val errorMsg:String):DownloadState()

    object Prepare:DownloadState()

    object Pause:DownloadState()
}