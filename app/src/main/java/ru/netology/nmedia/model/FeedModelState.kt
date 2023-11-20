package ru.netology.nmedia.model

data class FeedModelState(
    val loading: Boolean = false,
    val error: Boolean = false,
    val errorText: String = "",
    val refreshing: Boolean = false
)
