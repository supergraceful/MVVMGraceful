package me.magical.mvvmgraceful.ext.kv

import android.content.Context
import com.tencent.mmkv.MMKV
import me.magical.mvvmgraceful.ext.kv.KVStorage.Companion.DEFAULT_FILE_NAME

class MMKVStorage(context: Context, defaultFileName: String = DEFAULT_FILE_NAME): KVStorage<MMKV> {

    var mmkv:MMKV

    init {
        MMKV.initialize(context)
        mmkv=MMKV.mmkvWithID(defaultFileName)
    }
    override fun getKV():MMKV {
        return mmkv
    }

    override fun changeSaveFile(fileName: String) {
        mmkv = MMKV.mmkvWithID(fileName);
    }

    override fun put(key: String, value: Any): Boolean {
        return when(value){
            is Int-> mmkv.encode(key, value)
            is Float-> mmkv.encode(key, value)
            is Long-> mmkv.encode(key, value)
            is Double-> mmkv.encode(key, value)
            is String-> mmkv.encode(key, value)
            is Boolean-> mmkv.encode(key, value)
            is ByteArray->mmkv.encode(key, value)
            else->false
        }

    }
    override fun putSet(key: String, value: Set<String>): Boolean {
        return mmkv.encode(key, value)
    }

    override fun remove(key: String) {
       mmkv.remove(key)
    }

    override fun removes(keys: List<String>) {
        mmkv.removeValuesForKeys(keys.toTypedArray())
    }

    override fun <T> getValue(key: String, default: T): T? {
        return when(default){
            is Int->mmkv.decodeInt(key,default) as T
            is Long->mmkv.decodeLong(key,default) as T
            is Float->mmkv.decodeFloat(key,default) as T
            is Double->mmkv.decodeDouble(key,default) as T
            is String->mmkv.decodeString(key,default) as T
            is Boolean->mmkv.decodeBool(key,default) as T
            is ByteArray->mmkv.decodeBytes(key,default) as T
            else->null
        }
    }

    override fun getSet(key: String): Set<String>? {
        return mmkv.getStringSet(key,null)
    }

    override fun getAll(): MutableMap<String, *> =mmkv.all

}