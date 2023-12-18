package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: Long,
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    var authorAvatar: String? = "",
    val hidden:Boolean,
    var attachment: Attachment? = null,
//    val reposted: Int = 0,
//    val view: Int = 0,
//    val video:String= "",

) {
}

data class Attachment(
    val url: String,
    val description: String,
    val type: String
)
