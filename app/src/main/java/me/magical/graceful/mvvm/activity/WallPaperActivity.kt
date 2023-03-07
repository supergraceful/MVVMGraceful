package me.magical.graceful.mvvm.activity

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.magical.graceful.BR
import me.magical.graceful.R
import me.magical.graceful.databinding.ActivityVideoBinding
import me.magical.graceful.databinding.ActivityWallpagerBinding
import me.magical.graceful.mvvm.adapter.HomeImageAdapter
import me.magical.graceful.mvvm.adapter.WallPagerAdapter
import me.magical.graceful.mvvm.viewModel.WallPaperVM
import me.magical.graceful.request.bean.TypeImage
import me.magical.graceful.request.bean.VerticalBean
import me.magical.mvvmgraceful.base.activity.BaseMVVMAC

class WallPaperActivity:BaseMVVMAC<ActivityWallpagerBinding,WallPaperVM>() {

    private val dataList = ArrayList<VerticalBean>()
    private var adapter: WallPagerAdapter? = null
    private var page=0

    override fun initVariableId(): Int=BR.wallPaperVM

    override fun getLayout(): Int = R.layout.activity_wallpager

    override fun initView(savedInstanceState: Bundle?) {
        adapter = WallPagerAdapter()
        mBinding.rvWallpagerImageList.layoutManager = GridLayoutManager(this, 2)
        mBinding.rvWallpagerImageList.adapter = adapter

        mBinding.rvWallpagerImageList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!mBinding.rvWallpagerImageList.canScrollVertically(1)) {
                    //滑动到底部
                    mViewModel.getImages(++page)
                }
                if (!mBinding.rvWallpagerImageList.canScrollVertically(-1)) {
                    //滑动到顶部
                }
            }
        })

        mViewModel.getImages()
    }
    override fun createObserver() {
        mViewModel.dataList.observe(this) {
            dataList.addAll(it)
            adapter?.addData(dataList)
        }
    }
}