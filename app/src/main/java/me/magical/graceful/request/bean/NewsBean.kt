package me.magical.graceful.request.bean

data class NewsBean(var from: String?,var name:String?){
    override fun toString(): String {
        return "NewsBean(from=$from, name=$name)"
    }
}