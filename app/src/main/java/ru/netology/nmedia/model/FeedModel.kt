package ru.netology.nmedia.model

import ru.netology.nmedia.dto.Post

data class FeedModel(
    val posts: List<Post> = emptyList(),
    val error: Boolean = false,
    val loading: Boolean = false,
    val refreshing: Boolean = false,
    val empty: Boolean = false,
    val errorText: String = ""
)