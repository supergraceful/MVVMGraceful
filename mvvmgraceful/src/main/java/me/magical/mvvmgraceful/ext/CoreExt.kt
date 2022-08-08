package me.magical.mvvmgraceful.ext

import java.lang.reflect.ParameterizedType

fun <T>getPracticalClass(clz:Class<T>):T{
    return (clz.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as T
}