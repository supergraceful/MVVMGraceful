package me.magical.graceful.mvvm.viewModel

import dagger.hilt.android.lifecycle.HiltViewModel
import me.magical.graceful.mvvm.model.NewsModel
import me.magical.graceful.request.bean.NewsBean
import me.magical.mvvmgraceful.base.BaseViewModel
import me.magical.mvvmgraceful.helper.uiRequest
import me.magical.mvvmgraceful.livedata.UnFlowLiveData
import me.magical.mvvmgraceful.request.core.DataState
import javax.inject.Inject

@HiltViewModel
class NewsVM @Inject constructor() : BaseViewModel() {
    @Inject
    lateinit var newModel: NewsModel

    @Inject
    lateinit var resultState: UnFlowLiveData<DataState<NewsBean>>

    fun currentNews() {
        uiRequest(resultState, {
            newModel.getNews()
        })
    }
}