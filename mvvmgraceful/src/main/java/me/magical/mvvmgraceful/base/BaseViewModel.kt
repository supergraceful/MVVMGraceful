package me.magical.mvvmgraceful.base

import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.flow
import me.magical.mvvmgraceful.livedata.UnFlowLiveData
import java.lang.annotation.Inherited
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlin.text.Typography.dagger


open class BaseViewModel : ViewModel(){

    companion object {
        const val CLASS = "CLASS"
        const val BUNDLE = "BUNDLE"
    }

    val mUIData: UIChangeLiveData=UIChangeLiveData()

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

    class UIChangeLiveData {


        val showLoadingEvent: UnFlowLiveData<String> by lazy {
            UnFlowLiveData()
        }
        val showToastEvent: UnFlowLiveData<String>by lazy {
            UnFlowLiveData()
        }
        val dismissLoadingEvent: UnFlowLiveData<Void>by lazy {
            UnFlowLiveData()
        }
        val startActivityEvent: UnFlowLiveData<Map<String, Any>>by lazy {
            UnFlowLiveData()
        }
        val finishEvent: UnFlowLiveData<Void>by lazy {
            UnFlowLiveData()
        }
        val onBackPressedEvent: UnFlowLiveData<Void>by lazy {
            UnFlowLiveData()
        }


        //        val startContainerActivityEvent by lazy {
//            UnFlowLiveData<Map<String, Any>>()
//        }
    }
}