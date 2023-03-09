package me.magical.graceful.mvvm.viewModel

import dagger.hilt.android.lifecycle.HiltViewModel
import me.magical.graceful.mvvm.model.HomeModel
import me.magical.graceful.request.bean.MinVideoBean
import me.magical.graceful.request.bean.TypeImageBean
import me.magical.mvvmgraceful.base.BaseViewModel
import me.magical.mvvmgraceful.helper.uiRequest
import me.magical.mvvmgraceful.livedata.UnFlowLiveData
import javax.inject.Inject
import javax.inject.Named


@HiltViewModel
class HomeVM:BaseViewModel {

//    val homeModel=HomeModel()

    @Inject
    constructor():super()

    @Inject
    lateinit var homeModel:HomeModel

    @Inject
    lateinit var dataList:UnFlowLiveData<TypeImageBean>

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