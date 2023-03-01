package me.magical.graceful

import android.util.Log
import dalvik.system.DexClassLoader
import dalvik.system.PathClassLoader

class LogClassLoader(dexPath:String,parent:ClassLoader):PathClassLoader(dexPath, parent) {

    override fun loadClass(name: String): Class<*> {
        if (name.startsWith("android.")||name.startsWith("java.lang")) {
            return super.loadClass(name)
        }

        val start=System.currentTimeMillis()
        try {
            return super.loadClass(name)
        }finally {
            val cost=System.currentTimeMillis()-start
            val thread = Thread.currentThread().name
//            Log.e("loadClass", "load $name thread:$thread cost: $cost")
        }

    }
}