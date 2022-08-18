package me.magical.mvvmgraceful.request.download

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.magical.mvvmgraceful.utils.UtilsBridge
import me.magical.mvvmgraceful.ext.kv.KVUtil
import okhttp3.ResponseBody
import java.io.File
import java.io.RandomAccessFile
import java.nio.channels.FileChannel
import java.text.DecimalFormat

/**
 * 文件下载的具体过程
 */
object FileTool {

    //定义GB的计算常量
    private const val GB = 1024 * 1024 * 1024

    //定义MB的计算常量
    private const val MB = 1024 * 1024

    //定义KB的计算常量
    private const val KB = 1024

    @JvmStatic
    suspend fun downToFile(
        tag: String,
        savePath: String,
        saveName: String,
        currentLength: Long,
        responseBody: ResponseBody,
        listener: OnDownLoadListener,
    ) {
        val filePath = getFilePath(savePath, saveName)
        try {
            if (filePath == null) {
                withContext(Dispatchers.Main) {
                    listener.onDownloadError(tag, Throwable("mkdirs file [$savePath]  error"))
                }
                DownLoadPool.remove(tag)
                return
            }
            //保存到文件
            saveToFile(currentLength, responseBody, filePath, tag, listener)

        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                listener.onDownloadError(tag, e)
            }
            DownLoadPool.remove(tag)
        }

    }

    /**
     * 采用内存映射的方式进行文件的写操作
     */
    suspend fun saveToFile(
        currentLength: Long,
        responseBody: ResponseBody,
        filePath: String,
        tag: String,
        listener: OnDownLoadListener
    ) {
        val fileLength = getFileLength(currentLength, responseBody)
        val inputStream = responseBody.byteStream()

        val accessFile = RandomAccessFile(File(filePath), "rwd")
        val channel = accessFile.channel

        //映射到内存中进行文件的写
        val mappedBuffer = channel.map(
            FileChannel.MapMode.READ_WRITE,
            currentLength,
            fileLength - currentLength
        )

        val buffer = ByteArray(4 * 1024)
        var len = 0
        var lastProgress = 0
        var currentSaveLength = currentLength //当前的长度
        while (inputStream.read(buffer).also { len = it } != -1) {

            mappedBuffer.put(buffer, 0, len)
            currentSaveLength += len

            //计算下载的百分比
            val progress = (currentSaveLength.toFloat() / fileLength * 100).toInt()
            if (lastProgress != progress) {
                lastProgress = progress
                KVUtil.put(tag, currentSaveLength)
                withContext(Dispatchers.Main) {
                    listener.onDownloadProgress(
                        tag,
                        progress,
                        currentSaveLength,
                        fileLength,
                        currentSaveLength == fileLength
                    )
                }

                if (currentSaveLength == fileLength) {
                    withContext(Dispatchers.Main) {
                        listener.onDownloadSuccess(tag, filePath, fileLength)
                    }
                    DownLoadPool.remove(tag)
                }
            }
        }
        inputStream.close()
        accessFile.close()
        channel.close()
    }

    /**
     * 获取下载地址
     * @param savePath String
     * @param saveName String
     * @return String?
     */
    @JvmStatic
    fun getFilePath(savePath: String, saveName: String): String? {
        if (!createFile(savePath)) {
            return null
        }
        return "$savePath/$saveName"

    }

    /**
     * 创建文件夹
     * @param downLoadPath String
     * @return Boolean
     */
    @JvmStatic
    fun createFile(downLoadPath: String): Boolean {
        val file = File(downLoadPath)
        if (!file.exists()) {
            return file.mkdirs()
        }
        return true
    }

    /**
     * 数据总长度
     * @param currentLength Long
     * @param responseBody ResponseBody
     * @return Long
     */
    @JvmStatic
    fun getFileLength(
        currentLength: Long,
        responseBody: ResponseBody
    ) =
        if (currentLength == 0L) {

            responseBody.contentLength()
        } else {
            currentLength + responseBody.contentLength()
        }


    /**
     * 格式化小数
     * @param bytes Long
     * @return String
     */
    @JvmStatic
    fun bytes2kb(bytes: Long): String {
        val format = DecimalFormat("###.0")
        return when {
            bytes / GB >= 1 -> {
                format.format(bytes / GB) + "GB";
            }
            bytes / MB >= 1 -> {
                format.format(bytes / MB) + "MB";
            }
            bytes / KB >= 1 -> {
                format.format(bytes / KB) + "KB";
            }
            else -> {
                "${bytes}B";
            }
        }
    }

    /**
     * 获取App文件的根路径
     * @return String
     */
    @JvmStatic
    fun getBasePath(): String {
        var p: String? = UtilsBridge.getApplication().getExternalFilesDir(null)?.path
        val f: File? = UtilsBridge.getApplication().getExternalFilesDir(null)
        if (null != f) {
            p = f.absolutePath
        }
        return p ?: ""
    }
}