package me.magical.graceful.request.bean

data class TypeImageBean(
    val list: List<TypeImage>,
    val total: Int
)

data class TypeImage(
    val id: Int,
    val title: String,
    val type: String,
    val url: String
)