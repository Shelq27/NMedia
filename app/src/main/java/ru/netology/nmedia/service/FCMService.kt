package ru.netology.nmedia.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.AppActivity
import ru.netology.nmedia.service.Actions.LIKE
import ru.netology.nmedia.service.Actions.NEWPOST
import ru.netology.nmedia.service.Actions.valueOf
import kotlin.random.Random

class FCMService : FirebaseMessagingService() {
    private val channelId = "server"
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        message.data["action"]?.let {
            when (valueOf(it)) {
                LIKE -> handleLike(
                    Gson().fromJson(
                        message.data["content"],Like::class.java)
                )
                NEWPOST -> pushNewPost(
                    Gson().fromJson(message.data["content"], NewPost::class.java)
                )
            }
        }
        println(Gson().toJson(message))
    }

    private fun handleLike(like: Like) {

        val intent = Intent(this, AppActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_netology_48dp)
            .setContentText(
                getString(
                    R.string.notification_user_liked,
                    like.userName,
                    like.postAuthor
                )
            )
            .setContentIntent(pi)
            .setAutoCancel(true)
            .build()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(this).notify(
                Random.nextInt(100_000),
                notification
            )
        }
    }

    private fun pushNewPost(newPost: NewPost) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_netology_48dp)
            .setContentTitle(getString(R.string.notification_new_post, newPost.postAuthor))
            .setContentText(newPost.postSmallContent)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(newPost.postBigContent)
            )
            .build()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(this).notify(
                Random.nextInt(100_000),
                notification
            )
        }

    }

    override fun onNewToken(token: String) {
        println(token)
    }
}

enum class Actions {
    LIKE, NEWPOST
}

data class Like(
    val userId: Int,
    val userName: String,
    val postId: Int,
    val postAuthor: String
)

data class NewPost(
    val postId: Int,
    val postAuthor: String,
    val postSmallContent: String,
    val postBigContent: String
)
