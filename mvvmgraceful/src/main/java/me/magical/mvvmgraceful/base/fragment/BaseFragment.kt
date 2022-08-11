package me.magical.mvvmgraceful.base.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle


abstract class BaseFragment<DB : ViewDataBinding> : Fragment() {

    //是否为第一次启动
    private var mFirst = true
    private val mHandler = Handler()
    protected lateinit var mBing: DB


    lateinit var mActivity: AppCompatActivity

    /**
     * 懒加载
     */
    abstract fun lazyLoadData()

    abstract fun initVariableId(): Int

    @LayoutRes
    abstract fun getLayout(savedInstanceState: Bundle?): Int

    abstract fun initView(savedInstanceState: Bundle?)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //初始化bing和viewmodel
        initViewDataBinding(inflater, container, savedInstanceState)

        return mBing.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFirst = true

        initView(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        onVisible()
    }

    //懒加载
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && mFirst) {
            mHandler.postDelayed({ lazyLoadData() }, lazyLoadTime())
        }
    }

    private fun initViewDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        //绑定databing
        mBing = DataBindingUtil.inflate(inflater, getLayout(savedInstanceState), container, false)

    }


    fun startActivity(clz: Class<*>, bundle: Bundle?) {
        val mIntent = Intent(activity, clz)
        bundle?.also {
            mIntent.putExtras(bundle)
        }
        startActivity(mIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }

    /**
     * 延迟加载事件，毫秒
     */
    open fun lazyLoadTime(): Long {
        return 300
    }

    protected open fun showLoading(): String {
        return "加载中..."
    }

    protected open fun dismissLoading() {}

}