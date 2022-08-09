package me.magical.mvvmgraceful.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import me.magical.mvvmgraceful.utils.KVUtil

class BaseApplication: Application() {

    var mApplication: Application = this

    override fun onCreate() {
        super.onCreate()
        KVUtil.getInstance().init(this,  KVUtil.StorageType.MMKV);
//        RetrofitClient.getInstance()
        setApplication()
    }

    private fun setApplication() {
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
}