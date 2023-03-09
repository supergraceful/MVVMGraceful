package me.magical.graceful.mvvm.activity

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import me.magical.graceful.R
import me.magical.graceful.databinding.ActivityNewsBinding
import me.magical.graceful.mvvm.viewModel.NewsVM
import me.magical.mvvmgraceful.BR
import me.magical.mvvmgraceful.base.activity.BaseMVVMAC
import me.magical.mvvmgraceful.ext.GLog
import me.magical.mvvmgraceful.request.core.DataState


@AndroidEntryPoint
class NewsActivity : BaseMVVMAC<ActivityNewsBinding, NewsVM>() {

    override fun getLayout(): Int = R.layout.activity_news

    override fun initVariableId(): Int = BR.newsVM

    override fun initView(savedInstanceState: Bundle?) {

    }
    override fun createObserver() {
        mViewModel.resultState.observe(this){
            when(it){
                is DataState.OnStart->{
                    GLog.i("请求开始")
                }
                is DataState.OnSuccess->{
                    GLog.i("请求结果")
                    mBinding.tvNewsShow.text=it.data.toString()
                }
                is DataState.OnError->{
                    GLog.e("请求异常${it.exception.printStackTrace()}")
                    mBinding.tvNewsShow.text=it.exception.msg
                }
                is DataState.OnComplete->{
                    GLog.i("请求完成")
                }
            }
        }
    }
}