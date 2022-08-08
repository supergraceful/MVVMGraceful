package me.magical.mvvmgraceful.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

abstract class BaseMVVMActivity<DB : ViewDataBinding, VM : ViewModel> :BaseActivity<DB>(){

    abstract fun createObserver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //创建数据观察者
        createObserver()
    }
}