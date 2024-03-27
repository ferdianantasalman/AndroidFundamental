package com.example.androidfundamental.data.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.androidfundamental.data.local.UserDao
import com.example.androidfundamental.data.local.UserDatabase
import com.example.androidfundamental.data.model.UserGithubResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserRepository(application: Application) {
    private val userDao: UserDao

    init {
        val db = UserDatabase.getInstance(application)
        userDao = db.userDao()
    }

    fun getAllUserFavorite(): LiveData<List<UserGithubResponse.Items>> {
        return userDao.getAllUserFavorite()
    }

    fun insert(user: UserGithubResponse.Items) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                userDao.insert(user)
            }
        }
    }

    fun delete(user: UserGithubResponse.Items) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                userDao.delete(user)
            }
        }
    }

    fun getUserFavoriteById(id: Int): UserGithubResponse.Items = userDao.getUserFavoriteById(id)

}