package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentEditPostBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel


class EditPostFragment : Fragment() {
    companion object {
        var Bundle.text by StringArg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentEditPostBinding.inflate(layoutInflater)
        val viewModel: PostViewModel by activityViewModels()

        val text=arguments?.getString("text")
        binding.editPost.requestFocus()
        if (text!=null){
            binding.editPost.text
        }

        binding.okIB.setOnClickListener {
            val content = binding.editPost.text.toString()
            viewModel.changeContent(content)
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }
        binding.closeIB.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }
}