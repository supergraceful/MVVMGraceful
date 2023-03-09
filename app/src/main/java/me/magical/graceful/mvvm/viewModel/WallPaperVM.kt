package me.magical.graceful.mvvm.viewModel

import dagger.hilt.android.lifecycle.HiltViewModel
import me.magical.graceful.mvvm.model.HomeModel
import me.magical.graceful.mvvm.model.WallPagerModel
import me.magical.graceful.request.bean.TypeImageBean
import me.magical.graceful.request.bean.VerticalBean
import me.magical.mvvmgraceful.base.BaseViewModel
import me.magical.mvvmgraceful.helper.uiRequest
import me.magical.mvvmgraceful.livedata.UnFlowLiveData
import javax.inject.Inject

@HiltViewModel
class WallPaperVM @Inject constructor() : BaseViewModel() {

    @Inject
    lateinit var wallPaperVM: WallPagerModel

    @Inject
    lateinit var dataList: UnFlowLiveData<List<VerticalBean>>

    fun getImages(page: Int = 0) {
        uiRequest({
            wallPaperVM.getWallPager(page)
        }, {
            dataList.value = it
        }, {
            it.printStackTrace()
            showToast(it.msg)
        })
    }
}