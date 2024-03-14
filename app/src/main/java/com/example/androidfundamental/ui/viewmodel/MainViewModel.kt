package com.example.androidfundamental.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.androidfundamental.data.remote.ApiConfig
import com.example.androidfundamental.utils.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    private val _resultUser = MutableLiveData<Result>()
    val resultUser: LiveData<Result> = _resultUser

    private val _resultUserSearch = MutableLiveData<Result>()
    val resultUserSearch: LiveData<Result> = _resultUserSearch

    fun getUserGithub() {
        viewModelScope.launch {
            flow {
                val response = ApiConfig
                    .getApiService()
                    .getUserGithub()
                emit(response)
            }.onStart {
                _resultUser.value = Result.Loading(true)
            }.onCompletion {
                _resultUser.value = Result.Loading(false)
            }.catch {
                it.printStackTrace()
                _resultUser.value = Result.Error(it)
            }.collect {
                _resultUser.value = Result.Success(it)
                Log.d("JIRLAH", it.toString())

            }
        }
    }

    fun getUserGithub(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiConfig
                    .getApiService()
                    .searchUserGithub(
                        mapOf(
                            "q" to username,
//                            "per_page" to 10
                        )
                    )
                emit(response)
            }.onStart {
                _resultUser.value = Result.Loading(true)
            }.onCompletion {
                _resultUser.value = Result.Loading(false)
            }.catch {
                it.printStackTrace()
                _resultUser.value = Result.Error(it)
            }.collect {
                _resultUser.value = Result.Success(it.items)
                Log.d("JIRLAH", it.items.toString())
            }
        }
    }
}