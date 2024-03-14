package com.example.androidfundamental.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidfundamental.data.remote.ApiConfig
import com.example.androidfundamental.utils.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    private val _user = MutableLiveData<Result>()
    val user: LiveData<Result> = _user

    private val _resultFollowersUser = MutableLiveData<Result>()
    val resultFollowersUser = _resultFollowersUser

    private val _resultFollowingUser = MutableLiveData<Result>()
    val resultFollowingUser = _resultFollowingUser

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