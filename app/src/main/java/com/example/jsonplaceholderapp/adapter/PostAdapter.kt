package com.example.jsonplaceholderapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jsonplaceholderapp.R
import com.example.jsonplaceholderapp.model.Post

class PostAdapter(
    private var posts: List<Post>,
    private val onPostClick: (Post) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private var filteredPosts: List<Post> = posts

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNumber: TextView = itemView.findViewById(R.id.tvNumber)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = filteredPosts[position]
        holder.tvNumber.text = "${position + 1}"
        holder.tvTitle.text = post.title

        holder.itemView.setOnClickListener {
            onPostClick(post)
        }
    }

    override fun getItemCount(): Int = filteredPosts.size

    fun updatePosts(newPosts: List<Post>) {
        posts = newPosts
        filteredPosts = newPosts
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        filteredPosts = if (query.isEmpty()) {
            posts
        } else {
            posts.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.body.contains(query, ignoreCase = true) ||
                        it.id.toString().contains(query)
            }
        }
        notifyDataSetChanged()
    }
}