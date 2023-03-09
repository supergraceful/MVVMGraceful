package me.magical.graceful.mvvm.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import me.magical.graceful.R
import me.magical.graceful.databinding.FragmentExploreBinding
import me.magical.graceful.mvvm.activity.DownloadActivity
import me.magical.graceful.mvvm.activity.NewsActivity
import me.magical.graceful.mvvm.activity.VideoActivity
import me.magical.graceful.mvvm.activity.WallPaperActivity
import me.magical.mvvmgraceful.base.fragment.BaseFM
import java.io.BufferedReader
import java.io.InputStreamReader

class ExploreFragment : BaseFM<FragmentExploreBinding>() , View.OnClickListener {

    override fun getLayout(savedInstanceState: Bundle?): Int = R.layout.fragment_explore

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
    override fun lazyLoadData() {

    }
    override fun initView(savedInstanceState: Bundle?) {
        mBinding.btMainDownload.setOnClickListener(this)
        mBinding.btMainRequest.setOnClickListener(this)
        mBinding.btMainVideo.setOnClickListener(this)
        mBinding.btMainImg.setOnClickListener(this)

        mBinding.btMainTest.setOnClickListener {
//            activity?.moveTaskToBack(true)
            Thread{
                execByRuntime("input keyevent 66")
            }.start()

        }
    }

    override fun onClick(v: View?) {
        var intent: Intent? = null

        when (v!!.id) {
            R.id.bt_main_download -> {
                intent = Intent(activity, DownloadActivity::class.java)
            }
            R.id.bt_main_request -> {
                intent = Intent(activity, NewsActivity::class.java)
            }
            R.id.bt_main_video -> {
                intent = Intent(activity, VideoActivity::class.java)
            }
            R.id.bt_main_img -> {
                intent = Intent(activity, WallPaperActivity::class.java)
            }

        }
        startActivity(intent)

    }


    fun execByRuntime(cmd: String?): String? {
        var process: Process? = null
        var bufferedReader: BufferedReader? = null
        var inputStreamReader: InputStreamReader? = null
        return try {
            process = Runtime.getRuntime().exec(cmd)
            inputStreamReader = InputStreamReader(process.inputStream)
            bufferedReader = BufferedReader(inputStreamReader)
            var read: Int
            val buffer = CharArray(4096)
            val output = StringBuilder()
            while (bufferedReader.read(buffer).also { read = it } > 0) {
                output.append(buffer, 0, read)
            }
            output.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            if (null != inputStreamReader) {
                try {
                    inputStreamReader.close()
                } catch (t: Throwable) {
                    //
                }
            }
            if (null != bufferedReader) {
                try {
                    bufferedReader.close()
                } catch (t: Throwable) {
                    //
                }
            }
            if (null != process) {
                try {
                    process.destroy()
                } catch (t: Throwable) {
                    //
                }
            }
        }
    }
}