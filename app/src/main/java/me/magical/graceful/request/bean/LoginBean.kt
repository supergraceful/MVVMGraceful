package me.magical.graceful.request.bean


data class LoginBean(
    val account: String,
    val createdAt: Any,
    val deletedAt: Any,
    val id: Int,
    val level: Int,
    val token: String,
    val updatedAt: Any
){
    override fun toString(): String {
        return "LoginBean(account='$account', createdAt=$createdAt, deletedAt=$deletedAt, id=$id, level=$level, token='$token', updatedAt=$updatedAt)"
    }
}
