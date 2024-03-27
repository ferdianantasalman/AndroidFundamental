package com.example.androidfundamental.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidfundamental.data.model.UserGithubResponse
import com.example.androidfundamental.data.remote.ApiConfig
import com.example.androidfundamental.data.repositories.UserRepository
import com.example.androidfundamental.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel(private val application: Application) : ViewModel() {

    private val userRepository: UserRepository = UserRepository(application)

    private val _user = MutableLiveData<Result>()
    val user: LiveData<Result> = _user

    private val _resultFollowersUser = MutableLiveData<Result>()
    val resultFollowersUser = _resultFollowersUser

    private val _resultFollowingUser = MutableLiveData<Result>()
    val resultFollowingUser = _resultFollowingUser

    private val _resultUserFavoriteInsert = MutableLiveData<Boolean>()
    val resultUserFavoriteInsert = _resultUserFavoriteInsert

    private val _resultUserFavoriteDelete = MutableLiveData<Boolean>()
    val resultUserFavoriteDelete = _resultUserFavoriteDelete

    private var _isFavorite = false

    fun setUserFavorite(user: UserGithubResponse.Items) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                user.let {
                    if (_isFavorite) {
                        userRepository.delete(user)
                        _resultUserFavoriteDelete.postValue(true)
                    } else {
                        userRepository.insert(user)
                        _resultUserFavoriteInsert.postValue(true)
                    }
                    _isFavorite = !_isFavorite
                }
            }
        }
    }

    fun getUserFavoriteById(id: Int, listenFavorite: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val user = userRepository.getUserFavoriteById(id)

                if (user != null) {
                    listenFavorite()
                    _isFavorite = true
                }
            }
        }
    }

    fun getDetailUserGithub(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiConfig
                    .getApiService()
                    .getDetailUserGithub(username)
                emit(response)
            }.onStart {
                _user.value = Result.Loading(true)
            }.onCompletion {
                _user.value = Result.Loading(false)
            }.catch {
                it.printStackTrace()
                _user.value = Result.Error(it)
            }.collect {
                _user.value = Result.Success(it)
            }
        }
    }

    fun getFollowers(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiConfig
                    .getApiService()
                    .getFollowersUserGithub(username)

                emit(response)
            }.onStart {
                if (_resultFollowersUser.value != null) {
                    Result.Loading(false)
                } else {
                    _resultFollowersUser.value = Result.Loading(true)
                }
            }.onCompletion {
                _resultFollowersUser.value = Result.Loading(false)
            }.catch {
                it.printStackTrace()
                _resultFollowersUser.value = Result.Error(it)
            }.collect {
                _resultFollowersUser.value = Result.Success(it)
            }
        }
    }

    fun getFollowing(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiConfig
                    .getApiService()
                    .getFollowingUserGithub(username)

                emit(response)
            }.onStart {
                if (_resultFollowingUser.value != null) {
                    Result.Loading(false)
                } else {
                    _resultFollowingUser.value = Result.Loading(true)
                }
            }.onCompletion {
                _resultFollowingUser.value = Result.Loading(false)
            }.catch {
                it.printStackTrace()
                _resultFollowingUser.value = Result.Error(it)
            }.collect {
                _resultFollowingUser.value = Result.Success(it)
            }
        }
    }
}