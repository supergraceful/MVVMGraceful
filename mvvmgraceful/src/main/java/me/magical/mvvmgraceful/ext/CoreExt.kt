package me.magical.mvvmgraceful.ext

import java.lang.reflect.ParameterizedType

/**
 * 获取泛型的实际对象
 */
fun <T>getPracticalClass(obj:Any):T{
    return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as T
}