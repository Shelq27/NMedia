package ru.netology.nmedia.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent

private val empty = Post(
    id = 0,
    content = "",
    author = "Shelq",
    likes = 0,
    published = 0,
    likedByMe = false,
    authorAvatar = "netology.jpg"

)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated
    var draft: String? = null
    val context = getApplication<Application>()

    init {
        loadPost()
    }

    fun loadPost() {

        _data.postValue(FeedModel(loading = true))
        repository.getAllAsync(object : PostRepository.RepositoryCallback<List<Post>> {
            override fun onSuccess(result: List<Post>) {
                _data.postValue(FeedModel(posts = result, empty = result.isEmpty()))

            }

            override fun onError(e: Exception) {

                _data.postValue(
                    FeedModel(
                        error = true, errorText = (context.resources.getString(R.string.load_error))
                    )
                )

            }
        })
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun clearEdit() {
        edited.value = empty
    }

    fun changeContentAndSave(content: String) {
        edited.value?.let {
            val text = content.trim()
            if (edited.value?.content != text) {
                repository.saveAsync(it.copy(content = text),
                    object : PostRepository.RepositoryCallback<Unit> {
                        override fun onSuccess(result: Unit) {
                            _postCreated.postValue(Unit)
                            loadPost()
                        }

                        override fun onError(e: Exception) {
                            _data.postValue(
                                FeedModel(
                                    error = true,
                                    errorText = context.resources.getString(R.string.save_error)
                                )
                            )
                        }
                    })
            }
        }
        edited.value = empty
    }

    fun likeById(id: Long) {
        val callBackLike = object : PostRepository.RepositoryCallback<Post> {
            override fun onSuccess(result: Post) {
                val posts = _data.value?.posts?.map { post ->
                    if (post.id == result.id) {
                        result
                    } else {
                        post
                    }
                }
                _data.postValue(posts?.let { _data.value?.copy(posts = it, empty = it.isEmpty()) })
            }

            override fun onError(e: Exception) {
                val post = _data.value?.posts?.find { it.id == id }
                if (post?.likedByMe == true) {
                    toastError(R.string.unlike_error)
                    _data.postValue(data.value?.posts?.let {
                        _data.value?.copy(
                            posts = it,
                            empty = it.isEmpty()
                        )
                    })


                } else {
                    toastError(R.string.like_error)
                    _data.postValue(data.value?.posts?.let {
                        _data.value?.copy(
                            posts = it,
                            empty = it.isEmpty()
                        )
                    })

                }
            }

        }
        val post = _data.value?.posts?.find { it.id == id }
        if (post?.likedByMe == true) {
            repository.unlikeByIdAsync(id, callBackLike)
        } else {
            repository.likeByIdAsync(id, callBackLike)
        }
        _postCreated.postValue(Unit)

    }


    fun removeById(id: Long) {
        val old = _data.value?.posts.orEmpty()
        _data.postValue(
            _data.value?.copy(posts = _data.value?.posts.orEmpty()
                .filter { it.id != id }
            )
        )
        repository.removeByIdAsync(id, object : PostRepository.RepositoryCallback<Unit> {
            override fun onSuccess(result: Unit) {
                _data.postValue(_data.value?.posts?.isEmpty()?.let { FeedModel(empty = it) })
            }

            override fun onError(e: Exception) {
                _data.postValue(_data.value?.copy(posts = old))

            }
        })
    }


    fun repostById(id: Long) {
    }

    fun toastError(textResources: Int) {
        Toast.makeText(context, getString(context, textResources), Toast.LENGTH_SHORT).show()
        loadPost()
    }
}




