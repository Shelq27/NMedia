package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAllAsync(callback: RepositoryCallback<List<Post>>)
    fun likeByIdAsync(id:Long,callback: RepositoryCallback<Post>)
    fun unlikeByIdAsync(id: Long,callback: RepositoryCallback<Post>)
    fun removeByIdAsync(id:Long,callback: RepositoryCallback<Unit>)
    fun saveAsync(post:Post,callback: RepositoryCallback<Unit>)
//    fun repostById(id: Long)

    interface RepositoryCallback<T>{
        fun onSuccess(result : T)
        fun onError(e: Exception)

    }
}