package me.magical.graceful

import android.os.Bundle
import android.util.Log
import me.magical.graceful.databinding.ActivityMainBinding
import me.magical.graceful.databinding.DownloadActivityBinding
import me.magical.mvvmgraceful.base.activity.BaseMVVMActivity
import me.magical.mvvmgraceful.request.download.DownloadState
import me.magical.mvvmgraceful.request.download.FileTool

class DownloadActivity: BaseMVVMActivity<DownloadActivityBinding, DownloadViewModel>() {

    override fun getLayout()= R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.btDownload.setOnClickListener {
            mViewModel.download()
        }
        mBinding.btCancel.setOnClickListener {
            mViewModel.cancel()
        }
        mBinding.btPause.setOnClickListener {
            mViewModel.pause()
        }
    }

    override fun createObserver() {
        mViewModel.listener.observe(this){
            when(it){
                is DownloadState.Progress->{
                    val progress= it.progress
                    val readSize= FileTool.bytes2kb(it.read)
                    val totalSize= FileTool.bytes2kb(it.count)

                    mBinding.tvProgress.text="下载进度----->$progress %，   ----->$readSize/$totalSize"
                    if (it.done){
                        mBinding.tvProgress.text="已完成"
                    }

                }
                is DownloadState.Success -> {
                    Log.e("createObserver: ","保存地址${it.path} ,文件大小${it.size} ")
                }
                is DownloadState.Error -> {
                    mBinding.tvProgress.text = "下载错误-->${it.errorMsg}"
                    Log.e("createObserver: ", "下载错误-->${it.errorMsg}")
                }
            }
        }
    }

    override fun initVariableId()=BR._all
}