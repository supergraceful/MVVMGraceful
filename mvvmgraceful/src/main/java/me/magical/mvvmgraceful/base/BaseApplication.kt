package me.magical.mvvmgraceful.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

import me.magical.mvvmgraceful.ext.kv.KVStorage
import me.magical.mvvmgraceful.ext.kv.KVUtil
import me.magical.mvvmgraceful.ext.kv.MMKVStorage
import me.magical.mvvmgraceful.ext.kv.SPStorage

open class BaseApplication : Application(), ViewModelStoreOwner {

    private lateinit var mAppViewModelStore: ViewModelStore

    private var mFactory: ViewModelProvider.Factory? = null

    companion object{

        @JvmStatic
        @Synchronized
        fun setApplication(context:Application){
            KVUtil.instance.create(SPStorage(context))
            context.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
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
    }
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
     * ??????activity?????????
     */
    open fun initAppManager() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
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
     * ???????????????kv????????????,??????SharedPreferences??????????????????mmkv???SharedPreferences??????????????????
     * ????????????kv???????????????KVStorage?????????getKV()
     */
    open fun getKV(): KVStorage<*>{
        return SPStorage(this)

    }

    open fun initKV(){
        KVUtil.instance.create(getKV())
    }

    /**
     * ?????????????????????ViewModel
     */
    fun getAppViewModelProvider(context:Application): ViewModelProvider {
        return ViewModelProvider(this, this.getAppFactory())
    }

    private fun getAppFactory(): ViewModelProvider.Factory {
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        }
        return mFactory as ViewModelProvider.Factory
    }
}