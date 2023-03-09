package me.magical.mvvmgraceful.helper

import android.content.Context
import android.os.Environment
import me.magical.mvvmgraceful.utils.DeviceUtils
import me.magical.mvvmgraceful.utils.FileUtil
import me.magical.mvvmgraceful.utils.TimeUtils
import me.magical.mvvmgraceful.utils.UtilsBridge
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter

/**
 * 统一处理未捕获异常辅助类
 */
class CrashHelper private constructor() : Thread.UncaughtExceptionHandler {


    private val mContext = UtilsBridge.getApplication()
    //存储异常信息时的tag
    private var mCrashTag:Map<String,String>?=null
    //异常文件的存储路径
    private var mSavePath:String?=null
    private var mListener:UncaughtExceptionListener?=null
    //是否开启异常文件存储
    private var saveFile=true

    companion object {
        val instance: CrashHelper by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CrashHelper()
        }

        /**
         *  崩溃信息存储
         *
         * @param ctx
         * @param e
         * @param crashTag 设置异常信息头部tag信息，如果需要自定义将覆盖默认Tag，默认的Tag信息有：异常出现时间、设备型号、版本号、IP、MAC。
         * @param savePath 异常信息本地存储路径，默认路径为: Android/data/当前程序Id/cache/log/当前时间戳
         */
        fun saveCrashInfo(ctx: Context,e: Throwable?, crashTag:Map<String,String>?=null,savePath:String?=null) {

            val infoWriter = StringWriter()
            val printWriter = PrintWriter(infoWriter)
            e?.printStackTrace(printWriter)

            var cause = e?.cause
            while (cause != null) {
                cause.printStackTrace(printWriter)
                cause=cause.cause
            }
            val info=infoWriter.toString()
            val time=TimeUtils.timeStampToDateString(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss")
            val sb=StringBuffer().apply {

                if (!crashTag.isNullOrEmpty()){
                    crashTag.keys.forEach {
                        append("${it}${crashTag[it]}")
                        append("\r\n")
                    }
                }else{
                    append("时间：${time}")
                    append("\r\n")
                    append("设备型号：${DeviceUtils.getModel()}")
                    append("\r\n")
                    append("版本号：${DeviceUtils.getAppVersionCode(ctx)}")
                    append("\r\n")
                    append("IP：${DeviceUtils.getIpAddress(ctx)}")
                    append("\r\n")
                    append("MAC：${DeviceUtils.getMacAddress(ctx)}")
                    append("\r\n")

                }
                append(info)
            }

            val filePath=savePath?:let { ctx.externalCacheDir.toString()+File.separator+"log"+File.separator+time}

            FileUtil.writeToFile(filePath,sb.toString(),true)
        }
    }

    init {
        mSavePath=mContext.getExternalFilesDir(null).toString()+"/log"
    }

    /**
     * 启动自定义异常捕获
     */
    fun start(){
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * 开启异常文件存储
     */
    fun openSave(){
        this.saveFile=true
    }

    /**
     * 关闭异常文件存储
     */
    fun closeSave(){
        this.saveFile=false
    }

    /**
     * 设置存储信息的自定义Tag
     */
    fun setTag(crashTag:Map<String,String>)=this.apply{
        this.mCrashTag=crashTag
    }

    /**
     * 设置自定义存储路径
     */
    fun setSavePath(path:String)=this.apply {
        this.mSavePath=path
    }

    /**
     * 设置监听
     */
    fun setListener(listener:UncaughtExceptionListener) {
        this.mListener=listener
    }

    /**
     * 异常接收
     */
    override fun uncaughtException(t: Thread?, e: Throwable?) {
        if (mSavePath.isNullOrBlank())return

        if (saveFile){
            saveCrashInfo(mContext,e,mCrashTag,mSavePath)
        }

        mListener?.uncaughtException(e)
    }

    fun interface UncaughtExceptionListener{

        /**
         * 可以自定义为捕获异常的一些处理,比如：出现异常时退出程序
         */
        fun uncaughtException(e:Throwable?)
    }
}