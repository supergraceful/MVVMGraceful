package me.magical.mvvmgraceful.livedata

import androidx.annotation.MainThread
import androidx.annotation.NonNull
import androidx.lifecycle.*
import me.magical.mvvmgraceful.ext.GLog
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 解决数据倒灌问题，同时支持多观察者
 */
class UnFlowLiveData<T> : MutableLiveData<T>() {
    private val mPendingMap = HashMap<Observer<in T>, AtomicBoolean>()

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val lifecycle = owner.lifecycle
        if (lifecycle.currentState == Lifecycle.State.DESTROYED) return

        mPendingMap[observer] = AtomicBoolean(false)

        lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                mPendingMap.remove(observer)
            }
        })

        super.observe(owner) {
            val pending = mPendingMap[observer]
            if (pending != null && pending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        }

    }

    override fun observeForever(observer: Observer<in T>) {
        mPendingMap[observer] = AtomicBoolean(false)
        super.observeForever(observer)
    }

    @MainThread
    override fun removeObserver(@NonNull observer: Observer<in T?>) {
        mPendingMap.remove(observer)
        super.removeObserver(observer)
    }

    @MainThread
    override fun removeObservers(@NonNull owner: LifecycleOwner) {
        mPendingMap.clear()
        super.removeObservers(owner)
    }

    override fun getValue(): T? {
        val value = super.getValue()
        if (value == null) {
            return when (value) {
                is Int -> 0 as T
                is Long -> 0L as T
                is Float -> 0f as T
                is Double -> 0.0 as T
                is Boolean -> false as T
                is String -> "" as T
                else -> null
            }
        }
        return value
    }

    @MainThread
    override fun setValue(t: T?) {
        for (value in mPendingMap.values) {
            value.set(true)
        }
        super.setValue(t)
    }

    override fun postValue(value: T?) {
        for (v in mPendingMap.values) {
            v.set(true)
        }
        super.postValue(value)
    }


    @MainThread
    fun call() {
        postValue(null)
    }
}