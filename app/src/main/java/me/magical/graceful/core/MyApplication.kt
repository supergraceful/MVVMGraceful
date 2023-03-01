package me.magical.graceful.core

import dagger.hilt.android.HiltAndroidApp
import me.magical.mvvmgraceful.base.application.BaseApplication
import me.magical.mvvmgraceful.ext.GLog
import me.magical.mvvmgraceful.ext.kv.KVStorage
import me.magical.mvvmgraceful.helper.CrashHelper

@HiltAndroidApp
class MyApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        GLog.isShow=true

//
//        //自定义未捕获异常信息Tag
//        val crashTag=HashMap<String,String>()
//        crashTag["用户ID："]=""
//        crashTag["IP："]=""
//        crashTag["MAC："]=""
//
//        //开启崩溃处理，等出现未捕获异常时程序不会崩溃，会将信息存储在文件中
//        CrashHelper.instance.apply {
//            //设置异常信息文件存储路径
//            setSavePath("")
//            //设置异常信息头部tag信息，如果需要自定义将覆盖默认Tag，默认的Tag信息有：异常出现时间、设备型号、版本号、IP、MAC。
//            //可以使用DeviceUtils静态类获取部分设备信息
//            setTag(crashTag)
//            //启动
//            start()
//        }
//        //当出现未捕获异常时可自定义处理事件，比如：关闭当前页、退出程序等
//        CrashHelper.instance.setListener{
//
//        }

//        CrashHelper.instance.start()
    }

    override fun initAppManager() {
        super.initAppManager()
    }

    override fun getKV(): KVStorage<*> {
        return super.getKV()
    }


}