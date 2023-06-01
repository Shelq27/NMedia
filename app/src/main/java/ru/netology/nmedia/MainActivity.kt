package ru.netology.nmedia

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
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

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false
        )
        with(binding) {
            AuthorTv.text = post.author
            PublishedTv.text = post.published
            ContentTv.text = post.content
            NumberOfLikesTv.text = prettyCount(post.likes)
            NumberOfRepostsTv.text = prettyCount(post.reposted)
            statusLike(post, binding)
            binding.LikeIb.setOnClickListener {

                post.likedByMe = !post.likedByMe
                if (statusLike(post, binding)) {
                    post.likes++
                } else {
                    post.likes--
                }
                NumberOfLikesTv.text = prettyCount(post.likes)
            }
            binding.RepostIb.setOnClickListener {
                post.reposted++
                NumberOfRepostsTv.text = prettyCount(post.reposted)
            }
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


