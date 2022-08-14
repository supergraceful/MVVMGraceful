package me.magical.mvvmgraceful.request.download

interface OnDownLoadListener {

    //下载成功
    fun onDownloadSuccess(tag: String, path: String, size: Long)

    //下载错误
    fun onDownloadError(tag: String, throwable: Throwable)

    //等待下载
    fun onDownloadPrepare(tag: String)

    //下载暂停
    fun onDownLoadPause(tag: String)

    /**
     * 下载进度
     * @param key url
     * @param progress  进度
     * @param read  读取（实际的文件位置）
     * @param count 总共长度
     * @param done  是否完成
     */
    fun onDownloadProgress( key: String,progress: Int, read: Long,count: Long,done: Boolean)
}