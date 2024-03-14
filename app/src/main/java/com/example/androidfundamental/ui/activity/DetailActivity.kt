package com.example.androidfundamental.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.androidfundamental.R
import com.example.androidfundamental.data.model.DetailUserResponse
import com.example.androidfundamental.databinding.ActivityDetailBinding
import com.example.androidfundamental.databinding.ActivityMainBinding
import com.example.androidfundamental.ui.adapter.DetailViewPagerAdapter
import com.example.androidfundamental.ui.fragment.FollowFragment
import com.example.androidfundamental.ui.viewmodel.DetailViewModel
import com.example.androidfundamental.utils.Constant
import com.example.androidfundamental.utils.Result
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(Constant.ITEM) ?: "ferdi"

        val appBar = supportActionBar
        appBar!!.title = username
        appBar!!.setDisplayHomeAsUpEnabled(true)

        viewModel.getDetailUserGithub(username)

        viewModel.user.observe(this) {
            when (it) {
                is Result.Success<*> -> {
                    val user = it.data as DetailUserResponse

                    Glide.with(this)
                        .load(user.avatarUrl)
                        .circleCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(binding.ivUser);

                    binding.tvName.text = user.name
                    binding.tvLink.text = user.htmlUrl
                    binding.tvFollowing.text = user.following.toString()
                    binding.tvFollowers.text = user.followers.toString()
                }

                is Result.Error -> {
                    Toast.makeText(this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is Result.Loading -> {
                    binding.progressBar.isVisible = it.isLoading
                }
            }
        }

        val fragments = mutableListOf<Fragment>(
            FollowFragment.newInstance(FollowFragment.FOLLOWERS),
            FollowFragment.newInstance(FollowFragment.FOLLOWING)

        )
        val titleFragments = mutableListOf(
            getString(R.string.followers),
            getString(R.string.following),
        )
        val adapter = DetailViewPagerAdapter(this, fragments)
        binding.viewpager.adapter = adapter

        TabLayoutMediator(binding.tab, binding.viewpager) { tab, posisi ->
            tab.text = titleFragments[posisi]
        }.attach()

        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    viewModel.getFollowers(username)
                } else {
                    viewModel.getFollowing(username)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })


        viewModel.getFollowers(username)

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setMode(selectedMode: Int) {
        when (selectedMode) {
            R.id.share -> {
                val recipeShare = Intent()
                recipeShare.action = Intent.ACTION_SEND
                recipeShare.putExtra(
                    Intent.EXTRA_TEXT,
                    "${binding.tvLink.text}"
                )
                recipeShare.type = "text/plain"
                startActivity(Intent.createChooser(recipeShare, "Share To:"))
            }
        }
    }
}