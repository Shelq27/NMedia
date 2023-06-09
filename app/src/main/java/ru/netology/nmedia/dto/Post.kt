package ru.netology.nmedia.dto

import android.provider.MediaStore.Video

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likes: Int = 0,
    val reposted: Int = 0,
    val view: Int = 0,
    val likedByMe: Boolean = false,
    val video:String?=null
)
