package com.example.androidfundamental

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidfundamental.data.local.SettingPreferences
import com.example.androidfundamental.data.model.UserGithubResponse
import com.example.androidfundamental.databinding.ActivityMainBinding
import com.example.androidfundamental.ui.activity.DetailActivity
import com.example.androidfundamental.ui.activity.FavoriteActivity
import com.example.androidfundamental.ui.activity.SettingActivity
import com.example.androidfundamental.ui.adapter.ListUserAdapter
import com.example.androidfundamental.ui.viewmodel.MainViewModel
import com.example.androidfundamental.utils.Constant
import com.example.androidfundamental.utils.Result


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    private val adapter by lazy {
        ListUserAdapter { user ->
            Intent(this, DetailActivity::class.java).apply {
                putExtra(Constant.ITEM, user)
                startActivity(this)
            }
        }
    }

    private val viewModel by viewModels<MainViewModel> {
        MainViewModel.Factory(SettingPreferences(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.toolBar.inflateMenu(R.menu.main_menu)
        binding.toolBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.favorite -> {
                    Intent(this, FavoriteActivity::class.java).apply {
                        startActivity(this)
                    }
                    true
                }

                R.id.setting -> {
                    Intent(this, SettingActivity::class.java).apply {
                        startActivity(this)
                    }
                    true
                }

                else -> false
            }
        }

        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.setHasFixedSize(true)
        binding.rvUsers.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("WOILAH", query.toString())
                Toast.makeText(this@MainActivity, query.toString(), Toast.LENGTH_SHORT)
                viewModel.getUserGithub(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        viewModel.resultUser.observe(this) {
            when (it) {
                is Result.Success<*> -> {
                    adapter.setData(it.data as MutableList<UserGithubResponse.Items>)
                    viewModel.user = it.data
                }

                is Result.Error -> {
                    Toast.makeText(this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is Result.Loading -> {
                    binding.progressBar.isVisible = it.isLoading
                }

                else -> {
                    true
                }
            }
        }

        viewModel.getUserGithub()
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.user == null) {
            viewModel.getUserGithub()
        }

    }

}