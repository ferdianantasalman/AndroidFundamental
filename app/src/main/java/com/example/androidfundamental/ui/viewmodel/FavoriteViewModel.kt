package com.example.androidfundamental.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidfundamental.data.model.UserGithubResponse
import com.example.androidfundamental.data.repositories.UserRepository

class FavoriteViewModel(private val application: Application) : ViewModel() {

    private val userRepository: UserRepository = UserRepository(application)

    private val _resultAllUserFavorite = MutableLiveData<List<UserGithubResponse.Items>>()
    var resultAllUserFavorite: LiveData<List<UserGithubResponse.Items>> =
        _resultAllUserFavorite

    fun getAllUserFavorite(): LiveData<List<UserGithubResponse.Items>> =
        userRepository.getAllUserFavorite()
}