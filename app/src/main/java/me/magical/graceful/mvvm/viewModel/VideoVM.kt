package me.magical.graceful.mvvm.viewModel

import dagger.hilt.android.lifecycle.HiltViewModel
import me.magical.graceful.mvvm.model.VideoModel
import me.magical.graceful.request.bean.MinVideoBean
import me.magical.mvvmgraceful.base.BaseViewModel
import me.magical.mvvmgraceful.helper.uiRequest
import me.magical.mvvmgraceful.livedata.UnFlowLiveData
import javax.inject.Inject

@HiltViewModel
class VideoVM @Inject constructor():BaseViewModel(){

    @Inject
    lateinit var videoModel:VideoModel

    @Inject
    lateinit var dataList:UnFlowLiveData<MinVideoBean>

    var type:String="mini"

    fun getVideoList(page:Int=0,size:Int=10){
        uiRequest({
            when (type) {
                "mini" -> {
                    videoModel.getMiniVideo(page,size)
                }
                else -> {
                    videoModel.getShortVideo(page,size)
                }
//                else -> {
////                    videoModel.getHaokanVideo(page,size)
//                }
            }
        },{
            dataList.value=it
        },{
            it.printStackTrace()
            showToast(it.msg)
        })
    }
}