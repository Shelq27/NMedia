package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likes: Int =1950,
    var reposted : Int = 0,
    var likedByMe: Boolean = false
)
