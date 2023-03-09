package me.magical.graceful.core

import dagger.hilt.android.HiltAndroidApp
import me.magical.graceful.BuildConfig
import me.magical.mvvmgraceful.base.application.BaseApplication
import me.magical.mvvmgraceful.crash.Cockroach
import me.magical.mvvmgraceful.crash.ExceptionHandler
import me.magical.mvvmgraceful.ext.GLog
import me.magical.mvvmgraceful.ext.kv.KVStorage
import me.magical.mvvmgraceful.ext.kv.MMKVStorage
import me.magical.mvvmgraceful.helper.ChoreographerHelper
import me.magical.mvvmgraceful.helper.CrashHelper
import me.magical.mvvmgraceful.request.interceptor.logging.LoggingInterceptor.Builder

@HiltAndroidApp
class MyApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        GLog.isShow=true
        initCrashUtil()
        //掉帧检测
        if (BuildConfig.DEBUG){
            ChoreographerHelper.start()
        }

    }

    override fun getKV(): KVStorage<*> {
//        return MMKVStorage(this)
        return super.getKV()
    }


    //统一捕获全局未捕获异常并存储异常信息
    private fun initCrash(){
        //自定义未捕获异常信息Tag
        val crashTag=HashMap<String,String>()
        crashTag["用户ID："]=""
        crashTag["IP："]=""
        crashTag["MAC："]=""

        //开启崩溃处理，等出现未捕获异常时程序不会崩溃，会将信息存储在文件中
        CrashHelper.instance.apply {
            //设置异常信息文件存储路径
            setSavePath("")
            //设置异常信息头部tag信息，如果需要自定义将覆盖默认Tag，默认的Tag信息有：异常出现时间、设备型号、版本号、IP、MAC。
            //可以使用DeviceUtils静态类获取部分设备信息
            setTag(crashTag)
            //启动
            start()
        }
        //当出现未捕获异常时可自定义处理事件，比如：关闭当前页、退出程序等
        CrashHelper.instance.setListener{

        }

        CrashHelper.instance.start()
    }

    //全局崩溃处理，包含异常捕获
    private fun initCrashUtil(){

        Cockroach.install(this,object: ExceptionHandler(){
            override fun onUncaughtExceptionHappened(thread: Thread?, throwable: Throwable?) {
                GLog.e( "捕获到导致崩溃的异常" + "<---")
                throwable?.printStackTrace()

                //存储崩溃信息，
                CrashHelper.saveCrashInfo(this@MyApplication,throwable)
            }

            override fun onBandageExceptionHappened(throwable: Throwable?) {
                GLog.i("打印警告级别log，该throwable可能是最开始的bug导致的，无需关心");
            }

            override fun onEnterSafeMode() {
                GLog.e("已经进入安全模式")
            }

            override fun onMayBeBlackScreen(e: Throwable?) {
                super.onMayBeBlackScreen(e)
                GLog.e("捕获黑屏" + "<---")
            }
        })
    }

}