package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val authorId: Long,
    val author: String,
    val content: String,
    val published: Long,
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    var authorAvatar: String? = "",
    val hidden:Boolean,
    val attachment: Attachment? = null,
    val ownedByMe:Boolean = false ,
//    val reposted: Int = 0,
//    val view: Int = 0,
//    val video:String= "",

) {
}

data class Attachment(
    val url: String,
    val type: AttachmentType
)
