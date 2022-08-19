package me.magical.graceful

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import me.magical.graceful.databinding.ActivityMainBinding
import me.magical.graceful.news.NewsActivity
import me.magical.mvvmgraceful.base.activity.BaseActivity


class MainActivity : BaseActivity<ActivityMainBinding>(), View.OnClickListener {

    override fun getLayout() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.btMainDownload.setOnClickListener(this)
        mBinding.btMainRequest.setOnClickListener (this)
    }

    override fun onClick(v: View?) {
        var intent: Intent? = null

        when (v!!.id) {
            R.id.bt_main_download -> {
                intent = Intent(this, DownloadActivity::class.java)
            }
            R.id.bt_main_request -> {
                intent = Intent(this, NewsActivity::class.java)
            }
        }
        startActivity(intent)
    }


}