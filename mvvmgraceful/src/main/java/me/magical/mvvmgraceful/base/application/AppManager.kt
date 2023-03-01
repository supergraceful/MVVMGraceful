package me.magical.mvvmgraceful.base.application

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AppManager private constructor() {

    companion object {
        private lateinit var activityStack: Stack<Activity>
        private lateinit var activityFMMap: HashMap<Activity, HashMap<String, Fragment>>
        val instance: AppManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AppManager()
        }
    }

    init {
        activityStack = Stack<Activity>()
        activityFMMap = HashMap()
    }

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity?) {
        activityStack.push(activity)
    }

    /**
     * 移除指定的Activity
     */
    fun removeActivity(activity: Activity?) {
        activity.also {
            activityStack.remove(it)
        }
    }

    /**
     * 是否有activity
     */
    fun isActivity(): Boolean {
        return !activityStack.isEmpty()
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity? {
        return if (activityStack.isEmpty()) {
            null
        } else activityStack.lastElement()
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        val activity: Activity = activityStack.lastElement()
        finishActivity(activity)
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        activity?.also {
            if (!it.isFinishing) {
                it.finish()
                removeActivity(it)
            }
        }

    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        for (activity in activityStack) {
            if (activity.javaClass == cls) {
                finishActivity(activity)
                break
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        activityStack.forEach {
            it?.also {
                finishActivity(it)
            }
        }
        activityStack.clear()
    }

    /**
     * 获取指定的Activity
     *
     * @author kymjs
     */
    fun getActivity(cls: Class<*>): Activity? {
        for (activity in activityStack) {
            if (activity.javaClass == cls) {
                return activity
            }
        }
        return null
    }

    /**
     * 添加Fragment到堆栈
     */
    fun addFragment(activity: Activity, fragment: Fragment, tag:String?) {
       val tempTag= tag
           ?: if (fragment.tag==null){
               "${fragment::javaClass.name} - ${System.currentTimeMillis()}"
           }else{
               fragment.tag
           }

        if (activityFMMap.containsKey(activity)) {
            activityFMMap[activity]!![tempTag!!] = fragment
        } else {
            val fragmentMap = HashMap<String, Fragment>()
            fragmentMap[tempTag!!]=fragment
            activityFMMap[activity] = fragmentMap
        }
    }

    /**
     * 移除指定的Fragment
     */
    fun removeFragment(activity: Activity, fragment: Fragment) {
        if (!activityFMMap.containsKey(activity)) return
        val map = activityFMMap[activity]!!
        if (map.containsValue(fragment)) {
        }
    }


//    /**
//     * 当前activity是否存在指定Fragment
//     */
//    fun isFragment(activity: Activity, fragment: Fragment): Boolean {
//        if (!fragmentMap.containsKey(activity)) return false
//
//        return fragmentMap[activity]!!.contains(fragment)
//    }
//
//    /**
//     * 当前activity是否存在指定Fragment
//     */
//    fun isFragment(activity: FragmentActivity, tag: String): Boolean {
//        if (!fragmentMap.containsKey(activity)) return false
//
//        val fragment = activity.supportFragmentManager.findFragmentByTag(tag)
//        fragment ?: return false
//        return fragmentMap[activity]!!.contains(fragment)
//    }

//    /**
//     * 获取当前Activity（堆栈中最后一个压入的）
//     */
//    fun currentFragment(): Fragment? {
//        return fragmentStack.lastElement()
//    }

    /**
     * 退出应用程序
     */
    fun AppExit() {
        try {
            finishAllActivity()
            // 杀死该应用进程
//          android.os.Process.killProcess(android.os.Process.myPid());
//            调用 System.exit(n) 实际上等效于调用：
//            Runtime.getRuntime().exit(n)
//            finish()是Activity的类方法，仅仅针对Activity，当调用finish()时，只是将活动推向后台，并没有立即释放内存，活动的资源并没有被清理；当调用System.exit(0)时，退出当前Activity并释放资源（内存），但是该方法不可以结束整个App如有多个Activty或者有其他组件service等不会结束。
//            其实android的机制决定了用户无法完全退出应用，当你的application最长时间没有被用过的时候，android自身会决定将application关闭了。
            //System.exit(0);
        } catch (e: Exception) {
            activityStack.clear()
            e.printStackTrace()
        }
    }
}