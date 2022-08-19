package me.magical.graceful.core

import android.app.Application
import android.util.Log
import dalvik.system.BaseDexClassLoader
import me.magical.mvvmgraceful.base.BaseApplication
import me.magical.mvvmgraceful.ext.GLog
import me.magical.mvvmgraceful.ext.kv.KVStorage

class MyApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        GLog.isShow=true
    }

    override fun initAppManager() {
        super.initAppManager()
    }

    override fun getKV(): KVStorage<*> {
        return super.getKV()
    }


}