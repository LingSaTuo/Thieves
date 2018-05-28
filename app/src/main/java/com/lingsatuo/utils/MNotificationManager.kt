package com.lingsatuo.utils

import android.app.Notification
import android.app.Notification.*
import android.app.NotificationManager
import android.content.Context
import android.widget.RemoteViews
import com.lingsatuo.thieves.R

object MNotificationManager {
    fun show(context: Context){
        val notificationManager =  context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = Notification(R.mipmap.ic_launcher,"MusicOne",System.currentTimeMillis())
        notification.flags = FLAG_ONGOING_EVENT or FLAG_NO_CLEAR
        val remoteview = RemoteViews(context.packageName, R.layout.notifiction_view)
        notification.contentView = remoteview
        notificationManager.notify(0,notification)
    }
}