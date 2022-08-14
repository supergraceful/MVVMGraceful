package me.magical.mvvmgraceful.ext.kv

import android.content.Context
import android.content.SharedPreferences

class SPStorage(val context: Context, defaultFileName: String = KVStorage.DEFAULT_FILE_NAME) :
    KVStorage<SharedPreferences> {

    var sp: SharedPreferences

    init {
        sp = context.getSharedPreferences(defaultFileName, Context.MODE_PRIVATE)
    }

    override fun getKV(): SharedPreferences {
        return sp
    }

    override fun changeSaveFile(fileName: String) {
        sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    }

    override fun put(key: String, value: Any): Boolean {
        val editor = sp.edit()
        return when (value) {
            is Boolean -> {
                editor.putBoolean(key, value)
                editor.apply()
                true
            }
            is Int->{
                editor.putInt(key, value)
                editor.apply()
                true
            }
            is Long->{
                editor.putLong(key, value)
                editor.apply()
                true
            }

            is Float->{
                editor.putFloat(key, value)
                editor.apply()
                true
            }
            is String->{
                editor.putString(key, value)
                editor.apply()
                true
            }
            else -> false
        }
    }

    override fun putSet(key: String, value: Set<String>): Boolean {
        sp.edit().putStringSet(key,value).apply()
        return true
    }

    override fun remove(key: String) {
        sp.edit().remove(key).apply()
    }

    override fun removes(keys: List<String>) {
        keys.forEach {
            sp.edit().remove(it).apply()
        }
    }

    override fun <T> getValue(key: String, default: T): T? {
        return when(default){
            is Boolean -> {
                sp.getBoolean(key, default) as T
            }
            is Int->{
                sp.getInt(key, default) as T
            }
            is Long->{
                sp.getLong(key, default) as T
            }

            is Float->{
                sp.getFloat(key, default) as T
            }
            is String->{
                sp.getString(key, default) as T
            }
            else -> null
        }
    }

    override fun getSet(key: String): Set<String>? {
        return sp.getStringSet(key,null)
    }

    override fun getAll(): MutableMap<String, *> =sp.all
}