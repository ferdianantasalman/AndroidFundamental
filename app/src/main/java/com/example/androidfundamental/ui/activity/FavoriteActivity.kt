package com.example.androidfundamental.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidfundamental.data.model.UserGithubResponse
import com.example.androidfundamental.databinding.ActivityFavoriteBinding
import com.example.androidfundamental.ui.adapter.ListUserAdapter
import com.example.androidfundamental.ui.viewmodel.FavoriteViewModel
import com.example.androidfundamental.helper.ViewModelFactory
import com.example.androidfundamental.ui.viewmodel.DetailViewModel
import com.example.androidfundamental.utils.Constant
import com.example.androidfundamental.utils.Result

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var viewModel: FavoriteViewModel

    private val adapter by lazy {
        ListUserAdapter { user ->
            Intent(this, DetailActivity::class.java).apply {
                putExtra(Constant.ITEM, user)
                startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appBar = supportActionBar
        appBar!!.title = "Favorite User"
        appBar!!.setDisplayHomeAsUpEnabled(true)

        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.setHasFixedSize(true)
        binding.rvUsers.adapter = adapter

        viewModel = obtainViewModel(this@FavoriteActivity)

        viewModel.getAllUserFavorite().observe(this) {
            adapter.setData(it as MutableList<UserGithubResponse.Items>)
        }

//        viewModel.getAllUserFavorite {
//            adapter.setData()
//        }

//        viewModel.resultAllUserFavorite.observe(this) {
//            adapter.setData(it as MutableList<UserGithubResponse.Items>)
//        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}