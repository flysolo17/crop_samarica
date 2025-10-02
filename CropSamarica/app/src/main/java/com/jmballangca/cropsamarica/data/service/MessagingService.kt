package com.jmballangca.cropsamarica.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jmballangca.cropsamarica.MainActivity
import com.jmballangca.cropsamarica.R
import kotlin.random.Random

const val TAG = "MessagingService"
class MessagingService : FirebaseMessagingService() {

    private val manager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Token: $token")

    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        // Handle notification payload
        message.notification?.let {
            showNotification(it)
        }

        // Handle data payload
        if (message.data.isNotEmpty()) {
            handleDataMessage(message.data.toMap())
        }
    }

    private fun handleDataMessage(toMap: Map<String, String>) {

        Log.d(TAG, "Data Message: $toMap")

    }

    private fun showNotification(remoteMessage: RemoteMessage.Notification) {

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val channelId = "Default"



        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(remoteMessage.title ?: "")
            .setContentText(remoteMessage.body ?: "")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val channelName = "Firebase Messaging"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }
        manager.notify(Random.nextInt(), notification)



    }
}
