package me.magical.graceful.mvvm.activity

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphNavigator
import androidx.navigation.Navigation
import androidx.navigation.NavigatorProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import dagger.hilt.android.AndroidEntryPoint
import me.magical.graceful.R
import me.magical.graceful.databinding.ActivityMainBinding
import me.magical.graceful.mvvm.fragment.ExploreFragment
import me.magical.graceful.mvvm.fragment.FixFragmentNavigator
import me.magical.graceful.mvvm.fragment.HomeFragment
import me.magical.graceful.mvvm.fragment.MineFragment
import me.magical.mvvmgraceful.base.activity.BaseAC
import me.magical.mvvmgraceful.utils.ToastUtils

@AndroidEntryPoint
class MainActivity : BaseAC<ActivityMainBinding>() {
    var starUp: Long = 0

    override fun getLayout() = me.magical.graceful.R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {

        initNavigation()
    }

    private fun initNavigation() {

        val fragmentById: Fragment? = supportFragmentManager.findFragmentById(R.id.fm_main)
        //fragment的重复加载问题和NavController有关
        val navController = NavHostFragment.findNavController(fragmentById!!)
        val provider = navController.navigatorProvider
        //设置自定义的navigator
        val fixFragmentNavictor = FixFragmentNavigator(
            this,
            supportFragmentManager, fragmentById.id
        )
        provider.addNavigator(fixFragmentNavictor)
        val navDestinations = initNavGraph(provider, fixFragmentNavictor)
        navController.graph = navDestinations

        //将BottomNavigationView和NaviGraph关联起来--->自定义navigator后这样设置会有一些奇葩的问题,谁用谁知道(╥╯^╰╥)....
        //NavigationUI.setupWithNavController(activityIndexBinding.navBottom,navController);
        mBinding.navigationView.setOnNavigationItemSelectedListener { item ->
            navController.navigate(item.itemId)
            true
        }


        //设置角标
        val badge = mBinding.navigationView.getOrCreateBadge(R.id.mineFragment)
        badge.number = 20
        //设置背景色
//        badge?.backgroundColor
        // 显示位置，TOP_START，TOP_END，BOTTOM_START，BOTTOM_END，分别对应左上角，右上角，左下角和右下角。
//        badge?.badgeGravity
        //设置文字颜色
//        badge?.badgeTextColor
        //最多显示几位数字
//        badge?.maxCharacterCount

    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val lastUp = System.currentTimeMillis()
            if (lastUp - starUp > 2000) {
                ToastUtils.showShort("再按一次退出程序")
                starUp = lastUp
                return false
            }
        }
        return super.onKeyUp(keyCode, event)
    }
    private fun initNavGraph(
        provider: NavigatorProvider,
        fragmentNavigator: FixFragmentNavigator
    ): NavGraph {
        val navGraph = NavGraph(NavGraphNavigator(provider))
        //用自定义的导航器来创建目的地
        val destination1 = fragmentNavigator.createDestination()
        destination1.id = R.id.homeFragment
        destination1.setClassName(HomeFragment::class.java.canonicalName)
        navGraph.addDestination(destination1)
        val destination2 = fragmentNavigator.createDestination()
        destination2.id = R.id.exploreFragment
        destination2.setClassName(ExploreFragment::class.java.canonicalName)
        navGraph.addDestination(destination2)
        val destination3 = fragmentNavigator.createDestination()
        destination3.id = R.id.mineFragment
        destination3.setClassName(MineFragment::class.java.canonicalName)
        navGraph.addDestination(destination3)

        navGraph.setStartDestination(R.id.homeFragment)
        return navGraph
    }
}