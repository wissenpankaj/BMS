package com.wissen.bms.pushnotification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        const val CHANNEL_ID = "notifications_channel"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM Token", "Generated new FCM token: $token")
        sendTokenToServer(token)
    }

    private fun sendTokenToServer(token: String) {
        // Logic to send the token to your server
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("Message Received", "On Message received from FCM")
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.notification?.title ?: "Notification"
        val body = remoteMessage.notification?.body ?: "Check the latest update!"

        val isBatteryFault = remoteMessage.data.containsKey("batteryId")

        if (isBatteryFault) {
            // Extract BatteryFault specific data
            val batteryId = remoteMessage.data["batteryId"]
            val risk = remoteMessage.data["risk"]
            val faultReason = remoteMessage.data["faultReason"]
            val recommendation = remoteMessage.data["recommendation"]

            showNotification(
                title, body, NotificationType.BATTERY_FAULT,
                batteryId, risk, faultReason, recommendation, null, null
            )
        } else {
            // Extract StationInfoDTO specific data
            val stationName = remoteMessage.data["stationName"]
            val availableStock = remoteMessage.data["availableStock"]
            val latitude = remoteMessage.data["latitude"]
            val longitude = remoteMessage.data["longitude"]

            showNotification(
                title, body, NotificationType.STATION_INFO,
                null, null, null, null, stationName, availableStock, latitude, longitude
            )
        }
    }

    private fun showNotification(
        title: String,
        body: String,
        type: NotificationType,
        batteryId: String? = null,
        risk: String? = null,
        faultReason: String? = null,
        recommendation: String? = null,
        stationName: String? = null,
        availableStock: String? = null,
        latitude: String? = null,
        longitude: String? = null
    ) {
        createNotificationChannel()

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val styledText = SpannableStringBuilder()

        when (type) {
            NotificationType.BATTERY_FAULT -> {
                batteryId?.let {
                    styledText.append("Battery Id: $it\n")
                        .setSpan(ForegroundColorSpan(Color.RED), 0, styledText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                risk?.let {
                    val start = styledText.length
                    styledText.append("Risk: $it\n")
                        .setSpan(ForegroundColorSpan(Color.RED), start, styledText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                faultReason?.let {
                    val start = styledText.length
                    styledText.append("Fault Reason: $it\n")
                        .setSpan(ForegroundColorSpan(Color.RED), start, styledText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                recommendation?.let {
                    val start = styledText.length
                    styledText.append("Recommendation: $it")
                        .setSpan(ForegroundColorSpan(Color.GREEN), start, styledText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
            NotificationType.STATION_INFO -> {
                stationName?.let {
                    styledText.append("Station Name: $it\n")
                }
                availableStock?.let {
                    styledText.append("Available Stock: $it\n")
                }
                latitude?.let {
                    styledText.append("Latitude: $it\n")
                }
                longitude?.let {
                    styledText.append("Longitude: $it")
                }
            }
        }

        val bigTextStyle = NotificationCompat.BigTextStyle().bigText(styledText)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(bigTextStyle)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Permission Denied", "Post notification permission denied")
            return
        }

        notificationManager.notify(1, notificationBuilder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notifications Channel"
            val descriptionText = "Channel for notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    enum class NotificationType {
        BATTERY_FAULT, STATION_INFO
    }
}