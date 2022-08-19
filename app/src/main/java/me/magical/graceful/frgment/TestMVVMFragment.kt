package me.magical.graceful.frgment

import android.os.Bundle
import me.magical.graceful.BR
import me.magical.graceful.R
import me.magical.graceful.databinding.TestFragmentBinding
import me.magical.mvvmgraceful.base.fragment.BaseMVVMFragment

class TestMVVMFragment :BaseMVVMFragment<TestFragmentBinding,TestMVVMVViewModel>() {

    /**
     * 返回VariableId不建议使用BR._all，尽量使用xml中使data定义的name所生成的BR值
     */
    override fun initVariableId() =BR._all
    /**
     * 绑定的xml布局id
     */

    override fun getLayout(savedInstanceState: Bundle?)= R.layout.test_fragment
    /**
     * 懒加载
     */
    override fun lazyLoadData() {

    }

    /**
     * 创建一些mBinding的观察者
     */
    override fun createObserver() {

    }

    /**
     * 初始化view
     */
    override fun initView(savedInstanceState: Bundle?) {

    }

    /**
     * 懒加载延迟时间毫秒，默认300毫秒
     */
    override fun lazyLoadTime(): Long {
        return super.lazyLoadTime()
    }
}