package me.magical.mvvmgraceful.ext.kv

interface KVStorage<KV> {

    companion object{
        const val DEFAULT_FILE_NAME="android_kv_data"
    }
    /**
     * 获取当前存储对象
     */
    fun getKV():KV

    /**
     * 更改kv存储的文件
     */
    fun changeSaveFile(fileName:String)

    /**
     * 存储
     */
    fun put(key: String,value:Any):Boolean

    /**
     * set存储
     */
    fun putSet(key: String,value:Set<String>):Boolean

    /**
     * 删除某个存储对象
     */
    fun remove(key:String)
    /**
     * 批量删除对象
     */
    fun removes(keys:List<String>)
    /**
     * 获取值
     */
    fun <T>getValue(key:String,default:T):T?

    /**
     * 获取set存储
     */
    fun getSet(key:String):Set<String>?

    /**
     * 获取所有值
     */
    fun getAll():MutableMap<String,*>
}