package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likes: Int =1999,
    val reposted : Int = 0,
    val likedByMe: Boolean = false
)