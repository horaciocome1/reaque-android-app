package io.github.horaciocome1.reaque.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.data.users.UsersService
import io.github.horaciocome1.reaque.ui.MainActivity
import io.github.horaciocome1.reaque.util.Constants.MAIN_ACTIVITY
import io.github.horaciocome1.reaque.util.Constants.USER_ID

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String?) {
        val service = UsersService()
        token?.let { service.updateRegistrationToken(it) {} }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        remoteMessage?.let {
            if (it.data.isNotEmpty() && it.notification != null) {
                val userId = it.data[USER_ID]
                val title = it.notification!!.title
                val body = it.notification!!.body
                val clickAction = it.notification!!.clickAction
                if (userId != null && title != null && body != null && clickAction != null)
                    if (userId.isNotBlank() && title.isNotBlank() && body.isNotBlank() && clickAction.isNotBlank())
                        sendNotification(title, body, clickAction, userId)
            }
        }
    }

    private fun sendNotification(title: String, body: String, clickAction: String, userId: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        if (clickAction == MAIN_ACTIVITY)
            intent.putExtra(USER_ID, userId)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notification =
            NotificationCompat.Builder(this, resources.getString(R.string.default_notification_channel_id))
                .apply {
                    setSmallIcon(R.mipmap.ic_launcher)
                    setContentTitle(title)
                    setContentText(body)
                    setAutoCancel(true)
                    setSound(sound)
                    setContentIntent(pendingIntent)
                }
                .build()
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        manager?.notify(0, notification)
    }

}