package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.nmedia.databinding.FragmentSignInBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.SignInViewModel

class SignInFragment : Fragment() {

    private val viewModel by viewModels<SignInViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentSignInBinding.inflate(layoutInflater)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            }
        )


        binding.signIn.setOnClickListener {
            AndroidUtils.hideKeyboard(requireView())
            viewModel.updateUser(
                binding.login.text.toString(),
                binding.password.text.toString()
            )
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.authentication.collectLatest {
                        if (it) findNavController().navigateUp()
                    }
                }
            }
            if (viewModel.errorMessage.value.isNotEmpty()) {
                Snackbar.make(
                    binding.root,
                    viewModel.errorMessage.value,
                    Snackbar.LENGTH_LONG
                )
                    .show()
            }

        }

        return binding.root
    }

}