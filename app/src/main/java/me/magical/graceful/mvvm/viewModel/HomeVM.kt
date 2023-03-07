package me.magical.graceful.mvvm.viewModel

import me.magical.graceful.mvvm.model.HomeModel
import me.magical.graceful.request.bean.MinVideoBean
import me.magical.graceful.request.bean.TypeImageBean
import me.magical.mvvmgraceful.base.BaseViewModel
import me.magical.mvvmgraceful.helper.uiRequest
import me.magical.mvvmgraceful.livedata.UnFlowLiveData


class HomeVM:BaseViewModel() {


    val homeModel=HomeModel()

    val dataList= UnFlowLiveData<TypeImageBean>()

    fun getImages(page:Int=0,size:Int=10){
        uiRequest({
            homeModel.getImages(page,size)
        },{
            dataList.value=it
        },{
            it.printStackTrace()
            showToast(it.msg)
        })
    }
}