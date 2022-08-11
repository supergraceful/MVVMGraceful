package me.magical.mvvmgraceful.base.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<DB : ViewDataBinding>:AppCompatActivity() {

    protected lateinit var mBinding: DB

    @LayoutRes
    abstract fun getLayout():Int

    abstract fun initView(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //初始化databing
        initViewDataBinding()

        //view初始化
        initView(savedInstanceState)

    }

    protected open fun initViewDataBinding() {
        mBinding = DataBindingUtil.setContentView(this, getLayout())
    }

    protected open fun showLoading():String{
        return "加载中..."
    }
    protected open fun dismissLoading(){}

}