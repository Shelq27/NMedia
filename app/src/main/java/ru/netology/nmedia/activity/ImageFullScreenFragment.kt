package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.EditPostFragment.Companion.text
import ru.netology.nmedia.databinding.FragmentImageFullScreenBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.AndroidUtils.loadImgAttachment
import ru.netology.nmedia.util.idArg
import ru.netology.nmedia.viewmodel.PostViewModel
@AndroidEntryPoint
class ImageFullScreenFragment : Fragment() {
    companion object {
        var Bundle.id by idArg
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentImageFullScreenBinding.inflate(inflater, container, false)
        val viewModel: PostViewModel by activityViewModels()
        val postId = arguments?.id ?: -1
        viewModel.data.observe(viewLifecycleOwner) { state ->
            val post = state.posts.find { it.id == postId } ?: return@observe
            with(binding) {
                backIB.setOnClickListener {

                    findNavController().navigateUp()
                }
                menuIB.setOnClickListener {
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
                                    findNavController().navigate(
                                        R.id.action_imageFullScreenFragment_to_editPostFragment,
                                        Bundle().also { it.text = post.content })
                                    viewModel.edit(post)
                                    true
                                }

                                else -> false
                            }
                        }
                    }.show()
                }
                imageFullScreen.loadImgAttachment("http://10.0.2.2:9999/media/${post.attachment?.url}")

                likeIB.setOnClickListener {
                    viewModel.likeByPost(post)
                }
                likeIB.isChecked = post.likedByMe
                likeIB.text = AndroidUtils.prettyCount(post.likes)

                shareIB.setOnClickListener {
                    viewModel.repostById(post.id)
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(shareIntent)
                }

            }

        }
        return binding.root
    }
}