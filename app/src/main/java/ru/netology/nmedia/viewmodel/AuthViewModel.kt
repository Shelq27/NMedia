package ru.netology.nmedia.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.netology.nmedia.auth.AppAuth
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    appAuth: AppAuth
) : ViewModel() {
    val data = appAuth.authState

    val authenticated: Boolean
        get() = data.value.id != 0L

}