package com.example.androidfundamental.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidfundamental.data.model.UserGithubResponse
import com.example.androidfundamental.databinding.FragmentFollowBinding
import com.example.androidfundamental.ui.activity.DetailActivity
import com.example.androidfundamental.ui.adapter.ListUserAdapter
import com.example.androidfundamental.ui.viewmodel.DetailViewModel
import com.example.androidfundamental.utils.Constant
import com.example.androidfundamental.utils.Result

class FollowFragment : Fragment() {

    private var binding: FragmentFollowBinding? = null
    private val adapter by lazy {
        ListUserAdapter { user ->
            Intent(requireActivity(), DetailActivity::class.java).apply {
                putExtra(Constant.ITEM, user.login)
                startActivity(this)
            }
        }
    }
    private val viewModel by activityViewModels<DetailViewModel>()
    var type = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvFollow?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = this@FollowFragment.adapter
        }

        when (type) {
            FOLLOWERS -> {
                viewModel.resultFollowersUser.observe(viewLifecycleOwner, this::manageResultFollows)
            }

            FOLLOWING -> {
                viewModel.resultFollowingUser.observe(viewLifecycleOwner, this::manageResultFollows)
            }
        }
    }

    private fun manageResultFollows(state: Result) {
        when (state) {
            is Result.Success<*> -> {
                adapter.setData(state.data as MutableList<UserGithubResponse.Items>)
            }

            is Result.Error -> {
                Toast.makeText(
                    requireActivity(),
                    state.exception.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }

            is Result.Loading -> {
                binding?.progressBar?.isVisible = state.isLoading
            }
        }
    }

    //
    companion object {
        const val FOLLOWING = 100
        const val FOLLOWERS = 101

        fun newInstance(type: Int) = FollowFragment()
            .apply {
                this.type = type
            }
    }
}