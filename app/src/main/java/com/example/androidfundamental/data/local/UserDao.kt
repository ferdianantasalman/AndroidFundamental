package com.example.androidfundamental.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.androidfundamental.data.model.UserGithubResponse

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserGithubResponse.Items)

    @Query("SELECT * FROM user ORDER BY id ASC")
    fun getAllUserFavorite(): LiveData<List<UserGithubResponse.Items>>

    @Query("SELECT * FROM user WHERE id LIKE :id LIMIT 1")
    fun getUserFavoriteById(id: Int): UserGithubResponse.Items

    @Delete
    suspend fun delete(user: UserGithubResponse.Items)
}