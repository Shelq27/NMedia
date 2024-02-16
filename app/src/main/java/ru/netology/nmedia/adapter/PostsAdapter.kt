package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.AndroidUtils.loadImg
import ru.netology.nmedia.util.AndroidUtils.loadImgAttachment

interface OnInteractionListener {
    fun onLike(post: Post)
    fun onRepost(post: Post)
    fun onEdit(post: Post)
    fun onRemove(post: Post)
    fun onPlay(post: Post)
    fun onOpen(post: Post)
    fun onOpenFullScreen(post: Post)

}

class PostsAdapter(
    private val onInteractionListener: OnInteractionListener
) : PagingDataAdapter<Post, PostViewHolder>(PostDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position) ?: return
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding, private val onInteraсtionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {

        binding.apply {

//            videoGroup.visibility = if (post.video.isNotEmpty()) {
//                View.VISIBLE
//            } else View.GONE


            AvatarIv.loadImg("http://10.0.2.2:9999/avatars/${post.authorAvatar}")
            if (post.attachment != null) {
                AttachmentIv.loadImgAttachment("http://10.0.2.2:9999/media/${post.attachment.url}")
                AttachmentIv.visibility = View.VISIBLE
            } else AttachmentIv.visibility = View.GONE
            AuthorTv.text = post.author
            PublishedTv.text = post.published.toString()
            ContentTv.text = post.content
            LikeIB.isChecked = post.likedByMe
            LikeIB.text = AndroidUtils.prettyCount(post.likes)
//            RepostIb.text = prettyCount(post.reposted)
//            ViewsIv.text = prettyCount(post.view)
            LikeIB.setOnClickListener {
                onInteraсtionListener.onLike(post)
            }
            RepostIb.setOnClickListener {
                onInteraсtionListener.onRepost(post)
            }
            ContentTv.setOnClickListener {
                onInteraсtionListener.onOpen(post)
            }
            AttachmentIv.setOnClickListener{
                onInteraсtionListener.onOpenFullScreen(post)
            }
            VideoIb.setOnClickListener {
                onInteraсtionListener.onPlay(post)

            }
            VideoPlayIb.setOnClickListener {
                onInteraсtionListener.onPlay(post)
            }
            MenuIb.isVisible = post.ownedByMe
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






