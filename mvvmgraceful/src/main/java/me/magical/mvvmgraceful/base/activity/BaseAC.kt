package me.magical.mvvmgraceful.base.activity

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import dagger.hilt.android.AndroidEntryPoint

import me.magical.mvvmgraceful.ext.Loading
import me.magical.mvvmgraceful.helper.hideSoftInput
import me.magical.mvvmgraceful.helper.isShouldHideInput
import me.magical.mvvmgraceful.ui.loading.sprite.SpriteContainer
import me.magical.mvvmgraceful.utils.ToastUtils
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Named

abstract class BaseAC<DB : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var mBinding: DB

    protected var mLoading: Loading?=null

    protected var mContext: WeakReference<Context>?=null

    @LayoutRes
    abstract fun getLayout(): Int

    abstract fun initView(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = WeakReference<Context>(this)

        mLoading=Loading(this)
        //初始化databing
        initViewDataBinding()

        //view初始化
        initView(savedInstanceState)

    }

    protected open fun initViewDataBinding() {
        mBinding = DataBindingUtil.setContentView(this, getLayout())
    }

    /**
     * 如果想要去除调默认的loading可以复写该方法并将loading置空，同时需要showLoading和dismissLoading方法进行空实现
     *
     * https://github.com/ybq/Android-SpinKit
     * 设置默认loading样式
     * 如果使用默认的loading，可以
     * @param spriteContainer loading样式，默认Wave()支持以下样式
     *
     * RotatingPlane()，DoubleBounce()，Wave()，WanderingCubes()，Pulse()，ChasingDots()
     * ThreeBounce()，Circle()，CubeGrid()，FadingCircle()，FoldingCube()，RotatingCircle()
     * MultiplePulse()，PulseRing()，MultiplePulseRing()
     *
     * @param color loading的颜色，默认 #03DAC5
     */
    protected open fun setDefaultLoading(spriteContainer: SpriteContainer, color: Int? = null) {
        mLoading = if (color == null) {
            Loading(this, spriteContainer)
        } else {
            Loading(this, spriteContainer, color)
        }

    }

    override fun onStop() {
        super.onStop()
        dismissLoading()
    }

    /**
     * 显示loading,在使用uiRequest发起网络访问，并开启isLoading（默认开启）会自动调用
     */
    protected open fun showLoading(title: String) {
        mLoading?.title = title
        mLoading?.show()
    }

    /**
     * 隐藏loading，在使用uiRequest完成网络访问，并开启isLoading（默认开启）会自动调用
     */
    protected open fun dismissLoading() {
        mLoading?.dismiss()
    }

    /**
     * 显示Toast，在使用uiRequest 发生异常时，并开启isToast（默认开启）会自动调用
     */
    protected open fun showToast(str:String?) {
        ToastUtils.showLong(str)
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissLoading()
        mLoading = null
    }

    /**
     * 当前点击位置不在输入框内时失去焦点
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v)
            }
        }
        return super.dispatchTouchEvent(ev)
    }


}