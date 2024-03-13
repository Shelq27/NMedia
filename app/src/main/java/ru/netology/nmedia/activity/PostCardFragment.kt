//package ru.netology.nmedia.activity
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.PopupMenu
//import androidx.core.view.isVisible
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.activityViewModels
//import androidx.navigation.fragment.findNavController
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.coroutines.flow.collectLatest
//import kotlinx.coroutines.flow.map
//import ru.netology.nmedia.R
//import ru.netology.nmedia.activity.EditPostFragment.Companion.text
//import ru.netology.nmedia.adapter.PostViewHolder
//import ru.netology.nmedia.databinding.FragmentCardPostBinding
//import ru.netology.nmedia.util.AndroidUtils
//import ru.netology.nmedia.util.AndroidUtils.loadImg
//import ru.netology.nmedia.util.AndroidUtils.loadImgAttachment
//import ru.netology.nmedia.util.AndroidUtils.prettyCount
//import ru.netology.nmedia.util.idArg
//import ru.netology.nmedia.viewmodel.PostViewModel
//@AndroidEntryPoint
//class PostCardFragment : Fragment() {
//    companion object {
//        var Bundle.id by idArg
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
//    ): View? {
//        val binding = FragmentCardPostBinding.inflate(inflater, container, false)
//        val viewModel: PostViewModel by activityViewModels()
//
//        val postId = arguments?.id ?: -1
//            with(binding.fragmentCardPost) {
//                AuthorTv.text = post.author
//                PublishedTv.text = post.published.toString()
//                ContentTv.text = post.content
//                LikeIB.isChecked = post.likedByMe
//                LikeIB.text = AndroidUtils.prettyCount(post.likes)
//                AvatarIv.loadImg("http://10.0.2.2:9999/avatars/${post.authorAvatar}")
//                if (post.attachment != null) {
//                    AttachmentIv.loadImgAttachment("http://10.0.2.2:9999/avatars/${post.attachment.url}")
//                    AttachmentIv.visibility = View.VISIBLE
//                } else AttachmentIv.visibility = View.GONE
//                RepostIb.text = prettyCount(post.reposted)
//                ViewsIv.text = prettyCount(post.view)
//
////                videoGroup.visibility = if (post.video.isNotEmpty()) {
////                    View.VISIBLE
////                } else View.GONE
//
//                LikeIB.setOnClickListener {
//                    viewModel.likeByPost(post)
//                }
//                RepostIb.setOnClickListener {
//                    viewModel.repostById(post.id)
//                    val intent = Intent().apply {
//                        action = Intent.ACTION_SEND
//                        putExtra(Intent.EXTRA_TEXT, post.content)
//                        type = "text/plain"
//                    }
//
//                    val shareIntent =
//                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
//                    startActivity(shareIntent)
//                }
//
////                videoIB.setOnClickListener {
////                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
////                    startActivity(intent)
////                }
////                videoPlayIB.setOnClickListener {
////                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
////                    startActivity(intent)
////                }
//                MenuIb.isVisible = post.ownedByMe
//                MenuIb.setOnClickListener {
//                    PopupMenu(it.context, it).apply {
//                        inflate(R.menu.options_post)
//                        setOnMenuItemClickListener { item ->
//                            when (item.itemId) {
//                                R.id.remove -> {
//                                    viewModel.removeById(post.id)
//                                    findNavController().navigateUp()
//                                    true
//                                }
//
//                                R.id.editPost -> {
//                                    findNavController().navigate(R.id.action_postCardFragment_to_editPostFragment,
//                                        Bundle().also { it.text = post.content })
//                                    viewModel.edit(post)
//                                    true
//                                }
//
//                                else -> false
//                            }
//                        }
//                    }.show()
//                }
//            }
//        }
//
//        return binding.root
//    }
//}