package com.example.androidfundamental.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class UserGithubResponse(
    @field:SerializedName("total_count")
    val totalCount: Int,

    @field:SerializedName("incomplete_results")
    val incompleteResults: Boolean,

    @field:SerializedName("items")
    val items: MutableList<Items>
) {
    @Entity(tableName = "user")
    @Parcelize
    data class Items(

        @ColumnInfo(name = "following_url")
        @field:SerializedName("following_url")
        val followingUrl: String,

        @ColumnInfo(name = "login")
        @field:SerializedName("login")
        val login: String,

        @ColumnInfo(name = "followers_url")
        @field:SerializedName("followers_url")
        val followersUrl: String,

        @ColumnInfo(name = "type")
        @field:SerializedName("type")
        val type: String,

        @ColumnInfo(name = "url")
        @field:SerializedName("url")
        val url: String,

        @ColumnInfo(name = "avatar_url")
        @field:SerializedName("avatar_url")
        val avatarUrl: String,

        @ColumnInfo(name = "html_url")
        @field:SerializedName("html_url")
        val htmlUrl: String,

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        @field:SerializedName("id")
        val id: Int,
    ) : Parcelable
}
