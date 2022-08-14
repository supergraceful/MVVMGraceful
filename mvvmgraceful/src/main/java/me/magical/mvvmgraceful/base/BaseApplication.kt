package me.magical.mvvmgraceful.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

import me.magical.mvvmgraceful.ext.kv.KVStorage
import me.magical.mvvmgraceful.ext.kv.KVUtil
import me.magical.mvvmgraceful.ext.kv.MMKVStorage
import me.magical.mvvmgraceful.ext.kv.SPStorage

open class BaseApplication : Application(), ViewModelStoreOwner {

    var mApplication: Application = this

    private lateinit var mAppViewModelStore: ViewModelStore

    private var mFactory: ViewModelProvider.Factory? = null

    override fun onCreate() {
        super.onCreate()
        mAppViewModelStore = ViewModelStore()
        initKV()
        initAppManager()
    }

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }

    /**
     * 设置activity管理栈
     */
    open fun initAppManager() {
        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                AppManager.instance.addActivity(activity)
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                AppManager.instance.removeActivity(activity)
            }

        })
    }

    /**
     * 初始化全局kv存储类型,已默认实现mmkv和SharedPreferences存储，如果需要其他的kv存储可实现KVStorage并重写initKV
     */
    open fun getKV(): KVStorage<*>{
        return SPStorage(this)
    }

    open fun initKV(){
        KVUtil.instance.create(getKV())
    }

    /**
     * 获取一个全局的ViewModel
     */
    fun getAppViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(this, this.getAppFactory())
    }

    private fun getAppFactory(): ViewModelProvider.Factory {
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        }
        return mFactory as ViewModelProvider.Factory
    }
}