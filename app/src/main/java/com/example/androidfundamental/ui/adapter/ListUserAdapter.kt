package com.example.androidfundamental.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.androidfundamental.data.model.UserGithubResponse
import com.example.androidfundamental.databinding.ItemUserBinding

class ListUserAdapter(
    private val data: MutableList<UserGithubResponse.Items> = mutableListOf(),
    private val listener: (UserGithubResponse.Items) -> Unit
) : RecyclerView.Adapter<ListUserAdapter.UserViewHolder>() {

    fun setData(data: MutableList<UserGithubResponse.Items>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }


    class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserGithubResponse.Items) {
            Glide.with(itemView)
                .load(user.avatarUrl)
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imgItemPhoto);
            binding.tvItemName.text = user.login
            binding.tvItemLink.text = user.htmlUrl
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener(item)
        }
    }

}