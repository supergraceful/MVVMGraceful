package me.magical.graceful.mvvm.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import me.magical.graceful.BR
import me.magical.graceful.R
import me.magical.graceful.databinding.FragmentHomeBinding
import me.magical.graceful.mvvm.adapter.HomeImageAdapter
import me.magical.graceful.mvvm.adapter.VideoPlayAdapter
import me.magical.graceful.mvvm.viewModel.HomeVM
import me.magical.graceful.request.bean.Parameter
import me.magical.graceful.request.bean.TypeImage
import me.magical.graceful.request.bean.TypeImageBean
import me.magical.mvvmgraceful.base.fragment.BaseFM
import me.magical.mvvmgraceful.base.fragment.BaseMVVMFM
import me.magical.mvvmgraceful.ext.GLog
import me.magical.mvvmgraceful.ext.GLog.isShow
import me.magical.mvvmgraceful.request.core.DataState
import me.magical.mvvmgraceful.utils.NetworkUtil.url
import okhttp3.internal.notifyAll

class HomeFragment : BaseMVVMFM<FragmentHomeBinding, HomeVM>() {

    private var adapter: HomeImageAdapter? = null
    private val dataList = ArrayList<TypeImage>()
    private var page = 0

    companion object {
        const val ARG_PARAM1 = "param1"
        const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param: Bundle? = null) =
            ExploreFragment().apply {
                arguments = param
            }
    }

    override fun getLayout(savedInstanceState: Bundle?): Int = R.layout.fragment_home

    override fun initVariableId(): Int = BR.homeVM

    override fun createObserver() {

        mViewModel.dataList.observe(this) {
            if (dataList.isEmpty()) {
                Glide.with(mActivity)
                    .load(it.list[0].url)
                    .into(mBinding.siHomeTop)
            }
            dataList.addAll(it.list)
            adapter?.addData(dataList)

        }
    }

    override fun lazyLoadData() {
        GLog.i("lazyLoadData 执行")

    }

    var scrollRange = -1
    override fun initView(savedInstanceState: Bundle?) {
        adapter = HomeImageAdapter()
        mBinding.rvHomeImageList.layoutManager = GridLayoutManager(mActivity, 2)
        mBinding.rvHomeImageList.adapter = adapter

        mBinding.rvHomeImageList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!mBinding.rvHomeImageList.canScrollVertically(1)) {
                    //滑动到底部
                    mViewModel.getImages(++page)
                }
                if (!mBinding.rvHomeImageList.canScrollVertically(-1)) {
                    //滑动到顶部
                }
            }
        })

        mBinding.ablHomeAppbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->


            if (scrollRange == -1) {
                scrollRange = appBarLayout.totalScrollRange
            }
            if (scrollRange + verticalOffset == 0) {
                mBinding.ctlHomeToolbar.title = "收缩"
            } else{
                mBinding.ctlHomeToolbar.title = ""

            }
        }
        mViewModel.getImages()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GLog.i("onViewCreated 执行")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        GLog.i("onCreateView 执行")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}