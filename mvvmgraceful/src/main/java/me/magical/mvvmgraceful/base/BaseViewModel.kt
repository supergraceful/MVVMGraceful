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

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {

    companion object {
        const val CLASS = "CLASS"
        const val BUNDLE = "BUNDLE"
    }

    //    val mUIData: UIChangeLiveData = UIChangeLiveData()
    @Inject
    lateinit var mUIData: UIChangeLiveData

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

    class UIChangeLiveData @Inject constructor() {
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
    }
}