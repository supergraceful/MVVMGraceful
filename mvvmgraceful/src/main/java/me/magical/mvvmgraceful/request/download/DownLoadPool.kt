package me.magical.mvvmgraceful.request.download

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import me.magical.mvvmgraceful.ext.kv.KVUtil
import java.util.concurrent.ConcurrentHashMap

object DownLoadPool {

    private val scopeMap = ConcurrentHashMap<String, CoroutineScope>()

    private val pathMap = ConcurrentHashMap<String, String>()

    private val listenerMap = ConcurrentHashMap<String, OnDownLoadListener>()

    fun addScope(tag: String, scpoe: CoroutineScope) {
        scopeMap[tag] = scpoe
    }

    fun addPath(tag: String, path: String) {
        pathMap[tag] = path
    }

    fun addListener(tag: String, listener: OnDownLoadListener) {
        listenerMap[tag] = listener
    }

    fun add(tag: String, job: CoroutineScope, path: String, listener: OnDownLoadListener) {
        scopeMap[tag] = job
        pathMap[tag] = path
        listenerMap[tag] = listener
    }

    fun getScope(tag: String) = scopeMap[tag]

    fun getPath(tag: String) = pathMap[tag]

    fun getListener(tag: String) = listenerMap[tag]

    fun getListenerMap() = listenerMap


    fun removeScope(tag: String) {
        scopeMap.remove(tag)
    }

    fun removePath(tag: String) {
        pathMap.remove(tag)
    }

    fun removeListener(tag: String) {
        listenerMap.remove(tag)
    }
    fun remove(tag: String) {
        pause(tag)
        KVUtil.remove(tag)
        scopeMap.remove(tag)
        pathMap.remove(tag)
        listenerMap.remove(tag)
    }

    fun pause(tag:String){
        val scpoe=scopeMap[tag]
        if (scpoe!=null&&scpoe.isActive){
            scpoe.cancel()
        }
    }

}