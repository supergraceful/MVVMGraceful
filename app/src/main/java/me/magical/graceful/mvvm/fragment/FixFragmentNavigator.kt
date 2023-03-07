package me.magical.graceful.mvvm.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import me.magical.mvvmgraceful.ext.GLog
import java.util.*


/**
 *
 */
@Navigator.Name("fixFragment")
class FixFragmentNavigator(val context: Context,val manager: FragmentManager,val containerId: Int) :
    FragmentNavigator(context, manager, containerId) {

    private val mContext = context
    private val mManager = manager
    private val mContainerId = containerId


    private val TAG = "FixFragmentNavigator"

    override fun navigate(
        entries: List<NavBackStackEntry>,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ) {
        if (manager.isStateSaved) {
            GLog.i("navigate 没有执行")
            return
        }
        for (entry in entries) {
            navigate(entry, navOptions, navigatorExtras)
        }
    }

    private fun navigate(
        entry: NavBackStackEntry,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ) {

        val savedIds = try {
            val targetClass: Class<*> = this.javaClass.superclass
            val obj = targetClass.cast(this) as FragmentNavigator
            val field = targetClass.getDeclaredField("savedIds")
            //修改访问限制
            field.isAccessible = true

            field[obj] as MutableSet<String>
        } catch (e: Exception) {
            e.printStackTrace()
            mutableSetOf()
        }
        val initialNavigation = state.backStack.value.isEmpty()
        val restoreState = (
                navOptions != null && !initialNavigation &&
                        navOptions.shouldRestoreState() &&
                        savedIds.remove(entry.id)
                )
        if (restoreState) {
            // Restore back stack does all the work to restore the entry
            manager.restoreBackStack(entry.id)
            state.push(entry)
            return
        }
        val ft = createFragmentTransaction(entry, navOptions)

        if (!initialNavigation) {
            ft.addToBackStack(entry.id)
        }

        if (navigatorExtras is Extras) {
            for ((key, value) in navigatorExtras.sharedElements) {
                ft.addSharedElement(key, value)
            }
        }
        ft.commit()
        // The commit succeeded, update our view of the world
        state.push(entry)

    }

    @Nullable
    override fun navigate(
        destination: Destination,
        @Nullable args: Bundle?,
        @Nullable navOptions: NavOptions?,
        @Nullable navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        if (manager.isStateSaved()) {
            Log.i(
                TAG, "Ignoring navigate() call: FragmentManager has already"
                        + " saved its state"
            )
            return null
        }
        var className = destination.className
        if (className[0] == '.') {
            className = mContext.packageName + className
        }
        var frag: Fragment? = manager.findFragmentByTag(className)
        if (null == frag) {
            //不存在，则创建
            frag = instantiateFragment(mContext, manager, className, args)
        }
        frag.arguments = args
        val ft: FragmentTransaction = manager.beginTransaction()
        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }

//        ft.replace(mContainerId, frag);
        val fragments: List<Fragment> = manager.getFragments()
        for (fragment in fragments) {
            ft.hide(fragment)
        }
        if (!frag.isAdded) {
            ft.add(mContainerId, frag, className)
        }
        ft.show(frag)
        ft.setPrimaryNavigationFragment(frag)
        @IdRes val destId = destination.id

        //通过反射获取mBackStack
        val mBackStack: ArrayDeque<Int>
        try {
            val field = FragmentNavigator::class.java.getDeclaredField("mBackStack")
            field.setAccessible(true)
            mBackStack=field.get(this) as ArrayDeque<Int>
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        val initialNavigation = mBackStack.isEmpty()
        val isSingleTopReplacement = (navOptions != null && !initialNavigation
                && navOptions.shouldLaunchSingleTop()) && mBackStack.peekLast() == destId
        val isAdded: Boolean
        isAdded = if (initialNavigation) {
            true
        } else if (isSingleTopReplacement) {
            if (mBackStack.size > 1) {
                manager.popBackStack(
                    generateBackStackName(mBackStack.size, mBackStack.peekLast()),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                ft.addToBackStack(generateBackStackName(mBackStack.size, destId))
            }
            false
        } else {
            ft.addToBackStack(generateBackStackName(mBackStack.size + 1, destId))
            true
        }
        if (navigatorExtras is Extras) {
            (navigatorExtras as Extras).sharedElements.forEach{
                ft.addSharedElement(it.key, it.value)
            }
        }
        ft.setReorderingAllowed(true)
        ft.commit()
        return if (isAdded) {
            mBackStack.add(destId)
            destination
        } else {
            null
        }
    }

    //navigate需要打方法重复类直接复制过来就可以
    private fun generateBackStackName(backStackIndex: Int, destId: Int): String {
        return "$backStackIndex-$destId"
    }
    private fun createFragmentTransaction(
        entry: NavBackStackEntry,
        navOptions: NavOptions?
    ): FragmentTransaction {
        val destination = entry.destination as Destination
        val args = entry.arguments
        var className = destination.className
        if (className[0] == '.') {
            className = context.packageName + className
        }
//        val frag = manager.fragmentFactory.instantiate(context.classLoader, className)

        var frag = manager.findFragmentByTag(className)
        if (null == frag) {
            //不存在，则创建
            frag = manager.fragmentFactory.instantiate(mContext.classLoader, className)
        }
        frag.arguments = args
        val ft = manager.beginTransaction()
        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }
//        ft.replace(containerId, frag)
        //replace换成show和hide
        val fragments = manager.fragments
        for (fragment in fragments) {
            //NavHostFragment不能hide
            if(fragment.isAdded && fragment.isVisible && fragment !is NavHostFragment) {
                ft.hide(fragment)
            }
        }
        if (!frag.isAdded) {
            ft.add(mContainerId, frag, className)
        }
        ft.show(frag)
        ft.setPrimaryNavigationFragment(frag)
        ft.setReorderingAllowed(true)
        return ft
    }
}
