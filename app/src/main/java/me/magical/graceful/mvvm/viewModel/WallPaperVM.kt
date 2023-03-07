package me.magical.graceful.mvvm.viewModel

import me.magical.graceful.mvvm.model.HomeModel
import me.magical.graceful.mvvm.model.WallPagerModel
import me.magical.graceful.request.bean.TypeImageBean
import me.magical.graceful.request.bean.VerticalBean
import me.magical.mvvmgraceful.base.BaseViewModel
import me.magical.mvvmgraceful.helper.uiRequest
import me.magical.mvvmgraceful.livedata.UnFlowLiveData

class WallPaperVM:BaseViewModel() {
    val wallPaperVM= WallPagerModel()

    val dataList= UnFlowLiveData<List<VerticalBean>>()

    fun getImages(page:Int=0){
        uiRequest({
            wallPaperVM.getWallPager(page)
        },{
            dataList.value=it
        },{
            it.printStackTrace()
            showToast(it.msg)
        })
    }
}