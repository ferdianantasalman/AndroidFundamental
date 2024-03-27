package com.example.androidfundamental.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.androidfundamental.data.model.UserGithubResponse
import com.example.androidfundamental.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class UserDaoTest {

    private lateinit var database: UserDatabase
    private lateinit var dao: UserDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            UserDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.userDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertUser() = runBlocking {
        val userItem = UserGithubResponse.Items(
            "https://api.github.com/users/sidiqpermana/following{/other_user}",
            "sidiqpermana",
            "https://api.github.com/users/sidiqpermana/followers",
            "User",
            "https://api.github.com/users/sidiqpermana",
            "https://avatars.githubusercontent.com/u/4090245?v=4",
            "https://github.com/sidiqpermana",
            id = 1
        )
        dao.insert(userItem)
    }

    @Test
    fun deleteUser() = runBlocking {
        val userItem = UserGithubResponse.Items(
            "https://api.github.com/users/sidiqpermana/following{/other_user}",
            "sidiqpermana",
            "https://api.github.com/users/sidiqpermana/followers",
            "User",
            "https://api.github.com/users/sidiqpermana",
            "https://avatars.githubusercontent.com/u/4090245?v=4",
            "https://github.com/sidiqpermana",
            id = 1
        )
        dao.insert(userItem)
        dao.delete(userItem)
    }
}