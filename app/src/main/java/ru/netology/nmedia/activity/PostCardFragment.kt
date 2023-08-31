package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.EditPostFragment.Companion.text
import ru.netology.nmedia.adapter.prettyCount
import ru.netology.nmedia.databinding.FragmentCardPostBinding
import ru.netology.nmedia.util.idArg
import ru.netology.nmedia.viewmodel.PostViewModel

class PostCardFragment : Fragment() {
    companion object {
        var Bundle.id by idArg
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCardPostBinding.inflate(inflater, container, false)
        val viewModel: PostViewModel by activityViewModels()


        val postId = arguments?.id ?: -1
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == postId } ?: return@observe
            with(binding.fragmentCardPost) {
                AuthorTv.text = post.author
                PublishedTv.text = post.published
                ContentTv.text = post.content
                LikeIb.isChecked = post.likedByMe
                LikeIb.text = prettyCount(post.likes)
                RepostIb.text = prettyCount(post.reposted)
                ViewsIv.text = prettyCount(post.view)
                videoGroup.visibility = if (post.video.isNotEmpty()) {
                    View.VISIBLE
                } else View.GONE

                LikeIb.setOnClickListener {
                    viewModel.likeById(post.id)
                }
                RepostIb.setOnClickListener {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }

                    val shareIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(shareIntent)
                }

                videoIB.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                    startActivity(intent)
                }
                videoPlayIB.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                    startActivity(intent)
                }
                MenuIb.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.options_post)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    viewModel.removeById(post.id)
                                    findNavController().navigateUp()
                                    true
                                }

                                R.id.editPost -> {
                                    findNavController().navigate(R.id.action_postCardFragment_to_editPostFragment,
                                        Bundle().also { it.text = post.content })
                                    viewModel.edit(post)
                                    true
                                }

                                else -> false
                            }
                        }
                    }.show()
                }
            }
        }

        return binding.root
    }
}