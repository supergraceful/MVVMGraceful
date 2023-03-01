package me.magical.mvvmgraceful.base

import me.magical.mvvmgraceful.livedata.UnFlowLiveData
import javax.inject.Inject

class UIChangeLiveData {

        @Inject
        lateinit var showLoadingEvent: UnFlowLiveData<String>

        @Inject
        lateinit var showToastEvent: UnFlowLiveData<String>

        @Inject
        lateinit var dismissLoadingEvent: UnFlowLiveData<Void>

        @Inject
        lateinit var startActivityEvent: UnFlowLiveData<Map<String, Any>>

        @Inject
        lateinit var finishEvent: UnFlowLiveData<Void>

        @Inject
        lateinit var onBackPressedEvent: UnFlowLiveData<Void>


        //        val startContainerActivityEvent by lazy {
//            UnFlowLiveData<Map<String, Any>>()
//        }
    }