package ru.netology.nmedia.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.dto.Post

private const val BASE_URL = "${BuildConfig.BASE_URL}/api/slow/"


interface PostsApiService {
    @GET("posts")
    fun getAllAsync(): Call<List<Post>>

    @POST("posts/{id}/likes")
    fun likeByIdAsync(@Path("id") id: Long): Call<Post>

    @DELETE("posts/{id}/likes")
    fun unlikeByIdAsync(@Path("id") id: Long): Call<Post>

    @DELETE("posts/{id}")
    fun removeByIdAsync(@Path("id") id: Long): Call<Unit>

    @POST("posts")
    fun saveAsync(@Body post: Post): Call<Post>
}

val logger = HttpLoggingInterceptor().apply {
    if (BuildConfig.DEBUG) {
        level = HttpLoggingInterceptor.Level.BODY

    }
}
val client = OkHttpClient.Builder()
    .addInterceptor(logger)
    .build()
val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(client)
    .build()

object PostsApi {
    val retrofitService by lazy {
        retrofit.create(PostsApiService::class.java)
    }
}