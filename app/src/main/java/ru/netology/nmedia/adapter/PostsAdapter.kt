package ru.netology.nmedia.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.log10

interface OnInteractionListener {
    fun onLike(post: Post)
    fun onRepost(post: Post)
    fun onEdit(post: Post)
    fun onRemove(post: Post)
    fun onPlay(post: Post)
}

class PostsAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}


class PostViewHolder(
    private val binding: CardPostBinding, private val onInteraсtionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {

            videoGroup.visibility = if (post.video != null) {
                View.VISIBLE
            } else View.GONE

            AuthorTv.text = post.author
            PublishedTv.text = post.published
            ContentTv.text = post.content

            LikeIb.isChecked = post.likedByMe
            LikeIb.text = prettyCount(post.likes)
            RepostIb.text = prettyCount(post.reposted)
            ViewsIv.text = prettyCount(post.view)
            LikeIb.setOnClickListener {
                onInteraсtionListener.onLike(post)
            }

            RepostIb.setOnClickListener {
                onInteraсtionListener.onRepost(post)
            }


            videoIB.setOnClickListener {
                onInteraсtionListener.onPlay(post)

            }
            videoPlayIB.setOnClickListener {
                onInteraсtionListener.onPlay(post)
            }
            MenuIb.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteraсtionListener.onRemove(post)
                                true
                            }

                            R.id.editPost -> {
                                onInteraсtionListener.onEdit(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}

fun prettyCount(numb: Int): String? {
    val value = floor(log10(numb.toDouble())).toInt()
    return when {
        value < 3 -> numb.toString() // < 1000
        value < 4 -> DecimalFormat("#.#").apply { roundingMode = RoundingMode.FLOOR }
            .format(numb.toDouble() / 1000) + "K" // < 10_000
        value < 6 -> DecimalFormat("#").apply { roundingMode = RoundingMode.FLOOR }
            .format(numb.toDouble() / 1000) + "K" // < 1_000_000
        else -> DecimalFormat("#.#").apply { roundingMode = RoundingMode.FLOOR }
            .format(numb.toDouble() / 1_000_000) + "M"
    }
}





