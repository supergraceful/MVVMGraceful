package me.magical.graceful.mvvm.fragment

import android.os.Bundle
import me.magical.graceful.BR
import me.magical.graceful.R
import me.magical.graceful.databinding.FragmentHomeBinding
import me.magical.graceful.mvvm.viewModel.HomeVM
import me.magical.mvvmgraceful.base.fragment.BaseFM
import me.magical.mvvmgraceful.base.fragment.BaseMVVMFM
import me.magical.mvvmgraceful.ext.GLog
import me.magical.mvvmgraceful.request.core.DataState

class HomeFragment: BaseMVVMFM<FragmentHomeBinding,HomeVM>() {

    companion object {
        const val ARG_PARAM1 = "param1"
        const val ARG_PARAM2 = "param2"
        @JvmStatic
        fun newInstance(param1: String="", param2: String="") =
            ExploreFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    override fun getLayout(savedInstanceState: Bundle?): Int = R.layout.fragment_home

    override fun initVariableId(): Int =BR.homeVM

    override fun createObserver() {

        mViewModel.videoLiveData.observe(this){
            when(it){
                is DataState.OnStart->{
                    GLog.i("开始获取视频")
                }
                is DataState.OnSuccess->{
                    mBinding.tvHomeShow.text=it.data.toString()
                }
                is DataState.OnError->{
                    it.exception.printStackTrace()
                }
                is DataState.OnComplete->{
                    GLog.i("获取完成")
                }
            }
        }
    }

    override fun lazyLoadData() {
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

}