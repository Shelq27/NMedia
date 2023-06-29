package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import ru.netology.nmedia.databinding.ActivityEditPostBinding


class EditPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.editPost.requestFocus()
        val postContent = intent.getStringExtra("content")
        binding.editPost.setText(postContent)
        this.onBackPressedDispatcher.addCallback(

            this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    setResult(RESULT_CANCELED, intent)
                    finish()
                }
            }
        )

        binding.okIB.setOnClickListener {
            val intent = Intent()
            if (binding.editPost.text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val editContent = binding.editPost.text.toString()
                intent.putExtra(Intent.EXTRA_TEXT, editContent)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        binding.closeIB.setOnClickListener {
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }


    }
}