package me.magical.mvvmgraceful.ext

import java.lang.reflect.ParameterizedType

/**
 * 获取当前对象的的第几个泛型对象
 */
fun <T>getPracticalClass(obj:Any,count:Int):T{
    return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[count] as T
}

