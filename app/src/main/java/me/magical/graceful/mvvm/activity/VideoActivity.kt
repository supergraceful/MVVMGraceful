package me.magical.graceful.mvvm.activity

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import me.magical.graceful.BR
import me.magical.graceful.R
import me.magical.graceful.databinding.ActivityVideoBinding
import me.magical.graceful.mvvm.adapter.VideoPlayAdapter
import me.magical.graceful.mvvm.fragment.ExploreFragment
import me.magical.graceful.mvvm.viewModel.VideoVM
import me.magical.graceful.request.bean.Parameter
import me.magical.mvvmgraceful.base.activity.BaseAC
import me.magical.mvvmgraceful.base.activity.BaseMVVMAC
import javax.inject.Inject

@AndroidEntryPoint
class VideoActivity : BaseMVVMAC<ActivityVideoBinding, VideoVM>() {

    @Inject
    lateinit var adapter: VideoPlayAdapter

    private val dataList = ArrayList<Parameter>()

    private var page = 0

    override fun getLayout(): Int = R.layout.activity_video

    override fun initVariableId(): Int = BR.videoVM

    override fun createObserver() {
        mViewModel.dataList.observe(this) {
            dataList.addAll(it.list)
            adapter.addData(dataList)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
//        mBinding.rvHomeList.layoutManager=LinearLayoutManager(mActivity)
        //设置viewpage滑动方向
        mBinding.rvVideoList.orientation = ViewPager2.ORIENTATION_VERTICAL
        //设置view保留数量
        mBinding.rvVideoList.offscreenPageLimit = 1
        mBinding.rvVideoList.adapter = adapter
        mBinding.rvVideoList.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == dataList.size - 1) {
                    mViewModel.getVideoList(++page)
                }
            }
        })
        mViewModel.getVideoList()

        mBinding.ivVideoHandoff.setOnClickListener {
            when (mViewModel.type) {
                "mini" -> {
                    mViewModel.type = "short"
                }
                "short" -> {
                    mViewModel.type = "mini"
                }
            }
            dataList.clear()
            page = 0
            adapter.reset()
            mViewModel.getVideoList()
        }
    }
}