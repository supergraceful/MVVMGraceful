package me.magical.graceful.request.bean

data class MinVideoBean(
    val list: List<Parameter>,
    val total: Int
){
    override fun toString(): String {
        return "VideoBean(list=$list, total=$total)"
    }
}

data class Parameter(
    val coverUrl: String,
    val alias: String,
    val picuser: String,
    val picurl: String,
    val duration: String,
    val id: Int,
    val sec: Int,
    val playUrl: String,
    val playurl: String?,
    val title: String,
    val userName: String,
    val userPic: String
){
    override fun toString(): String {
        return "Parameter(coverUrl='$coverUrl', alias='$alias', picuser='$picuser', picurl='$picurl', duration='$duration', id=$id, sec=$sec, playurl='$playUrl', title='$title', userName='$userName', userPic='$userPic')"
    }
}