package me.magical.graceful.frgment

import android.os.Bundle
import me.magical.graceful.R
import me.magical.graceful.databinding.TestFragmentBinding
import me.magical.mvvmgraceful.base.fragment.BaseFragment

class TestFragment : BaseFragment<TestFragmentBinding>() {

    /**
     * 懒加载
     */
    override fun lazyLoadData() {

    }

    /**
     * fragment所要绑定的xml的id
     */
    override fun getLayout(saedInstanceState: Bundle?) = R.layout.test_fragment

    /**
     * 初始化一些ui相关的东西
     */
    override fun initView(savedInstanceState: Bundle?) {

    }
}