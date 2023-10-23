package ru.netology.nmedia.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.log10

object AndroidUtils {

    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun ImageView.loadImg(url: String) {
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.ic_load_24dp)
            .error(R.drawable.ic_error_24dp)
            .timeout(30_0000)
            .circleCrop()
            .into(this)
    }

    fun ImageView.loadAttachment(url: String) {
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.ic_load_24dp)
            .error(R.drawable.ic_error_24dp)
            .timeout(30_0000)
            .into(this)
    }

    fun prettyCount(numb: Int): String? {
        val value = floor(log10(numb.toDouble())).toInt()
        return when {
            value < 3 -> numb.toString() // < 1000
            value < 4 -> DecimalFormat("#.#").apply { roundingMode = RoundingMode.FLOOR }
                .format(numb.toDouble() / 1000) + "K" // < 10_000
            value < 6 -> DecimalFormat("#").apply { roundingMode = RoundingMode.FLOOR }
                .format(numb.toDouble() / 1000) + "K" // < 1_000_000
            else -> DecimalFormat("#.#").apply { roundingMode = RoundingMode.FLOOR }
                .format(numb.toDouble() / 1_000_000) + "M"

        }
    }
}