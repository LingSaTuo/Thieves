package com.lingsatuo.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Process
import com.lingsatuo.lingapplication.LingBaseTop
import com.lingsatuo.service.MusicService
import com.lingsatuo.thieves.Controller
import com.lingsatuo.thieves.R

class NotifictionReceiver : BroadcastReceiver() {
    companion object {
        val TYPE = "MusicOneMusicRecevier"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        when (intent.action) {
            "musicone_pause_play" -> {
                if (MusicService.player.isPlaying)
                    Controller.listener.invoke(Controller.Type.PAUSE)
                else
                    Controller.listener.invoke(Controller.Type.PLAY)
            }
            "musicone_next" -> {
                Controller.listener.invoke(Controller.Type.NEXT)
            }
            "musicone_close" -> {
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(R.string.app_name)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationManager.deleteNotificationChannel("MusicOne")
                }
                MusicService.instance?.stop()
                MusicService.instance?.stopSelf()
                MusicService.instance=null
                Controller.clear()
                LingBaseTop.exitApp()
            }
        }
    }
}