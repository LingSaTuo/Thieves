package com.lingsatuo.thieves

import android.media.MediaPlayer
import android.widget.ImageView
import com.lingsatuo.getqqmusic.MusicItem
import com.lingsatuo.getqqmusic.RunOnUiThread
import com.lingsatuo.service.MusicService

object Controller {
    var list = ArrayList<MusicItem>()
    var index = 0
    private val statelistener = ArrayList<(Type) -> Unit>()
    private val bufferingUpdateListener = ArrayList<(MediaPlayer, Int) -> Unit>()
    val listener: (Type) -> Unit = { type ->
        when (type) {
            Controller.Type.PLAY -> {
                if (!mediaPlayer.isPlaying) {
                    if (MusicService.instance?.path != null) {
                        mediaPlayer.start()
                        for (index in 0 until statelistener.size) {
                            if (index < statelistener.size) {
                                statelistener[index].invoke(Type.PLAY)
                            }
                        }
                    }
                }
            }
            Type.PAUSE -> {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                    for (index in 0 until statelistener.size) {
                        if (index < statelistener.size) {
                            statelistener[index].invoke(Type.PAUSE)
                        }
                    }
                }
            }
            Controller.Type.LAST -> {
                index--
                if (index < 0) index = list.size - 1
                if (index >= 0) {
                    MusicService.instance?.start(list[index])
                    for (index in 0 until statelistener.size) {
                        if (index < statelistener.size) {
                            statelistener[index].invoke(Type.LAST)
                        }
                    }
                }
            }
            Controller.Type.NEXT -> {
                index++
                if (index >= list.size) index = 0
                if (index != list.size) {
                    MusicService.instance?.start(list[index])
                    for (index in 0 until statelistener.size) {
                        if (index < statelistener.size) {
                            statelistener[index].invoke(Type.NEXT)
                        }
                    }
                }
            }
        }
    }
    private var mediaPlayer: MediaPlayer = MediaPlayer()

    fun addStateChangeListener(listener: (Type) -> Unit) {
        if (!statelistener.contains(listener)) {
            statelistener.add(listener)
        }
    }

    fun removeStateListener(listener: (Type) -> Unit) {
        if (statelistener.contains(listener)) {
            statelistener.remove(listener)
        }
    }

    fun addBufferingUpdateListener(listener: (MediaPlayer, Int) -> Unit) {
        if (!bufferingUpdateListener.contains(listener)) {
            bufferingUpdateListener.add(listener)
        }
    }

    fun removeBufferingUpdateListener(listener: (MediaPlayer, Int) -> Unit) {
        if (bufferingUpdateListener.contains(listener)) {
            bufferingUpdateListener.remove(listener)
        }
    }

    fun setMediaPlayer(mediaPlayer: MediaPlayer) {
        if (mediaPlayer.isPlaying) mediaPlayer.stop()
        this.mediaPlayer = mediaPlayer
        mediaPlayer.setOnCompletionListener {
            listener.invoke(Controller.Type.NEXT)
        }
        mediaPlayer.setOnBufferingUpdateListener { mp, percent ->
            for (index in 0 until bufferingUpdateListener.size) {
                if (index < bufferingUpdateListener.size) {
                    RunOnUiThread {
                        bufferingUpdateListener[index].invoke(mp, percent)
                    }
                }
            }
        }
    }

    enum class Type {
        PLAY, NEXT, LAST, PAUSE
    }
}