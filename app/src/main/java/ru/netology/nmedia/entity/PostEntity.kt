package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String ,
    val content: String,
    val published: Long ,
    val likedByMe: Boolean,
    val likes: Int,
    val reposted: Int,
    val view: Int,
    val video: String,
) {
    fun toDto() = Post(id, author, content, published, likedByMe ,likes, reposted, view, video)

    companion object {
        fun fromDto(post: Post) = PostEntity(
            post.id,
            post.author,
            post.content,
            post.published,
            post.likedByMe,
            post.likes,
            post.reposted,
            post.view,
            post.video
        )
    }
}
