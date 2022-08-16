package me.magical.mvvmgraceful.base.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

import me.magical.mvvmgraceful.ext.Loading
import me.magical.mvvmgraceful.ui.loading.sprite.SpriteContainer

abstract class BaseActivity<DB : ViewDataBinding>:AppCompatActivity() {

    protected lateinit var mBinding: DB

    private lateinit var loading:Loading

    @LayoutRes
    abstract fun getLayout():Int

    abstract fun initView(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loading=Loading(this)
        //初始化databing
        initViewDataBinding()

        //view初始化
        initView(savedInstanceState)

    }

    protected open fun initViewDataBinding() {
        mBinding = DataBindingUtil.setContentView(this, getLayout())
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
            Loading(this,spriteContainer)
        }else{
            Loading(this,spriteContainer,color)
        }

    }

    /**
     * 显示loading
     */
    protected open fun showLoading(title: String){
        loading.title=title
        loading.show()
    }

    /**
     * 显示loading
     */
    protected open fun dismissLoading(){
        loading.dismiss()
    }

}