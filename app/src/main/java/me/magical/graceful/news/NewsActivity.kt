package me.magical.graceful.news

import android.os.Bundle
import android.util.Log
import me.magical.graceful.BR
import me.magical.graceful.R
import me.magical.graceful.databinding.NewsActivityBinding
import me.magical.mvvmgraceful.base.activity.BaseMVVMActivity
import me.magical.mvvmgraceful.ext.GLog
import me.magical.mvvmgraceful.ext.parse
import me.magical.mvvmgraceful.request.core.DataState


class NewsActivity : BaseMVVMActivity<NewsActivityBinding, NewsViewModel>() {

    /**
     * 创建viewmodel的观察者
     */
    override fun createObserver() {

        mViewModel.requestData.observe(this) {
            /**
             * 状态返回式结果，转换为回调
             * 参数一：DataState对象，非空
             * 参数二：请求开始时回调，可为空
             * 参数三：请求成功回调，非空
             * 参数三：请求失败回调，可为空
             * 参数三：请求完成回调，可为空
             */
            parse(
                it,
                {
                    //请求开始时参数 it string类型
                    GLog.e("start ")
                }, { result ->
                    //请求成功数据 it 具体类型需要根据请求返回参数
                    mBinding.tvNewsData.text = result.toString()
                    GLog.e("success" + result.toString())
                }, { error ->
                    //请求失败数据 it CustomException类型
                    GLog.e("error" + error.message)
                }, {
                    GLog.e("Complete")
                })

            //状态返回式结果
//            when (it) {
//                is DataState.OnStart -> {
//                    mBinding.tvNewsData.text = it.message
//                    GLog.e("activity",it.message)
//                }
//                is DataState.OnSuccess -> {
//                    mBinding.tvNewsData.text = it.data.toString()
//                    GLog.e("activity",it.data)
//                }
//                is DataState.OnError -> {
//                    GLog.e("activity",it.exception.message)
//                }
//                is DataState.OnComplete -> {
//                    GLog.e("activity","OnComplete")
//                }
//            }
        }
    }

    /**
     * 返回VariableId不建议使用BR._all，尽量使用xml中使data定义的name所生成的BR值
     */
    override fun initVariableId(): Int {
        return BR.newViewModel
    }

    /**
     * 绑定的xml布局id
     */
    override fun getLayout(): Int {
        return R.layout.news_activity
    }
//    override fun getLayout()=R.layout.test_activity

    /**
     * 初始化view
     */
    override fun initView(savedInstanceState: Bundle?) {
    }
}