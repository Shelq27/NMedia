package ru.netology.nmedia.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.log10



@SuppressLint("StaticFieldLeak")
private var _binding: ActivityMainBinding? = null

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel by viewModels<PostViewModel>()
        viewModel.data.observe(this){post->
            with(binding) {
                AuthorTv.text = post.author
                PublishedTv.text = post.published
                ContentTv.text = post.content
                NumberOfLikesTv.text = prettyCount(post.likes)
                NumberOfRepostsTv.text = prettyCount(post.reposted)
                statusLike(post, binding)
            }
        }
        binding.LikeIb.setOnClickListener {
            viewModel.like()
        }
        binding.RepostIb.setOnClickListener {
            viewModel.repost()
        }

    }
}

fun statusLike(post: Post, binding: ActivityMainBinding): Boolean {
    return if (post.likedByMe) {
        binding.LikeIb.setImageResource(R.drawable.ic_liked)
        true
    } else {
        binding.LikeIb.setImageResource(R.drawable.ic_like)
        false
    }
}
fun prettyCount(numb : Int): String? {
    val value = floor(log10(numb.toDouble())).toInt()
    return when {
        value < 3 -> numb.toString() // < 1000
        value < 4 -> DecimalFormat("#.#").apply { roundingMode=RoundingMode.FLOOR }.format(numb.toDouble() / 1000) + "K" // < 10_000
        value < 6 -> DecimalFormat("#").apply { roundingMode=RoundingMode.FLOOR }.format(numb.toDouble() / 1000) + "K" // < 1_000_000
        else -> DecimalFormat("#.#").apply { roundingMode=RoundingMode.FLOOR }.format(numb.toDouble() / 1_000_000) + "M"
    }
}


