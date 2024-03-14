package ru.netology.nmedia.dto


sealed interface FeedItem {
    val id: Long
}

data class Post(
    override val id: Long,
    val authorId: Long,
    val author: String,
    val content: String,
    val published: Long,
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    var authorAvatar: String? = "",
    val hidden: Boolean,
    val attachment: Attachment? = null,
    val ownedByMe: Boolean = false,
//    val reposted: Int = 0,
//    val view: Int = 0,
//    val video:String= "",

) : FeedItem {
}

data class Attachment(
    val url: String,
    val type: AttachmentType
)

data class Ad(
    override val id: Long,
    val image: String
) : FeedItem
