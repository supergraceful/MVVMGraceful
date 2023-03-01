package me.magical.graceful.mvvm.viewModel

import me.magical.graceful.mvvm.model.HomeModel
import me.magical.graceful.request.bean.VideoBean
import me.magical.mvvmgraceful.base.BaseViewModel
import me.magical.mvvmgraceful.base.fragment.BaseFM
import me.magical.mvvmgraceful.helper.uiRequest
import me.magical.mvvmgraceful.livedata.UnFlowLiveData
import me.magical.mvvmgraceful.request.core.DataState

class HomeVM:BaseViewModel() {

    val videoLiveData=UnFlowLiveData<DataState<VideoBean>>()

    val homeModel=HomeModel()

    fun getVideoList(){

        uiRequest(videoLiveData,{
            homeModel.getHaokanVideo()
        })
    }

}