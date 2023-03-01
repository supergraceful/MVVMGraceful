package me.magical.graceful.mvvm.activity

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.core.view.forEach
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import me.magical.graceful.R
import me.magical.graceful.databinding.ActivityMainBinding
import me.magical.mvvmgraceful.base.activity.BaseAC
import me.magical.mvvmgraceful.ext.GLog
import me.magical.mvvmgraceful.utils.ToastUtils


class MainActivity : BaseAC<ActivityMainBinding>() {
    var starUp: Long = 0

    override fun getLayout() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {

        initNavigation()
    }

    private fun initNavigation() {
        val homeFragment = supportFragmentManager.findFragmentById(R.id.fm_main) as NavHostFragment
        val navController = homeFragment.navController

        mBinding.navigationView.menu.forEach {
            val menuItemView = findViewById<BottomNavigationItemView>(it.itemId)
            menuItemView.setOnLongClickListener(View.OnLongClickListener {
                return@OnLongClickListener true
            })
        }
        // BottomNavigationView 设置 navController
        mBinding.navigationView.setupWithNavController(navController)


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
}