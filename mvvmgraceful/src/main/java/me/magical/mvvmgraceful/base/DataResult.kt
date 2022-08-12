package me.magical.mvvmgraceful.base

import java.lang.Exception

sealed class DataResult <out T>{
    data class OnSuccess <out T>(val data: T): DataResult<T>()
    data class OnError(val exception: Exception):DataResult<Nothing>()
}
