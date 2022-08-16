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
import me.magical.mvvmgraceful.ext.Loading
import me.magical.mvvmgraceful.ui.loading.sprite.SpriteContainer


abstract class BaseFragment<DB : ViewDataBinding> : Fragment() {

    //是否为第一次启动
    private var mFirst = true
    private val mHandler = Handler()
    protected lateinit var mBing: DB
    private var loading: Loading? = null

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

        loading = Loading(mActivity)
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


    /**
     * https://github.com/ybq/Android-SpinKit
     * 设置默认loading样式
     * RotatingPlane()，DoubleBounce()，Wave()，WanderingCubes()，Pulse()，ChasingDots()
     * ThreeBounce()，Circle()，CubeGrid()，FadingCircle()，FoldingCube()，RotatingCircle()
     * MultiplePulse()，PulseRing()，MultiplePulseRing()
     */
    protected open fun setDefaultLoading(spriteContainer: SpriteContainer, color:Int?=null){
        loading = if (color==null){
            Loading(mActivity,spriteContainer)
        }else{
            Loading(mActivity,spriteContainer,color)
        }

    }

    protected open fun showLoading(title: String) {
        loading?.title = title
        loading?.show()
    }

    protected open fun dismissLoading() {
        loading?.dismiss()
    }

}