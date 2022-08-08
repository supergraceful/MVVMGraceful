package me.magical.mvvmgraceful.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider

abstract class BaseActivity<DB : ViewDataBinding>:AppCompatActivity() {

    protected lateinit var mBing: DB

    @LayoutRes
    abstract fun getLayout():Int

    abstract fun initView(savedInstanceState: Bundle?)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewDataBinding()
        //view初始化
        initView(savedInstanceState)

    }

    private fun initViewDataBinding() {
        mBing = DataBindingUtil.setContentView(this, getLayout())
    }

    fun showLoading():String{
        return "加载中..."
    }
    fun dismissLoading(){

    }
}