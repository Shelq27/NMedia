package ru.netology.nmedia.activity

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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

        val text=arguments?.text
        binding.editPost.requestFocus()
        if (text!=null){
            binding.editPost.setText(text)
        }

        binding.okIB.setOnClickListener {
            val content = binding.editPost.text.toString()
            viewModel.changeContentAndSave(content)
            AndroidUtils.hideKeyboard(requireView())

        }
        binding.closeIB.setOnClickListener {
            viewModel.clearEdit()
            findNavController().navigateUp()
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    AlertDialog.Builder(requireActivity()).apply {
                        setTitle("Выход")
                        setMessage("Отменить редактировани?")
                        setPositiveButton("Да") { _, _ ->
                            viewModel.clearEdit()
                            AndroidUtils.hideKeyboard(binding.editPost)
                            findNavController().navigateUp()
                        }
                        setNegativeButton("Нет") { _, _ -> }
                        setCancelable(true)
                    }.create().show()
                }
            }
        )
        viewModel.postCreated.observe(viewLifecycleOwner) {
            viewModel.loadPost()
            findNavController().navigateUp()
        }
        return binding.root
    }
}