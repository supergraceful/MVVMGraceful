package me.magical.mvvmgraceful.base.fragment

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import me.magical.mvvmgraceful.base.BaseViewModel
import me.magical.mvvmgraceful.utils.ToastUtils
import me.magical.mvvmgraceful.utils.UtilsBridge

abstract class BaseMVVMFragment<DB : ViewDataBinding, VM : BaseViewModel>:BaseFragment<DB>(){

    private var mViewModelId: Int = 0
    protected lateinit var mViewModel: VM

    override fun initView(savedInstanceState: Bundle?) {
        //利用反射获取viewmodel对象
        val viewModelClass = UtilsBridge.getViewModelClass<VM>(javaClass)
        mViewModel = ViewModelProvider(this).get(viewModelClass)
        mViewModelId = initVariableId()
        mBing.lifecycleOwner = this
        mBing.setVariable(mViewModelId, mViewModel)

        //界面触发事件
        registorUIChangeLiveDataCallBack()
    }

    private fun registorUIChangeLiveDataCallBack() {
        mViewModel.mUIData.showLoadingEvent.observe(this){
            showLoading(it)
        }
        mViewModel.mUIData.dismissLoadingEvent.observe(this){
            dismissLoading()
        }
        mViewModel.mUIData.showToastEvent.observe(this){
            ToastUtils.showLong(it)
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
}