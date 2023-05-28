package ru.netology.nmedia

import android.annotation.SuppressLint
import android.icu.text.DecimalFormat
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow


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
            binding.RepostIb.setOnClickListener{
                    post.reposted++
                NumberOfRepostsTv.text= prettyCount(post.reposted)
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
    val suffix = charArrayOf(' ', 'K', 'M', 'B', 'T', 'P', 'E')
    val numValue = numb.toLong()
    val value = floor(log10(numValue.toDouble())).toInt()
    val base = value / 3


    return if (value >= 3 && base < suffix.size && (numValue.toInt() % 1000 != 0)) {
        DecimalFormat("#.#").format(
            numValue / 10.0.pow((base * 3).toDouble())
        ) + suffix[base]
    } else if (value >= 3 && base < suffix.size && (numValue.toInt() % 1000 == 0) && numb <= 1000000) {

        DecimalFormat("#").format(
            numValue / 10.0.pow((base * 3).toDouble())
        ) + suffix[base]

    } else {
        DecimalFormat("#,##").format(numValue)
    }
}