package com.example.androidfundamental.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.androidfundamental.data.local.SettingPreferences
import com.example.androidfundamental.data.model.UserGithubResponse
import com.example.androidfundamental.data.remote.ApiConfig
import com.example.androidfundamental.utils.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


class MainViewModel(val pref: SettingPreferences) : ViewModel() {

    private val _resultUser = MutableLiveData<Result>()
    val resultUser: LiveData<Result> = _resultUser

    var user: List<UserGithubResponse.Items> = listOf()

    fun getThemeSetting() = pref.getThemeSetting().asLiveData()
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

    class Factory(private val pref: SettingPreferences) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MainViewModel(pref) as T
    }
}