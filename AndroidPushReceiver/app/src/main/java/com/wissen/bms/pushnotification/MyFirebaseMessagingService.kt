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
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        const val CHANNEL_ID = "battery_fault_notifications"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM Token", "Generated new FCM token: $token")
        // Optionally, send this token to your backend server
        sendTokenToServer(token)
    }

    private fun sendTokenToServer(token: String) {
        // Logic to send the token to your server
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("Message Received", "On Message received from FCM")
        super.onMessageReceived(remoteMessage)

        // Extract data from the FCM payload
        val title = remoteMessage.notification?.title ?: "Battery Alert"
        val body = remoteMessage.notification?.body ?: "Check battery status!"
        val batteryId = remoteMessage.data["batteryId"]
        val risk = remoteMessage.data["risk"]
        val faultReason = remoteMessage.data["faultReason"]
        val recommendation = remoteMessage.data["recommendation"]

        // Show the notification
        showNotification(title, body, batteryId, risk, faultReason, recommendation)
    }

    private fun showNotification(title: String, body: String, batteryId: String?, risk: String?, faultReason: String?, recommendation: String?) {
        createNotificationChannel()

        // Prepare intent to open activity on notification click
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Format risk, faultReason in red and recommendation in green
        val styledText = SpannableStringBuilder()

        batteryId?.let {
            val batteryIdText = "Battery Id: $it\n"
            styledText.append(batteryIdText).setSpan(ForegroundColorSpan(Color.RED), 0, batteryIdText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        risk?.let {
            val riskText = "Risk: $it\n"
            styledText.append(riskText).setSpan(ForegroundColorSpan(Color.RED), 0, riskText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        faultReason?.let {
            val faultText = "Fault Reason: $it\n"
            styledText.append(faultText).setSpan(ForegroundColorSpan(Color.RED), 0, faultText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        recommendation?.let {
            val recommendationText = "Recommendation: $it"
            styledText.append(recommendationText).setSpan(ForegroundColorSpan(Color.GREEN), 0, recommendationText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        // Prepare the BigTextStyle with the formatted text (for expanded view)
        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(styledText)

        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification) // Replace with your icon
            .setContentTitle(title)
            .setContentText(body) // Use regular text for the collapsed notification
            .setStyle(bigTextStyle) // This will show the styled text when expanded
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Check if permission is granted to show notifications
        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Permission Denied: ", "Post notification permission denied")
            return
        }

        // Show the notification
        notificationManager.notify(1, notificationBuilder.build())
    }

    // Notification Channel setup (if needed)
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Battery Health Channel"
            val descriptionText = "Channel for battery health notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
