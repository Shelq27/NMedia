package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryRoomImpl

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likes = 0,
    published = "",
    reposted = 0,
    likedByMe = false,
    video = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {


    private val repository: PostRepository =
        PostRepositoryRoomImpl(AppDb.getInstance(application).PostDao)

    val data = repository.getAll()

    val edited = MutableLiveData(empty)
    var draft: String? = null


    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun clearEdit() {
        edited.value = empty
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun likeById(id: Long) = repository.likeById(id)
    fun removeById(id: Long) = repository.removeById(id)
    fun repostById(id: Long) = repository.repostById(id)


}