package me.magical.graceful

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import me.magical.graceful.databinding.ActivityMainBinding
import me.magical.mvvmgraceful.base.activity.BaseActivity
import me.magical.mvvmgraceful.base.activity.BaseMVVMActivity
import me.magical.mvvmgraceful.ext.AppLauncher
import me.magical.mvvmgraceful.request.download.DownloadState
import me.magical.mvvmgraceful.request.download.FileTool

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getLayout() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.btMainDownload.setOnClickListener {
            startActivity(Intent(this, DownloadActivity::class.java))
        }

        mBinding.btMainGoto.setOnClickListener {
            val intent = Intent()

            val componentName = ComponentName(
                "com.github.ybq.android.spinkit",
                "com.github.ybq.android.loading.MainActivity"
            )
            val uri = Uri.parse("com.github.ybq.android.spinkit")
            intent.component = componentName
            intent.data = uri
            intent.putExtra("data","11111111111")
            startActivity(intent)
        }

    }


}