package me.magical.graceful

import android.os.Bundle
import android.util.Log
import me.magical.graceful.databinding.ActivityMainBinding
import me.magical.graceful.databinding.DownloadActivityBinding
import me.magical.mvvmgraceful.base.activity.BaseMVVMActivity
import me.magical.mvvmgraceful.ext.GLog
import me.magical.mvvmgraceful.request.download.DownloadState
import me.magical.mvvmgraceful.request.download.FileTool

class DownloadActivity : BaseMVVMActivity<DownloadActivityBinding, DownloadViewModel>() {

    override fun getLayout() = R.layout.activity_main

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
        mViewModel.listener.observe(this) {
            when (it) {
                /**
                 * 下载过程
                 * progress：下载进度百分数, Int类型
                 * read：已经下载的文件大小byte数, Long类型
                 * count：文件总大小, Long类型
                 * done：是否下载完成，Boolean类型
                 */
                is DownloadState.Progress -> {
                    val progress = it.progress
                    val readSize = FileTool.bytes2kb(it.read)
                    val totalSize = FileTool.bytes2kb(it.count)

                    mBinding.tvProgress.text = "下载进度----->$progress %，   ----->$readSize/$totalSize"
                    if (it.done) {
                        mBinding.tvProgress.text = "已完成"
                    }

                }
                /**
                 * 下载成功
                 * path：保存地址的完整路径
                 * size：文件大小
                 */
                is DownloadState.Success -> {
                    GLog.e("createObserver: ", "保存地址${it.path} ,文件大小${it.size} ")
                }
                /**
                 * throwable:异常的throwable
                 */
                is DownloadState.Error -> {
                    mBinding.tvProgress.text = "下载错误-->${it.throwable.message}"
                    GLog.e("createObserver: ", "下载错误-->${it.throwable.message}")
                }
                /**
                 * 等待下载
                 */
                is DownloadState.Prepare -> {
                }

                /**
                 * 暂停下载
                 */
                is DownloadState.Pause -> {
                }
            }
        }
    }

    override fun initVariableId() = BR._all
}