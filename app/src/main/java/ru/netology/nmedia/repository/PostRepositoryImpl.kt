package ru.netology.nmedia.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.dto.Post

class PostRepositoryImpl : PostRepository {

    override fun getAllAsync(callback: PostRepository.RepositoryCallback<List<Post>>) {
        PostsApi.retrofitService.getAllAsync().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body() ?: throw RuntimeException("empty body"))
                } else {
                    callback.onError(RuntimeException("error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                callback.onError(Exception(t))
            }

        })
    }

    override fun likeByIdAsync(id: Long, callback: PostRepository.RepositoryCallback<Post>) {

        PostsApi.retrofitService.likeByIdAsync(id).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body() ?: throw RuntimeException("empty body"))
                } else {
                    callback.onError(RuntimeException("error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(Exception(t))
            }
        })

    }


    override fun unlikeByIdAsync(id: Long, callback: PostRepository.RepositoryCallback<Post>) {
        PostsApi.retrofitService.unlikeByIdAsync(id).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body() ?: throw RuntimeException("empty body"))
                } else {
                    callback.onError(RuntimeException("error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(Exception(t))
            }
        })
    }

    override fun removeByIdAsync(id: Long, callback: PostRepository.RepositoryCallback<Unit>) {
        PostsApi.retrofitService.removeByIdAsync(id).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    callback.onSuccess(Unit)
                } else {
                    callback.onError(RuntimeException("error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError(Exception(t))
            }

        })
    }


    override fun saveAsync(post: Post, callback: PostRepository.RepositoryCallback<Unit>) {
        PostsApi.retrofitService.saveAsync(post).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful) {
                    callback.onSuccess(Unit)
                } else {
                    callback.onError(RuntimeException("error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(Exception(t))
            }
        })
    }


}
