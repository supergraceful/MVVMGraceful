package me.magical.mvvmgraceful.livedata

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import me.magical.mvvmgraceful.ext.GLog
import java.util.concurrent.atomic.AtomicBoolean


/**
 * 解决数据倒灌,但是只能有一个观察者接收到数据
 */
open class SingleLiveData<T>: MutableLiveData<T>() {

    private val mPending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            //如果此LiveData具有活动的观察者，则返回true。
            GLog.w("SingleLiveData", "多个观察者存在的时候，只会有一个被通知到数据更新")
        }
        super.observe(owner, { t->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    override fun setValue(value: T?) {
        mPending.set(true)
        super.setValue(value)
    }

    override fun postValue(value: T?) {
        mPending.set(true)
        super.postValue(value)
    }

    @MainThread
    fun call() {
        value=null
    }
}