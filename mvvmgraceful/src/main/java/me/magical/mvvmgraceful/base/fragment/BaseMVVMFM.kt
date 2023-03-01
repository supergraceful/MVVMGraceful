package me.magical.mvvmgraceful.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import me.magical.mvvmgraceful.base.BaseViewModel
import me.magical.mvvmgraceful.utils.ToastUtils
import me.magical.mvvmgraceful.utils.UtilsBridge

abstract class BaseMVVMFM<DB : ViewDataBinding, VM : BaseViewModel>:BaseFM<DB>(){

    private var mViewModelId: Int?=null

    protected  val mViewModel: VM by lazy {
        //利用反射获取viewmodel对象
        val viewModelClass = UtilsBridge.getViewModelClass<VM>(javaClass)
        ViewModelProvider(this)[viewModelClass]
    }

    abstract fun initVariableId(): Int?

    abstract fun createObserver()

    override fun initViewDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        super.initViewDataBinding(inflater, container, savedInstanceState)
        initViewModel()
    }

    protected open fun initViewModel(){

        mViewModelId = initVariableId()
        mBinding.lifecycleOwner = this
        mViewModelId?.let {
            mBinding.setVariable(it, mViewModel)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        createObserver()
    }

    private fun initObserver() {
        mViewModel.mUIData.showLoadingEvent.observe(this){
            showLoading(it)
        }
        mViewModel.mUIData.dismissLoadingEvent.observe(this){
            dismissLoading()
        }
        mViewModel.mUIData.showToastEvent.observe(this){
            showToast(it)
        }
        mViewModel.mUIData.startActivityEvent.observe(this){
            val clz = it[BaseViewModel.CLASS] as Class<*>
            val bundle = it[BaseViewModel.BUNDLE] as Bundle
            startActivity(clz, bundle)
        }
        mViewModel.mUIData.finishEvent.observe(this){
            activity?.finish()
        }
        mViewModel.mUIData.onBackPressedEvent.observe(this){
            activity?.onBackPressed()
        }
    }

    /**
     * 刷新数据
     */
    fun refreshLayout(){
        mBinding.setVariable(mViewModelId!!, mViewModel)
    }

}