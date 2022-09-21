package me.magical.mvvmgraceful.request.core

interface ResponseImpl<T> {

    fun onStart()

    fun onSuccess(data:T)

    fun onError(exception: CustomException)

    fun onComplete()
}