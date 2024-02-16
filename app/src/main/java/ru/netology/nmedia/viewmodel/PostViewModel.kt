package ru.netology.nmedia.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.model.PhotoModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.File
import javax.inject.Inject

private val empty = Post(
    id = 0,
    content = "",
    author = "Shelq",
    authorId = 0,
    likes = 0,
    published = 0,
    likedByMe = false,
    hidden = true,
    authorAvatar = "netology.jpg"

)

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository,
    appAuth: AppAuth
) : ViewModel() {


    @OptIn(ExperimentalCoroutinesApi::class)
    val data: Flow<PagingData<Post>> = appAuth
        .authState
        .flatMapLatest { auth ->
            repository.data
                .map { posts ->
                        posts.map { it.copy(ownedByMe = auth.id == it.authorId) }

                }
        }
        .catch { it.printStackTrace() }.flowOn(Dispatchers.Default)


    private val _photo = MutableLiveData<PhotoModel?>(null)
    val photo: LiveData<PhotoModel?>
        get() = _photo


//    val newerCount: LiveData<Int> = data.switchMap {
//        repository.getNewerCount(it.posts.firstOrNull()?.id ?: 0L)
//            .catch { _dataState.postValue(FeedModelState(error = true)) }
//            .asLiveData(Dispatchers.Default, 100)
//    }
    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated
    var draft: String? = null

    init {
        loadPost()
    }

    fun setPhoto(uri: Uri, file: File) {
        _photo.value = PhotoModel(uri, file)
    }

    fun loadPost() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            repository.getAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun loadLocalDBPost() = viewModelScope.launch {
        repository.loadLocalDBPost()
    }

    fun refreshPosts() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(refreshing = true)
            repository.getAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun changeContentAndSave(content: String) {
        val text = content.trim()
        edited.value?.let {
            if (it.content == text) {
                return
            }
            edited.value = it.copy(content = text)
        }
        viewModelScope.launch {
            try {
                val photoModel = _photo.value
                if (photoModel == null) {
                    edited.value?.let { repository.save(it) }
                } else {
                    edited.value?.let { repository.saveWithAttachment(it, photoModel) }
                }
                _dataState.value = FeedModelState()
                _postCreated.value = Unit
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
        edited.value = empty

    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun clearEdit() {
        edited.value = empty
    }


    fun likeByPost(post: Post) = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(error = false)
            repository.likeByPost(post)
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }


    fun removeById(id: Long) = viewModelScope.launch {
        try {
            repository.removeById(id)
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }


    fun repostById(id: Long) {
    }

    fun clearPhoto() {
        _photo.value = null
    }


}




