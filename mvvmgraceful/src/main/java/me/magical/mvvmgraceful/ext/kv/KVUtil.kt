package me.magical.mvvmgraceful.ext.kv

class KVUtil private constructor(){

    companion object {

        val instance by lazy (mode = LazyThreadSafetyMode.SYNCHRONIZED){
            KVUtil()
        }
        private var kv:KVStorage<*>?=null

        fun getKV(): KVStorage<*>? {
            return kv
        }

        fun changeSaveFile(fileName: String) {
            kv?.changeSaveFile(fileName)
        }

        fun put(key: String, value: Any): Boolean {
           return  kv?.put(key, value)?:false
        }

        fun putSet(key: String, value: Set<String>): Boolean {
            return kv?.putSet(key, value)?:false
        }

        fun remove(key: String) {
            kv?.remove(key)
        }

        fun removes(keys: List<String>) {
            kv?.removes(keys)
        }

        fun <T> getValue(key: String, default: T): T? {
            return kv?.getValue(key,default)
        }

        fun getSet(key: String): Set<String>? {
            return kv?.getSet(key)
        }

        fun getAll(): MutableMap<String, *>? {
            return kv?.getAll()
        }
    }
   fun create(storage:KVStorage<*>){
        kv=storage
   }

}