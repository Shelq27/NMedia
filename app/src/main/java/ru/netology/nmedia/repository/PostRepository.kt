package ru.netology.nmedia.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.auth.AuthState
import ru.netology.nmedia.dto.FeedItem
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.PhotoModel

interface PostRepository {
    val data: Flow<PagingData<FeedItem>>
    fun getNewerCount(id: Long): Flow<Int>
    suspend fun loadLocalDBPost()
    suspend fun getAll()
    suspend fun updateUser(login:String, password:String): AuthState
    suspend fun likeByPost(post: Post)
    suspend fun removeById(id: Long)
    suspend fun save(post: Post)
    suspend fun saveWithAttachment(post: Post, photoModel: PhotoModel)
}