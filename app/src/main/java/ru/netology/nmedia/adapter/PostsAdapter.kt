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
import ru.netology.nmedia.databinding.CardAdBinding
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Ad
import ru.netology.nmedia.dto.FeedItem
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.AndroidUtils.loadImg
import ru.netology.nmedia.util.AndroidUtils.loadImgCircle

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
) : PagingDataAdapter<FeedItem, RecyclerView.ViewHolder>(PostDiffCallback()) {
    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is Ad -> R.layout.card_ad
            is Post -> R.layout.card_post
            null -> error("unknown item type")
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.card_post -> {
                val binding =
                    CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PostViewHolder(binding, onInteractionListener)
            }

            R.layout.card_ad -> {
                val binding =
                    CardAdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                AdViewHolder(binding)
            }

            else -> error("unknown view type : $viewType")
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is Ad -> (holder as? AdViewHolder)?.bind(item)
            is Post -> (holder as? PostViewHolder)?.bind(item)
            null -> error("unknown item type")
        }
    }
}

class AdViewHolder(
    private val binding: CardAdBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(ad: Ad) {
        binding.imageAd.loadImg("http://10.0.2.2:9999/media/${ad.image}")
    }
}

class PostViewHolder(
    private val binding: CardPostBinding, private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {

        binding.apply {

//            videoGroup.visibility = if (post.video.isNotEmpty()) {
//                View.VISIBLE
//            } else View.GONE


            AvatarIv.loadImgCircle("http://10.0.2.2:9999/avatars/${post.authorAvatar}")
            if (post.attachment != null) {
                AttachmentIv.loadImg("http://10.0.2.2:9999/media/${post.attachment.url}")
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
                onInteractionListener.onLike(post)
            }
            RepostIb.setOnClickListener {
                onInteractionListener.onRepost(post)
            }
            ContentTv.setOnClickListener {
                onInteractionListener.onOpen(post)
            }
            AttachmentIv.setOnClickListener {
                onInteractionListener.onOpenFullScreen(post)
            }
            VideoIb.setOnClickListener {
                onInteractionListener.onPlay(post)

            }
            VideoPlayIb.setOnClickListener {
                onInteractionListener.onPlay(post)
            }
            MenuIb.isVisible = post.ownedByMe
            MenuIb.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }

                            R.id.editPost -> {
                                onInteractionListener.onEdit(post)
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

class PostDiffCallback : DiffUtil.ItemCallback<FeedItem>() {
    override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }

        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        return oldItem == newItem
    }
    

}






