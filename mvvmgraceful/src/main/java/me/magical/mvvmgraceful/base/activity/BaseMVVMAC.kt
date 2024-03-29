package me.magical.mvvmgraceful.base.activity

import android.content.Intent
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import me.magical.mvvmgraceful.base.BaseViewModel
import me.magical.mvvmgraceful.base.BaseViewModel.Companion.BUNDLE
import me.magical.mvvmgraceful.base.BaseViewModel.Companion.CLASS
import me.magical.mvvmgraceful.utils.ToastUtils
import me.magical.mvvmgraceful.utils.UtilsBridge

abstract class BaseMVVMAC<DB : ViewDataBinding, VM : BaseViewModel> : BaseAC<DB>() {

    protected lateinit var mViewModel: VM

    private var mViewModelId: Int? = null


    abstract fun createObserver()

    /**
     * 返回VariableId不建议使用BR._all，尽量使用xml中使data定义的name所生成的BR值
     */
    abstract fun initVariableId(): Int?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //viewModel基础交互的观察者
        initObserver()
        //创建数据观察者
        createObserver()
    }

    override fun initViewDataBinding() {
        super.initViewDataBinding()
        initViewModel()
    }

    /**
     * 初始化viewmodel并绑定生命周期
     */
    protected open fun initViewModel() {
        //利用反射获取viewmodel对象
        val viewModelClass = UtilsBridge.getViewModelClass<VM>(javaClass)
        mViewModel=ViewModelProvider(this)[viewModelClass]
        mViewModelId = initVariableId()
        //支持LiveData绑定xml，数据改变，UI自动会更新
        mBinding.lifecycleOwner = this

        mViewModelId?.let {
            //关联ViewModel
            mBinding.setVariable(it, mViewModel)
        }

    }

    /**
     * viewModel基础交互的观察者：
     * 弹出隐藏loading
     * 弹出Toast
     * 界面跳转
     * 带参跳转
     * finish
     * onBackPressed
     */
    private fun initObserver() {
        mViewModel.mUIData.showLoadingEvent.observe(this) {
            showLoading(it)
        }
        mViewModel.mUIData.dismissLoadingEvent.observe(this) {
            dismissLoading()
        }
        mViewModel.mUIData.showToastEvent.observe(this) {
            showToast(it)
        }
        mViewModel.mUIData.startActivityEvent.observe(this) {
            val clz = it[CLASS] as Class<*>
            val bundle = it[BUNDLE] as Bundle
            startActivity(clz, bundle)
        }
        mViewModel.mUIData.finishEvent.observe(this) {
            finish()
        }
        mViewModel.mUIData.onBackPressedEvent.observe(this) {
            onBackPressed()
        }
    }

    fun startActivity(clz: Class<*>, bundle: Bundle?=null) {
        val mIntent = Intent(this, clz)
        bundle?.also {
            mIntent.putExtras(bundle)
        }
        startActivity(mIntent)
    }

    /**
     * 刷新数据
     */
    fun refreshLayout() {
        mBinding.setVariable(mViewModelId!!, mViewModel)
    }

}