package me.magical.mvvmgraceful.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import me.magical.mvvmgraceful.livedata.UnFlowLiveData

abstract class BaseViewModel : ViewModel() {

    companion object {
        const val CLASS = "CLASS"
        const val BUNDLE = "BUNDLE"
    }

    val mUIData = UIChangeLiveData()

    fun showToast(content: String?) {

        mUIData.showToastEvent.postValue(content)
    }

    fun showLoading(title: String?) {
        mUIData.showLoadingEvent.postValue(title)
    }

    fun dismissLoading() {
        mUIData.dismissLoadingEvent.call()
    }

    fun startActivity(clz: Class<*>, bundle: Bundle?) {
        val params = mutableMapOf<String, Any>()
        params[CLASS] = clz
        bundle?.also {
            params[BUNDLE] = bundle
        }
        mUIData.startActivityEvent.postValue(params)

    }

    fun finish() {
        mUIData.finishEvent.call()
    }

    fun onBackPressed() {
        mUIData.onBackPressedEvent.call()
    }


    inner class UIChangeLiveData {

        val showLoadingEvent by lazy {
            UnFlowLiveData<String>()
        }
        val showToastEvent by lazy {
            UnFlowLiveData<String>()
        }
        val dismissLoadingEvent by lazy {
            UnFlowLiveData<Void>()
        }
        val startActivityEvent by lazy {
            UnFlowLiveData<Map<String, Any>>()
        }

        //        val startContainerActivityEvent by lazy {
//            UnFlowLiveData<Map<String, Any>>()
//        }
        val finishEvent by lazy {
            UnFlowLiveData<Void>()
        }
        val onBackPressedEvent by lazy {
            UnFlowLiveData<Void>()
        }

    }
}