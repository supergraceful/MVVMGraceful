package me.magical.mvvmgraceful.request.download

import android.os.Looper
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import me.magical.mvvmgraceful.ext.kv.KVUtil
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.io.File
import java.lang.Exception
import java.util.concurrent.TimeUnit
import kotlin.math.log

/**
 *
 * https://github.com/hegaojian/JetpackMvvm
 * 下载管理类
 */
object DownLoadManager {

    private val retrofitBuilder by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.baidu.com/")
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.SECONDS).build()
            ).build()
    }

    /**
     * @param tag 下载任务标记
     * @param url 下载地址
     * @param saveName 文件保存名称
     * @param savePath 文件保存地址
     * @param again 当下载存在时，是否重新下载
     * @param again 下载回调
     */
    suspend fun downLoad(
        tag: String,
        url: String,
        savePath: String,
        saveName: String,
        listener: OnDownLoadListener,
        again: Boolean = false,
    ) {
        downLoad(tag, url, null, savePath, saveName, listener,again)
    }

    /**
     * @param tag 下载任务标记
     * @param url 下载地址
     * @param headers 请求头
     * @param saveName 文件保存名称
     * @param savePath 文件保存地址
     * @param again 当下载存在时，是否重新下载
     * @param again 下载回调
     */
    suspend fun downLoad(
        tag: String,
        url: String,
        headers: HashMap<String, String>? = null,
        savePath: String,
        saveName: String,
        listener: OnDownLoadListener,
        again: Boolean = false,
    ) {
        withContext(Dispatchers.IO) {
            downLoad(tag, url, savePath, saveName,  this, listener,headers,again)
        }
    }

    /**
     * 取消下载
     */
    fun cancel(tag: String) {
        val path = DownLoadPool.getPath(tag)
        if (path != null) {
            val file = File(path)
            if (file.exists()) {
                file.delete()
            }
        }
        DownLoadPool.remove(tag)
    }

    /**
     * 暂停下载
     * @param key String 暂停的标识
     */
    fun pause(key: String) {
        val listener = DownLoadPool.getListener(key)
        listener?.onDownLoadPause(key)
        DownLoadPool.pause(key)
    }


    /**
     * 取消所有下载
     */
    fun doDownLoadCancelAll() {
        DownLoadPool.getListenerMap().forEach {
            cancel(it.key)
        }
    }

    /**
     * 暂停所有下载
     */
    fun doDownLoadPauseAll() {
        DownLoadPool.getListenerMap().forEach {
            pause(it.key)
        }
    }


    /**
     *开始下载
     * @param tag String 标识
     * @param url String  下载的url
     * @param savePath String 保存的路径
     * @param saveName String 保存的名字
     * @param reDownload Boolean 如果文件已存在是否需要重新下载 默认不需要重新下载
     * @param loadListener OnDownLoadListener
     */
    suspend fun downLoad(
        tag: String,
        url: String,
        savePath: String,
        saveName: String,
        patchers: CoroutineScope,
        listener: OnDownLoadListener,
        headers: HashMap<String, String>? = null,
        again: Boolean = false,
    ) {
        val scope = DownLoadPool.getScope(tag)
        if (scope != null && scope.isActive) {
            Log.i("downLoad: ", "下载正在进行")
            return
        } else if (scope != null && !scope.isActive) {
            Log.i("downLoad: ", "下载已存在未开始")
            DownLoadPool.removeScope(tag)
        }

        if (saveName.isEmpty()) {
            withContext(Dispatchers.Main) {
                listener.onDownloadError(tag, Throwable("save name is Empty"))
            }
            return
        }
        if (Looper.getMainLooper().thread == Thread.currentThread()) {
            withContext(Dispatchers.Main) {
                listener.onDownloadError(tag, Throwable("current thread is in main thread"))
            }
            return
        }

        val file = File("$savePath/$saveName")
        val currentLength = if (!file.exists()) {
            0L
        } else {
            KVUtil.getValue(tag, 0L) ?: 0L
        }

        //文件已经存在
        if (file.exists() && currentLength == 0L && !again) {
            listener.onDownloadSuccess(tag, file.path, file.length())
        }
        Log.i("downLoad: ", "download start current $currentLength")

        try {
            DownLoadPool.add(tag, patchers, "$savePath/$saveName", listener)
            withContext(Dispatchers.Main) {
                listener.onDownloadPrepare(tag)
            }


            val responseBody =
                if (headers == null) {
                    retrofitBuilder.create(DownloadApi::class.java).downloadFile(url).body()

                } else {
                    retrofitBuilder.create(DownloadApi::class.java).downloadFile(url, headers)
                        .body()
                }
            if (responseBody == null) {
                Log.i("downLoad: ", "responseBody is null")
                withContext(Dispatchers.Main) {
                    listener.onDownloadError(
                        tag,
                        Throwable("responseBody is null please check download url")
                    )
                }
                DownLoadPool.remove(tag)
                return
            }


            FileTool.downToFile(
                tag,
                savePath,
                saveName,
                currentLength,
                responseBody,
                listener
            )
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                listener.onDownloadError(tag, e)
            }
            DownLoadPool.remove(tag)
        }
    }
}